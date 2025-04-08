public class RoboTerrestre extends Robo {
    private int velocidadeMaxima;

    public RoboTerrestre(String nome, String direcao, int posicaoX, int posicaoY, int velocidadeMaxima) {
        super(nome, direcao, posicaoX, posicaoY);
        this.velocidadeMaxima = velocidadeMaxima;
    }

    public void mover(int deltaX, int deltaY, int vel) {
        if (vel <= velocidadeMaxima) {
            super.mover(deltaX, deltaY);
        }
    }

    public int getVelocidadeMaxima() {
        return this.velocidadeMaxima;
    }

    public void setVelocidadeMaxima(int vmax) {
        this.velocidadeMaxima = vmax;
    }

    public double distanciaRobo(RoboTerrestre robo) {
        return Math.sqrt(Math.pow(robo.getPosicaoX() - this.getPosicaoX(), 2)
                + Math.pow(robo.getPosicaoY() - this.getPosicaoY(), 2));
    }

    public double distanciaRobo(RoboAereo alvo) {
        return Math.sqrt(Math.pow(alvo.getPosicaoX() - this.getPosicaoX(), 2)
                + Math.pow(alvo.getPosicaoY() - this.getPosicaoY(), 2) + Math.pow(alvo.getAltitude() - 0, 2));
    }
}
