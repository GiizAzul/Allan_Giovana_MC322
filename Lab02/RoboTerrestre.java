public class RoboTerrestre extends Robo {
    private int velocidadeMaxima;

    public RoboTerrestre(String nome, String direcao, int posicaoX, int posicaoY, int velocidadeMaxima) {
        super(nome, direcao, posicaoX, posicaoY);
        this.velocidadeMaxima=velocidadeMaxima;
    }

    public void mover(int deltaX, int deltaY, int vel){
        System.out.println("Aqui");
        if (vel <= this.velocidadeMaxima){
            this.posicaoX += deltaX;
            this.posicaoY += deltaY;
        }
    }
}
