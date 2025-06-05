package interfaces;

import ambiente.CentralComunicacao;
import excecoes.ambiente.ErroComunicacaoException;
import excecoes.robos.gerais.RoboDesligadoException;

/**
 * Interface que define as capacidades de comunicação para robôs.
 * Implementada por robôs que podem enviar e receber mensagens através da central de comunicação.
 */
public interface Comunicavel {
    
    /**
     * Envia uma mensagem para outro robô comunicável através da central de comunicação
     * @param destinatario Robô que receberá a mensagem (deve implementar Comunicavel)
     * @param mensagem Conteúdo da mensagem a ser enviada
     * @param central Central de comunicação responsável por gerenciar a transmissão
     * @return String confirmando o envio da mensagem
     * @throws ErroComunicacaoException Se houver falha na comunicação ou destinatário inválido
     * @throws RoboDesligadoException Se o robô remetente ou destinatário estiver desligado
     */
    String enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central) 
        throws ErroComunicacaoException, RoboDesligadoException;
    
    /**
     * Recebe uma mensagem de outro robô comunicável
     * @param remetente Robô que enviou a mensagem (deve implementar Comunicavel)
     * @param mensagem Conteúdo da mensagem recebida
     * @return String confirmando o recebimento da mensagem
     * @throws ErroComunicacaoException Se houver falha na comunicação ou remetente inválido
     * @throws RoboDesligadoException Se o robô receptor estiver desligado
     */
    String receberMensagem(Comunicavel remetente, String mensagem) 
        throws ErroComunicacaoException, RoboDesligadoException;
}
