public class Robo {
    //propriedades
    private String nome;
    private String direcao;
    private int posicaoX;
    private int posicaoY;

    public Robo(String nome, String direcao, int posicaoX, int posicaoY) { //constructor q inicializa nome e posição do robo
        this.nome = nome;
        this.direcao = direcao;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
    }

    public void mover(int deltaX, int deltaY) { //método para mover o robo
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    public void exibirPosicao() { //método para imprimir posição
        String text = "Nome: %s X:%d Y:%d";
        String formated = String.format(text, this.nome, this.posicaoX, this.posicaoY);
        System.out.println(formated);
        // idealmente, só métodos da classe Main imprimem no terminal
    }

    public int getPosicaoX() { //método para retornar posição x
        return this.posicaoX;
    }

    public int getPosicaoY() { //método para retornar posição x
        return this.posicaoY;
    }

}