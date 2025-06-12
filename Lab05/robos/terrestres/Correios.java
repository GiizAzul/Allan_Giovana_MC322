package robos.terrestres;

import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.CentralComunicacao;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.subsistemas.ModuloComunicacao;
import excecoes.robos.especificos.*;
import excecoes.ambiente.*;
import excecoes.logger.LoggerException;
import excecoes.robos.gerais.*;
import excecoes.sensor.SensorException;
import interfaces.*;

/**
 * Classe que representa um robô de correios terrestre com capacidades de
 * entrega
 * Estende RoboTerrestre e implementa Comunicavel
 */
public class Correios extends RoboTerrestre implements Comunicavel {
    private int capacidadeMax; // Capacidade máxima de pacotes
    private float pesoMax; // Peso máximo suportado
    private float pesoAtual; // Peso atual carregado
    private ArrayList<String> entregas; // Lista de IDs dos pacotes
    private ArrayList<Float> pesos; // Lista de pesos dos pacotes
    private ModuloComunicacao moduloComunicacao;

    /**
     * Construtor do robô de correios com especificações completas
     * 
     * @param nome             Nome identificador do robô
     * @param direcao          Direção inicial do robô
     * @param ambiente         Ambiente onde o robô opera
     * @param material         Material de construção do robô
     * @param posicaoX         Coordenada X inicial
     * @param posicaoY         Coordenada Y inicial
     * @param velocidade       Velocidade inicial do robô
     * @param velocidadeMaxima Velocidade máxima do robô
     * @param capacidadeMax    Capacidade máxima de pacotes
     * @param pesoMax          Peso máximo suportado
     */
    public Correios(String nome, String direcao, Ambiente ambiente, MateriaisRobo material, int posicaoX, int posicaoY,
            int velocidade, int velocidadeMaxima, int capacidadeMax,
            float pesoMax) {
        super(nome, direcao, ambiente, material, posicaoX, posicaoY, velocidade, velocidadeMaxima);
        this.capacidadeMax = capacidadeMax;
        this.pesoMax = pesoMax;
        pesoAtual = 0;
        entregas = new ArrayList<String>();
        pesos = new ArrayList<Float>();
        setIntegridade(50);
        this.moduloComunicacao = new ModuloComunicacao(this);
    }

    /**
     * Executa tarefas específicas do robô de correios
     * Suporta carregamento, entrega e listagem de pacotes
     * 
     * @param argumentos Array de argumentos variados dependendo da tarefa
     * @return String com o resultado da execução da tarefa
     * @throws MovimentoInvalidoException
     * @throws RoboDestruidoPorBuracoException
     */
    public String executarTarefa(Object... argumentos)
            throws RoboDestruidoPorBuracoException, MovimentoInvalidoException {
        String result = super.executarTarefa(argumentos);
        if (result != "") {
            return result;
        }
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "carregar":
                String id = (String) argumentos[1];
                float peso = (Float) argumentos[2];
                try {
                    return carregarPacote(id, peso);
                } catch (CapacidadeInsuficienteException | PesoAcimaPermitidoException e) {
                    return "Não foi possível carregar o pacote, erro: " + e.getMessage();
                }

            case "entregar":
                id = (String) argumentos[1];
                int destinoX = (Integer) argumentos[2];
                int destinoY = (Integer) argumentos[3];
                Ambiente ambiente = (Ambiente) argumentos[4];

                try {
                    return entregarPacote(id, destinoX, destinoY, ambiente);
                } catch (PacoteNaoEncontrado | SensorException e) {
                    return "Não foi possível entregar o pacote, erro: " + e.getMessage();
                }

            case "listar":
                return listarEntregas();
            default:
                return "";
        }
    }

    /**
     * Carrega um pacote no robô verificando capacidade e peso
     * 
     * @param id   Identificador único do pacote
     * @param peso Peso do pacote a ser carregado
     * @return String confirmando o carregamento
     * @throws CapacidadeInsuficienteException Se não houver espaço para mais
     *                                         pacotes
     * @throws PesoAcimaPermitidoException     Se o peso exceder a capacidade máxima
     */
    private String carregarPacote(String id, float peso)
            throws CapacidadeInsuficienteException, PesoAcimaPermitidoException {
        if (entregas.size() >= capacidadeMax) {
            throw new CapacidadeInsuficienteException("Não há espaço para mais pacotes");
        }
        if (pesoAtual + peso > pesoMax) {
            throw new PesoAcimaPermitidoException("Peso excede a capacidade máxima");
        }
        entregas.add(id);
        pesos.add(peso);
        pesoAtual += peso;

        return "Pacote carregado com sucesso";
    }

    /**
     * Move o robô para entrega considerando obstáculos especiais como buracos
     * Implementa lógica específica para entregas onde buracos podem ser preenchidos
     * 
     * @param deltaX   Deslocamento na direção X
     * @param deltaY   Deslocamento na direção Y
     * @param ambiente Ambiente onde o robô se move
     * @return true se o movimento foi bem-sucedido, false caso contrário
     * @throws SensorException  Se houver problemas com sensores
     * @throws ColisaoException Se houver colisão com obstáculos impeditivos
     */
    private boolean moverEntrega(int deltaX, int deltaY, Ambiente ambiente) throws SensorException, LoggerException, ColisaoException {
        verificarGPSAtivo();

        // Verifica se o robô está dentro dos limites do ambiente
        int destinoX = getX() + deltaX > ambiente.getTamX() ? ambiente.getTamX() : getX() + deltaX;
        int destinoY = getY() + deltaY > ambiente.getTamY() ? ambiente.getTamY() : getY() + deltaY;

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            for (int x = getX() + passoX; x != destinoX + passoX; x += passoX) {
                Entidade obj = ambiente.identificarEntidadePosicao(x, getY(), 0);
                if (obj != null) {
                    if (obj.getTipo() == TipoEntidade.ROBO) {
                        throw new ColisaoException("O robô " + getNome() + " colidiu com o objeto: "
                                + ((Robo) obj).getNome() + " na posição X:" + x + " Y:" + getY());
                    } else if (((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        return true;
                    } else {
                        throw new ColisaoException("O robô " + getNome() + " colidiu com o objeto: "
                                + ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + x + " Y:" + getY());
                    }
                }
                ambiente.moverEntidade(this, x, getY(), getZ());
                setPosicaoX(x);
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = getY() + passoY; y != destinoY + passoY; y += passoY) {
                Entidade obj = ambiente.identificarEntidadePosicao(getX(), y, 0);
                if (obj != null) {
                    if (obj.getTipo() == TipoEntidade.ROBO) {
                        throw new ColisaoException("O robô " + getNome() + " colidiu com o objeto: "
                                + ((Robo) obj).getNome() + " na posição X:" + getX() + " Y:" + y);
                    } else if (((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        return true;
                    } else {
                        throw new ColisaoException("O robô " + getNome() + " colidiu com o objeto: "
                                + ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + getX() + " Y:" + y);
                    }
                }
                ambiente.moverEntidade(this, getX(), y, getZ());
                setPosicaoY(y);
            }
        }
        return true;
    }

    /**
     * Entrega um pacote específico no destino indicado
     * Remove o pacote da carga e pode preencher buracos com a entrega
     * 
     * @param id       Identificador do pacote a ser entregue
     * @param destinoX Coordenada X de destino da entrega
     * @param destinoY Coordenada Y de destino da entrega
     * @param ambiente Ambiente onde realizar a entrega
     * @return String detalhando o resultado da entrega
     * @throws SensorException     Se houver problemas com sensores
     * @throws PacoteNaoEncontrado Se o pacote não estiver na carga
     */
    private String entregarPacote(String id, int destinoX, int destinoY, Ambiente ambiente)
            throws SensorException, PacoteNaoEncontrado {
        if (!entregas.contains(id)) {
            throw new PacoteNaoEncontrado("Pacote " + id + " não encontrado na carga.");
        }
        int i = entregas.indexOf(id);
        Entidade objeto_posicao = ambiente.identificarEntidadePosicao(destinoX, destinoY, 0);
        boolean resEntrega;
        try {
            resEntrega = moverEntrega(destinoX - getX(), destinoY - getY(), ambiente);
        } catch (SensorException | LoggerException | ColisaoException e) {
            return "Erro ao entregar pacote: " + e.getMessage();
        }
        if (resEntrega) {
            if (objeto_posicao != null && objeto_posicao.getTipo() == TipoEntidade.OBSTACULO
                    && ((Obstaculo) objeto_posicao).getTipoObstaculo() == TipoObstaculo.BURACO) {
                pesoAtual -= pesos.get(i);
                entregas.remove(i);
                pesos.remove(i);
                String texto = "Pacote " + id + " entregue na posição (" + destinoX + ", " + destinoY
                        + ").\nBuraco coberto na posição X1:" + ((Obstaculo) objeto_posicao).getX1() + " X2:"
                        + ((Obstaculo) objeto_posicao).getX2() + " Y1:" + ((Obstaculo) objeto_posicao).getY1()
                        + " Y2:" + ((Obstaculo) objeto_posicao).getY2();
                ambiente.removerEntidade(objeto_posicao);
                return texto;
            }

            pesoAtual -= pesos.get(i);
            entregas.remove(i);
            pesos.remove(i);

            return ("Pacote " + id + " entregue na posição (" + destinoX + ", " + destinoY + ").");
        }
        return "Entrega não concluída";
    }

    /**
     * Lista todas as entregas pendentes no robô
     * 
     * @return String com lista de IDs dos pacotes pendentes ou mensagem se vazio
     */
    private String listarEntregas() {
        if (entregas.isEmpty()) {
            return "Não há entregas pendentes.";
        } else {
            return "Entregas pendentes: " + String.join(", ", entregas);
        }
    }

    /**
     * Envia uma mensagem para outro robô comunicável através da central
     * Registra a mensagem na central e processa a resposta do destinatário
     * 
     * @param destinatario Robô que receberá a mensagem
     * @param mensagem     Conteúdo da mensagem a ser enviada
     * @param central      Central de comunicação para registro
     * @return String confirmando envio e resposta do destinatário
     * @throws ErroComunicacaoException Se houver problemas na comunicação
     * @throws RoboDesligadoException   Se o robô estiver desligado
     */
    public String enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central)
            throws ErroComunicacaoException, RoboDesligadoException {
        return this.moduloComunicacao.enviarMensagem(destinatario, mensagem, central);
    }

    /**
     * Recebe uma mensagem de outro robô comunicável
     * Verifica se o robô está operacional antes de processar a mensagem
     * 
     * @param remetente Robô que enviou a mensagem
     * @param mensagem  Conteúdo da mensagem recebida
     * @return String confirmando recebimento da mensagem
     * @throws ErroComunicacaoException Se houver problemas na comunicação
     * @throws RoboDesligadoException   Se o robô estiver desligado
     */
    public String receberMensagem(Comunicavel remetente, String mensagem)
            throws ErroComunicacaoException, RoboDesligadoException {
        return this.moduloComunicacao.receberMensagem(remetente, mensagem);
    }

    public String executarMissao(Ambiente a) {
        if (temMissao()) {
            String resultado = "Correio " + getNome() + " iniciando execução da missão...\n";
            resultado+=missao.executar(this, a);
            resultado+="\nCorreio " + getNome() + " finalizou a missão.";
            return resultado;
        } else {
            return "Correio " + getNome() + " não possui uma missão para executar.";
        }
    }
}
