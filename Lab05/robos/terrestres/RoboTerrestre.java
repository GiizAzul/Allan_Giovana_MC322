package robos.terrestres;
import ambiente.Ambiente;
import robos.equipamentos.sensores.Colisao;
import robos.geral.AgenteInteligente;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.subsistemas.movimento.ControleMovimentoTerrestre;
import excecoes.robos.gerais.ColisaoException;
import excecoes.robos.gerais.MovimentoInvalidoException;
import excecoes.robos.gerais.RoboDestruidoPorBuracoException;
import excecoes.robos.gerais.VelocidadeMaximaException;
import excecoes.sensor.*;;

/**
 * Classe que representa um robô terrestre, especialização de Robo
 * com capacidade de velocidade limitada
 */
public class RoboTerrestre extends AgenteInteligente {
    private int velocidadeMaxima;  // Velocidade máxima do robô terrestre

    /**
     * Construtor de RoboTerrestre que inicializa o robô na superfície (Z=0)
     * Cria e adiciona o sensor de colisão específico para robôs terrestres
     * @param nome Nome do robô
     * @param direcao Direção inicial
     * @param ambiente Ambiente onde o robô opera (necessário para sensor de colisão)
     * @param material Material de construção do robô
     * @param posicaoX Posição X inicial
     * @param posicaoY Posição Y inicial
     * @param velocidade Velocidade inicial do robô
     * @param velocidadeMaxima Velocidade máxima do robô
     */
    public RoboTerrestre(String nome, String direcao, Ambiente ambiente, MateriaisRobo material, int posicaoX, int posicaoY, int velocidade, int velocidadeMaxima) {
        super(nome, direcao, ambiente, material, posicaoX, posicaoY, 0, velocidade, new ControleMovimentoTerrestre());
        this.velocidadeMaxima = velocidadeMaxima;
        super.gerenciadorSensores.adicionarSensor(new Colisao(this, ambiente, ambiente.getLogger()));
    }

    /**
     * Move o robô terrestre de acordo com o delta especificado, considerando velocidade máxima
     * Utiliza sensor de colisão para detectar obstáculos e outros robôs durante o movimento
     * Robôs terrestres não precisam de GPS para movimentação (usam velocidade * tempo)
     * @param deltaX Deslocamento na direção X
     * @param deltaY Deslocamento na direção Y
     * @param velocidade Velocidade para o movimento atual
     * @param ambiente Ambiente onde o robô está se movendo
     * @throws VelocidadeMaximaException Se a velocidade exceder o limite máximo
     * @throws SensorException Se houver problemas com o sensor de colisão
     * @throws ColisaoException Se houver colisão com obstáculos ou outros robôs
     * @throws MovimentoInvalidoException 
     * @throws RoboDestruidoPorBuracoException 
     */
    public void mover(int deltaX, int deltaY, int velocidade, Ambiente ambiente) throws VelocidadeMaximaException, SensorException, ColisaoException, RoboDestruidoPorBuracoException, MovimentoInvalidoException {
        ((ControleMovimentoTerrestre) this.controleMovimento).mover(this, deltaX, deltaY, velocidade, ambiente);
    }

    /**
     * Obtém a velocidade máxima do robô
     * @return Velocidade máxima
     */
    public int getVelocidadeMaxima() {
        return this.velocidadeMaxima;
    }

    /**
     * Define uma nova velocidade máxima
     * @param vmax Nova velocidade máxima
     */
    public void setVelocidadeMaxima(int vmax) {
        this.velocidadeMaxima = vmax;
    }

    /**
     * Calcula a distância euclidiana 3D entre este robô terrestre e outro robô
     * Considera que robôs terrestres estão sempre na altura 0 (superfície)
     * @param alvo Robô alvo para calcular a distância
     * @return Distância euclidiana 3D (considerando altitude do robô alvo)
     * @throws SensorException Se houver problemas com o GPS
     */
    public double distanciaRobo(Robo alvo) throws SensorException {
        super.verificarGPSAtivo();

        return Math.sqrt(Math.pow(alvo.getXInterno() - this.getX(), 2)
                + Math.pow(alvo.getYInterno() - this.getY(), 2) + Math.pow(alvo.getZ() - 0, 2));
    }

    /**
     * Retorna o sensor de colisão do robô terrestre
     * @return Instância do sensor de colisão
     */
    public Colisao getSensorColisao() {
        return super.gerenciadorSensores.getSensorColisao();
  }

    /**
     * Executa tarefas específicas de robôs terrestres baseadas nos argumentos fornecidos
     * Suporta movimento com velocidade controlada e configuração de velocidade máxima
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
            case "mover":
                int deltaX = (Integer) argumentos[1];
                int deltaY = (Integer) argumentos[2];
                int velocidade = (Integer) argumentos[3];
                Ambiente ambiente = (Ambiente) argumentos[4];
                try {
                    mover(deltaX, deltaY, velocidade, ambiente);
                } catch (VelocidadeMaximaException | SensorException | ColisaoException e) {
                    return "Não foi possível mover o robô, erro: " + e.getMessage();
                }
                return "";

            case "velocidade":
                int vel = (Integer) argumentos[1];
                setVelocidadeMaxima(vel);
                return "";

            default:
                return "";
        }
    }

    public String executarMissao ( Ambiente a ) {
        return null;
    }

}
