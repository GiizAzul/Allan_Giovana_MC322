/**
 * Classe que representa um robô terrestre, especialização de Robo
 * com capacidade de velocidade limitada
 */
public class RoboTerrestre extends Robo {
    private int velocidadeMaxima;  // Velocidade máxima do robô terrestre

    /**
     * Construtor de RoboTerrestre
     * @param nome Nome do robô
     * @param direcao Direção inicial
     * @param posicaoX Posição X inicial
     * @param posicaoY Posição Y inicial
     * @param velocidadeMaxima Velocidade máxima do robô
     */
    public RoboTerrestre(String nome, String direcao, int posicaoX, int posicaoY, int velocidadeMaxima) {
        super(nome, direcao, posicaoX, posicaoY);
        this.velocidadeMaxima = velocidadeMaxima;
    }

    /**
     * Move o robô terrestre respeitando a velocidade máxima
     * @param deltaX Deslocamento X
     * @param deltaY Deslocamento Y
     * @param vel Velocidade desejada
     * @param ambiente Ambiente onde o robô se encontra
     */
    public void mover(int deltaX, int deltaY, int vel, Ambiente ambiente) {
        if (vel <= velocidadeMaxima) {
            super.mover(deltaX, deltaY, ambiente);
        }
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
     * Calcula a distância entre este robô terrestre e outro
     * @param robo Robô terrestre alvo
     * @return Distância euclidiana 2D
     */
    public double distanciaRobo(RoboTerrestre robo) {
        return Math.sqrt(Math.pow(robo.getPosicaoX() - this.getPosicaoX(), 2)
                + Math.pow(robo.getPosicaoY() - this.getPosicaoY(), 2));
    }

    /**
     * Calcula a distância entre este robô terrestre e um robô aéreo
     * @param alvo Robô aéreo alvo
     * @return Distância euclidiana 3D (considerando altitude do robô aéreo)
     */
    public double distanciaRobo(RoboAereo alvo) {
        return Math.sqrt(Math.pow(alvo.getPosicaoX() - this.getPosicaoX(), 2)
                + Math.pow(alvo.getPosicaoY() - this.getPosicaoY(), 2) + Math.pow(alvo.getAltitude() - 0, 2));
    }
}
