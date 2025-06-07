// Crie no pacote: robos.subsistemas
package robos.subsistemas;

import ambiente.CentralComunicacao;
import excecoes.ambiente.ErroComunicacaoException;
import excecoes.robos.gerais.RoboDesligadoException;
import interfaces.Comunicavel;
import robos.geral.Robo;

/**
 * Subsistema que encapsula a lógica de comunicação para robôs.
 * Implementa a interface Comunicavel e gerencia o envio e recebimento de mensagens.
 */
public class ModuloComunicacao implements Comunicavel {

    private Robo roboPai; // Referência ao robô que possui este módulo

    /**
     * Construtor do módulo de comunicação.
     * @param roboPai O robô que este módulo servirá.
     */
    public ModuloComunicacao(Robo roboPai) {
        this.roboPai = roboPai;
    }

    /**
     * Envia uma mensagem para outro robô comunicável.
     * A lógica foi extraída das classes originais (ex: Correios, DroneVigilancia).
     */
    @Override
    public String enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central)
            throws ErroComunicacaoException, RoboDesligadoException {
        
        if (destinatario == null) {
            throw new ErroComunicacaoException("Destinatário não pode ser nulo.");
        }
        if (!roboPai.getEstado()) {
            throw new RoboDesligadoException("Robô " + roboPai.getNome() + " está desligado e não pode enviar mensagens.");
        }

        try {
            // A central registra a mensagem usando os robôs como remetente/destinatário
            central.registrarMensagem((Comunicavel)this.roboPai, destinatario, mensagem);
            
            // O destinatário processa o recebimento
            String respostaDestinatario = destinatario.receberMensagem((Comunicavel)this.roboPai, mensagem);
            
            return "Mensagem enviada com sucesso por " + this.roboPai.getNome() + ".\n" + respostaDestinatario;
        } catch (RoboDesligadoException | ErroComunicacaoException e) {
            // Re-lança a exceção para ser tratada pelo chamador
            throw new ErroComunicacaoException("Falha ao enviar mensagem: " + e.getMessage());
        }
    }

    /**
     * Recebe uma mensagem de outro robô comunicável.
     * A lógica foi extraída das classes originais.
     */
    @Override
    public String receberMensagem(Comunicavel remetente, String mensagem)
            throws ErroComunicacaoException, RoboDesligadoException {

        if (!roboPai.getEstado()) {
            throw new RoboDesligadoException("Robô " + roboPai.getNome() + " está desligado e não pode receber mensagens.");
        }
        if (remetente == null) {
            throw new ErroComunicacaoException("Remetente não pode ser nulo.");
        }

        // Simplesmente confirma o recebimento.
        // A mensagem já foi registrada pela central no método enviarMensagem.
        return roboPai.getNome() + " recebeu mensagem de " + ((Robo) remetente).getNome() + ": \"" + mensagem + "\"";
    }
}