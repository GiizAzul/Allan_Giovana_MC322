import java.util.ArrayList;

public class Robo {
    //propriedades
    protected String nome;
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
        posicaoX += deltaX;
        posicaoY += deltaY;
    }

    public String exibirPosicao() { //método para imprimir posição
        return this.nome + " está na posição X:" + this.posicaoX + " Y:" + this.posicaoY;
    }

    public int getPosicaoX() { //método para retornar posição x
        return posicaoX;
    }

    public int getPosicaoY() { //método para retornar posição x
        return posicaoY;
    }

    public String getDirecao() {
        return direcao;
    }

    public void setDirecao(String direcao) {
        this.direcao = direcao;
    }

    public Object identificarObstaculos(Ambiente ambiente, String direcao){
        ArrayList<Robo> listaRobo=ambiente.getListaRobos();
        Object obstaculo = null;

        int posY = ambiente.getTamY();
        int posX = ambiente.getTamX();

        for (Robo robo : listaRobo) {
            if (direcao.equals("Norte")){
                if (robo.getPosicaoX()==this.posicaoX && robo.getPosicaoY()>this.posicaoY && robo.getPosicaoY()<=posY){
                    posY=robo.getPosicaoY();
                    obstaculo=robo;
                }
            }
            if (direcao.equals("Sul")){
                if (robo.getPosicaoX()==this.posicaoX && robo.getPosicaoY()<this.posicaoY && robo.getPosicaoY()>=posY){
                    posY=robo.getPosicaoY();
                    obstaculo=robo;
                }
            }
            if (direcao.equals("Leste")){
                if (robo.getPosicaoY()==this.posicaoY && robo.getPosicaoX()>this.posicaoX && robo.getPosicaoX()<=posX){
                    posX=robo.getPosicaoY();
                    obstaculo=robo;
                }
            }
            if (direcao.equals("Oeste")){
                if (robo.getPosicaoY()==this.posicaoY && robo.getPosicaoX()<this.posicaoX && robo.getPosicaoX()>=posX){
                    posX=robo.getPosicaoY();
                    obstaculo=robo;
                }
            }
            
        }
        return obstaculo;
    }

}