public class Robo {

    private String nome;
    private int posicaoX;
    private int posicaoY;

    public Robo(String nome, int posicaoX, int posicaoY) {
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
    }

    public void mover(int deltaX, int deltaY) {
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    public void exibirPosicao() {
        String text = "Nome: %s X:%d Y:%d";
        String formated = String.format(text, this.nome, this.posicaoX, this.posicaoY);
        System.out.println(formated);
    }

    public int getPosicaoX() {
        return this.posicaoX;
    }

    public int getPosicaoY() {
        return this.posicaoY;
    }

}