package robos.equipamentos.sensores;

import java.util.ArrayList;
import ambiente.Ambiente;
import ambiente.Obstaculo;
import interfaces.Entidade;
import robos.geral.Robo;
import robos.aereos.RoboAereo;

/**
 * Classe que implementa um sensor de Radar para robôs aéreos.
 * O Radar é capaz de detectar tanto obstáculos quanto outros robôs em um determinado
 * raio de alcance e ângulo de abertura a partir da posição do robô equipado.
 * Estende a classe Sensor com tipo parametrizado ArrayList<Entidade> para retornar
 * os objetos detectados.
 */
public class Radar extends Sensor<ArrayList<Entidade>> {
    private RoboAereo robo;         // Robô aéreo ao qual o radar está acoplado
    private Ambiente ambiente;      // Ambiente onde o radar realiza a detecção
    private int raioAlcance;        // Alcance máximo do radar em unidades de distância
    private int anguloAlcance;      // Ângulo de abertura vertical do radar em graus

    /**
     * Construtor do Radar.
     * 
     * @param roboAir Robô aéreo ao qual o radar será acoplado
     * @param ambienteSensor Ambiente onde o radar fará a detecção
     * @param raioAlcance Alcance máximo do radar em unidades de distância
     * @param anguloAlcance Ângulo de abertura vertical do radar em graus
     */
    public Radar(RoboAereo roboAir, Ambiente ambienteSensor, int raioAlcance, int anguloAlcance) {
        super();
        this.raioAlcance = raioAlcance;
        this.anguloAlcance = anguloAlcance;
        this.ambiente = ambienteSensor;
        this.robo = roboAir;
    }

    /**
     * Aciona o radar para detectar objetos no ambiente.
     * Verifica todos os obstáculos e robôs no ambiente e retorna aqueles que:
     * 1. Estão dentro do raio de alcance do radar
     * 2. Estão dentro do ângulo de abertura vertical do radar
     * 3. São "visíveis" para o radar (no caso dos robôs)
     * 
     * @return ArrayList contendo todos os objetos (obstáculos e robôs) detectados pelo radar
     */
    public ArrayList<Entidade> acionar() {
        ArrayList<Obstaculo> obstaculosAmbiente = ambiente.getListaObstaculos();
        ArrayList<Obstaculo> obstaculosEncontrados = new ArrayList<Obstaculo>();
        
        // Verifica quais obstáculos estão no alcance do radar
        for (Obstaculo obstaculo : obstaculosAmbiente) {
            if (verificacaoObstaculo(obstaculo)) {
                obstaculosEncontrados.add(obstaculo);
            }
        }

        // Verifica quais robôs estão no alcance do radar
        ArrayList<Robo> robosAmbiente = ambiente.getListaRobos();
        ArrayList<Robo> robosEncontrados = new ArrayList<Robo>();
        for (Robo roboAmbiente : robosAmbiente) {
            if (verificacaoRobo(roboAmbiente)) {
                robosEncontrados.add(roboAmbiente);
            }
        }

        // Combina os resultados em uma única lista
        ArrayList<Entidade> resultado = new ArrayList<Entidade>();
        resultado.addAll(obstaculosEncontrados);
        resultado.addAll(robosEncontrados);
        
        return resultado;
    }

    /**
     * Verifica se um obstáculo está dentro do alcance do radar.
     * 
     * @param obstaculo Obstáculo a ser verificado
     * @return true se o obstáculo estiver dentro do alcance do radar, false caso contrário
     */
    private boolean verificacaoObstaculo(Obstaculo obstaculo) {
        // Calcula o ponto central do obstáculo
        float xObstaculo = (obstaculo.getX1() + obstaculo.getX2()) / 2;
        float yObstaculo = (obstaculo.getY1() + obstaculo.getY2()) / 2;
        float zObstaculo = 0; // Por padrão, obstáculos começam na altura 0
        
        // Verifica se está dentro do alcance radial corrigido pelo tipo do obstáculo
        if (this.getDistanciaPontoRadar3D(xObstaculo, yObstaculo, zObstaculo) > getAlcanceCorrigido(obstaculo)) {
            return false; // Não detectável - fora do alcance
        }

        // Verifica se está dentro do ângulo de abertura do radar
        return nucleoComumVerificacao(xObstaculo, yObstaculo, zObstaculo);
    }

    /**
     * Verifica se um robô está dentro do alcance do radar.
     * 
     * @param roboAmbiente Robô a ser verificado
     * @return true se o robô estiver dentro do alcance do radar, false caso contrário
     */
    private boolean verificacaoRobo(Robo roboAmbiente) {
        // Obtém a posição do robô no ambiente
        int xRoboAmbiente = roboAmbiente.getPosicaoXInterna();
        int yRoboAmbiente = roboAmbiente.getPosicaoYInterna();
        int zRoboAmbiente = roboAmbiente.getZ();

        // Verifica se o robô está visível - robôs camuflados não são detectados
        if (!roboAmbiente.getVisivel()) {
            return false;
        }

        // Verifica se está dentro do alcance radial corrigido pelo material do robô
        if (this.getDistanciaPontoRadar3D(xRoboAmbiente, yRoboAmbiente, zRoboAmbiente) > getAlcanceCorrigido(roboAmbiente)) {
            return false; // Não detectável - fora do alcance
        }

        // Verifica se está dentro do ângulo de abertura do radar
        return nucleoComumVerificacao(xRoboAmbiente, yRoboAmbiente, zRoboAmbiente);
    }

    /**
     * Verifica se um ponto (x,y,z) está dentro do ângulo de abertura do radar,
     * considerando a direção atual do robô.
     * 
     * @param x Coordenada X do ponto
     * @param y Coordenada Y do ponto
     * @param z Coordenada Z do ponto
     * @return true se o ponto estiver dentro do ângulo de abertura, false caso contrário
     */
    private boolean nucleoComumVerificacao(float x, float y, float z) {
        float anguloRotacao = 0;

        // Ajusta o ângulo de rotação com base na direção do robô
        switch (this.robo.getDirecao()) {
            case "Norte":
                anguloRotacao = -90f;
                break;
            case "Sul":
                anguloRotacao = -270f;
                break;
            case "Leste":
                anguloRotacao = 0f;
                break;
            case "Oeste":
                anguloRotacao = -180f;
                break;
            default:
                break;
        }

        // Rotaciona o ponto de acordo com a direção do robô
        double[] pontoRotacionado = rotacionarPonto(x, y, anguloRotacao);
        
        // Calcula a distância horizontal (raio) do ponto ao robô
        double r = this.getDistanciaPontoRadar2D(pontoRotacionado[0], pontoRotacionado[1]);
        
        // Calcula o ângulo vertical entre o robô e o objeto
        double anguloObjeto = Math.atan((this.robo.getZ() - z) / (r));
        
        // Verifica se o ângulo está dentro do ângulo de abertura do radar
        if (Math.abs(anguloObjeto) > this.anguloAlcance) {
            return false; // Não detectável - fora do ângulo de abertura
        }

        return true; // Detectável - dentro do ângulo de abertura
    }

    /**
     * Rotaciona um ponto (x,y) em torno da origem pelo ângulo especificado.
     * Usado para ajustar as coordenadas com base na direção do robô.
     * 
     * @param xP Coordenada X do ponto
     * @param yP Coordenada Y do ponto
     * @param angulo Ângulo de rotação em graus
     * @return Vetor com as coordenadas X e Y rotacionadas
     */
    private double[] rotacionarPonto(float xP, float yP, float angulo) {
        double radiano = Math.toRadians(angulo);
        double xRotacionado = (xP * Math.cos(radiano)) - (yP * Math.sin(radiano));
        double yRotacionado = (xP * Math.sin(radiano)) + (yP * Math.cos(radiano));

        return new double[] {xRotacionado, yRotacionado};
    }

    /**
     * Calcula a distância euclidiana 2D entre o robô e um ponto (x,y).
     * 
     * @param x Coordenada X do ponto
     * @param y Coordenada Y do ponto
     * @return Distância 2D entre o robô e o ponto
     */
    public double getDistanciaPontoRadar2D(double x, double y) {
        return Math.sqrt(Math.pow(x - robo.getX(), 2) + Math.pow(y - robo.getY(), 2));
    }

    /**
     * Calcula a distância euclidiana 3D entre o robô e um ponto (x,y,z).
     * 
     * @param x Coordenada X do ponto
     * @param y Coordenada Y do ponto
     * @param z Coordenada Z do ponto
     * @return Distância 3D entre o robô e o ponto
     */
    public double getDistanciaPontoRadar3D(double x, double y, double z) {
        return Math.sqrt(Math.pow(x - robo.getX(), 2) + Math.pow(y - robo.getY(), 2) + Math.pow(z - robo.getZ(), 2));
    }

    /**
     * Calcula o alcance efetivo do radar para um obstáculo específico,
     * considerando o fator de redução do tipo de obstáculo.
     * 
     * @param obstaculo Obstáculo para cálculo do alcance efetivo
     * @return Alcance efetivo do radar para o obstáculo
     */
    public double getAlcanceCorrigido(Obstaculo obstaculo) {
        return this.raioAlcance * obstaculo.getTipoObstaculo().getFatorReducaoAlcanceRadar();
    }

    /**
     * Calcula o alcance efetivo do radar para um robô específico,
     * considerando o fator de redução do material do robô.
     * 
     * @param robo Robô para cálculo do alcance efetivo
     * @return Alcance efetivo do radar para o robô
     */
    public double getAlcanceCorrigido(Robo robo) {
        return this.raioAlcance * robo.getMateriaisRobo().getFatorReducaoAlcanceRadar();
    }

    /**
     * Obtém o raio de alcance atual do radar.
     * 
     * @return Raio de alcance do radar
     */
    public int getRaioAlcance() {
        return raioAlcance;
    }

    /**
     * Define um novo raio de alcance para o radar.
     * 
     * @param alcanceRadar Novo raio de alcance
     */
    public void setRaioAlcance(int alcanceRadar) {
        this.raioAlcance = alcanceRadar;
    }

    /**
     * Obtém o ângulo de abertura atual do radar.
     * 
     * @return Ângulo de abertura do radar em graus
     */
    public int getAnguloAlcance() {
        return anguloAlcance;
    }

    /**
     * Define um novo ângulo de abertura para o radar.
     * 
     * @param anguloAlcance Novo ângulo de abertura em graus
     */
    public void setAnguloAlcance(int anguloAlcance) {
        this.anguloAlcance = anguloAlcance;
    }
}
