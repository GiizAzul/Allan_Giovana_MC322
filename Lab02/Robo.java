import java.util.ArrayList;
import java.util.Comparator;


public class Robo {
    // Propriedades
    private String nome;
    private String direcao;
    private int posicaoX;
    private int posicaoY;
    private int integridade;
    private boolean operando;

    // Acessível somente para subclasses
    protected boolean visivel = true;

    public Robo(String nome, String direcao, int posicaoX, int posicaoY) { //constructor q inicializa nome e posição do robo
        this.nome = nome;
        this.direcao = direcao;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.operando=true;
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

    public boolean getOperando(){
        return operando;
    }

    public void setOperando(boolean operando){
        this.operando=operando;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean getVisivel() {
        return this.visivel;
    }

    public int getIntegridade(){
        return integridade;
    }

    public void setIntegridade(int integridade){
        this.integridade=integridade;
    }

    public ArrayList<Robo> identificarObstaculo(Ambiente ambiente, String direcao){

        ArrayList<Robo> listaRobo=ambiente.getListaRobos();
        ArrayList<Robo> obstaculos = new ArrayList<>();

        if (direcao.equals("Norte")){
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoX()==this.posicaoX && robo.getPosicaoY()>this.posicaoY){
                    obstaculos.add(robo);
                }
            }
            obstaculos.sort(Comparator.comparingInt(o -> o.posicaoY));
        } else if (direcao.equals("Sul")){
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoX()==this.posicaoX && robo.getPosicaoY()<this.posicaoY){
                    obstaculos.add(robo);
                }
            obstaculos.sort(Comparator.comparingInt((Robo o) -> o.posicaoY).reversed());
            }
        } else if (direcao.equals("Leste")){
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoY()==this.posicaoY && robo.getPosicaoX()>this.posicaoX){
                    obstaculos.add(robo);
                }
            obstaculos.sort(Comparator.comparingInt(o -> o.posicaoX));
        }
        } else if (direcao.equals("Oeste")){
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoY()==this.posicaoY && robo.getPosicaoX()<this.posicaoX){
                    obstaculos.add(robo);
                }
            obstaculos.sort(Comparator.comparingInt((Robo o) -> o.posicaoX).reversed());
            }
        } 
        return obstaculos;
    }

    public Object identificarObstaculoPosicao(Ambiente ambiente, int posY, int posX){
        ArrayList<Robo> listaRobo = ambiente.getListaRobos();
        for (Robo robo : listaRobo){
            if (robo.getPosicaoX()==posX && robo.getPosicaoY()==posY){
                return robo;
            }
        }
        return null;
    }

    public String defender(int dano){
        integridade-=dano;

        if (integridade<=0){
            setOperando(false);
            return "O robô " + getNome() + " está inoperante devido ao dano tomado";
        }

        return "O robô " + getNome() + " ainda está operando";
    }

    public double distanciaRobo(Robo robo) {
        return Math.sqrt(Math.pow(robo.getPosicaoX() - this.getPosicaoX(), 2) + Math.pow(robo.getPosicaoY() - this.getPosicaoY(), 2));
    }
}