public class Ambiente{
    //propriedades
    private int largura; 
    private int altura;

    public Ambiente(int largura, int altura){  //constructor q inicializa tamanho do ambiente
        this.largura=largura;
        this.altura=altura;
    }

    public boolean dentroDosLimites(int x, int y){ //método que verifica se robo está dentro dos limites
        return (x<= this.largura && x>=0) && (y<= this.altura && y>=0); //posição deve ser entre 0 e limites
    }

}