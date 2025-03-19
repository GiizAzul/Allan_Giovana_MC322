public class RoboTerrestre extends Robo {
    private int velocidadeMaxima;

    public RoboTerrestre(String nome, String direcao, int posicaoX, int posicaoY, int velocidadeMaxima) {
        super(nome, direcao, posicaoX, posicaoY);
        this.velocidadeMaxima=velocidadeMaxima;
    }

    public void mover(int deltaX, int deltaY, int vel){
            if (vel <= velocidadeMaxima){
            super.mover(deltaX, deltaY);
        }
    }
}
