package robos.terrestres;
import ambiente.Ambiente;
import ambiente.Obstaculo;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import interfaces.*;
import excecoes.robos.especificos.AlvoInvalidoException;
import excecoes.robos.especificos.MunicaoInsuficienteException;
import excecoes.robos.gerais.MovimentoInvalidoException;
import excecoes.robos.gerais.RoboDestruidoPorBuracoException;
import excecoes.sensor.SensorException;

/**
 * Classe que representa um tanque de guerra terrestre com capacidades de ataque
 * Estende RoboTerrestre e implementa a interface Atacante para combate
 */
public class TanqueGuerra extends RoboTerrestre implements Atacante {
    private int municaoMax;     // Capacidade máxima de munição
    private int municaoAtual;   // Quantidade atual de munição disponível
    private int alcance;        // Alcance máximo de ataque

    /**
     * Construtor do tanque de guerra com especificações completas
     * @param nome Nome identificador do tanque
     * @param direcao Direção inicial do tanque
     * @param ambiente Ambiente onde o tanque opera
     * @param material Material de construção do tanque
     * @param posicaoX Coordenada X inicial
     * @param posicaoY Coordenada Y inicial
     * @param velocidade Velocidade inicial do tanque
     * @param velocidadeMaxima Velocidade máxima do tanque
     * @param municaoMax Capacidade máxima de munição
     * @param alcance Alcance máximo de ataque
     */
    public TanqueGuerra(String nome, String direcao, Ambiente ambiente, MateriaisRobo material, int posicaoX, int posicaoY, int velocidade, int velocidadeMaxima, int municaoMax,
            int alcance) {
        super(nome, direcao, ambiente, material, posicaoX, posicaoY, velocidade, velocidadeMaxima);

        this.municaoMax = municaoMax;
        this.municaoAtual = municaoMax;
        this.alcance = alcance;
        setIntegridade(100);
    }

    /**
     * Executa tarefas específicas do tanque de guerra
     * Suporta ataques e recarregamento de munição
     * @param argumentos Array de argumentos variados dependendo da tarefa
     * @return String com o resultado da execução da tarefa
     * @throws MovimentoInvalidoException 
     * @throws RoboDestruidoPorBuracoException 
     */
    public String executarTarefa(Object... argumentos) throws RoboDestruidoPorBuracoException, MovimentoInvalidoException {
        String result = super.executarTarefa(argumentos);
        if (result != ""){
            return result;
        }
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "atirar":
                int alvoX = (Integer) argumentos[1];
                int alvoY = (Integer) argumentos[2];
                int nTiros = (Integer) argumentos[3];
                Ambiente ambiente = (Ambiente) argumentos[4];
                try {
                    return atirar(alvoX, alvoY, 0, nTiros, ambiente);
                } catch (SensorException | MunicaoInsuficienteException | AlvoInvalidoException e) {
                    return e.getMessage();
                }
        
            case "recarregar":
                int nBalas = (Integer) argumentos[1];
                return recarregar(nBalas);

            default:
                return "";
        }
    }

    /**
     * Executa um ataque contra coordenadas específicas no ambiente
     * Verifica munição, alcance e validade do alvo antes de executar
     * Aplica dano apenas em entidades na superfície (Z=0)
     * @param alvoX Coordenada X do alvo
     * @param alvoY Coordenada Y do alvo
     * @param alvoZ Coordenada Z (ignorada - tanques atacam apenas superfície)
     * @param nTiros Número de tiros a serem disparados
     * @param ambiente Referência ao ambiente onde ocorre o ataque
     * @return String descrevendo o resultado detalhado do ataque
     * @throws SensorException Se houver problemas com GPS/sensores
     * @throws MunicaoInsuficienteException Se não houver munição suficiente
     * @throws AlvoInvalidoException Se o alvo for inválido (próprio tanque)
     */
    public String atirar(int alvoX, int alvoY, int alvoZ, int nTiros, Ambiente ambiente) throws SensorException, MunicaoInsuficienteException, AlvoInvalidoException {
        verificarGPSAtivo();

        Entidade alvo = ambiente.identificarEntidadePosicao(alvoX, alvoY, 0);

        if (municaoAtual < nTiros) {
            throw new MunicaoInsuficienteException();
        } 
        if (alvoX == getX() && alvoY == getY()) {
            throw new AlvoInvalidoException("Não é possível atirar no próprio robô");
        } 

        int dX = getX() - alvoX;
        int dY = getY() - alvoY;
        if (Math.pow(dX, 2) + Math.pow(dY, 2) <= Math.pow(alcance, 2)) {
            municaoAtual -= nTiros;
            if (alvo == null) {
                return String.format("Disparado realizado no alvo (%d, %d)\nNenhum alvo foi atingido!\n", alvoX,
                        alvoY);
            } else {
                String result = null;
                if (alvo.getTipo() == TipoEntidade.ROBO) {
                    Robo alvodef = (Robo) alvo;
                    String defesa = alvodef.defender(nTiros, ambiente);
                    result = String.format(
                            "Disparo realizado no alvo (%d, %d)\n" +
                                    "Robô %s foi atingido!\n" +
                                    defesa,
                            alvoX, alvoY, alvodef.getNome());
                } else if (alvo.getTipo() == TipoEntidade.OBSTACULO) {
                    Obstaculo alvodef = (Obstaculo) alvo;
                    String defesa = alvodef.defender(nTiros, ambiente);
                    result = String.format(
                            "Disparo realizado no alvo (%d, %d)\n" +
                                    "Obstáculo %s foi atingido!\n" +
                                    defesa,
                            alvoX, alvoY, alvodef.getTipoObstaculo());

                }
                return result;
            }
        } else {
            return "Alvo fora do alcance";
        }
    }

    /**
     * Recarrega a munição do tanque adicionando o número especificado de projéteis
     * Garante que a munição não exceda a capacidade máxima
     * @param nBalas Número de projéteis a serem adicionados ao arsenal
     * @return String confirmando a conclusão do recarregamento
     */
    public String recarregar(int nBalas) {
        municaoAtual += nBalas;
        if (municaoAtual > municaoMax) {
            municaoAtual = municaoMax;
        }
        return "Recarregamento concluido";
    }
}
