import java.util.ArrayList;

public class Ambiente{
    //propriedades
    private int largura; 
    private int altura;
    private ArrayList<Robo> listaRobos;

    public Ambiente(int largura, int altura){  //constructor q inicializa tamanho do ambiente
        this.largura=largura;
        this.altura=altura;
        listaRobos = new ArrayList<Robo>();
    }

    public boolean dentroDosLimites(int x, int y){ //método que verifica se robo está dentro dos limites
        return (x<= this.largura && x>=0) && (y<= this.altura && y>=0); //posição deve ser entre 0 e limites
    }

    public void adicionarRobo(Robo robo){
        listaRobos.add(robo);
    }

    public ArrayList<Robo> getListaRobos(){
        return listaRobos;
    }

    public Robo criarRobo(int tipo, int subcategoria, Object... atributo) {
        if (tipo == 1){
            if (subcategoria==1) {
                return new TanqueGuerra((String)atributo[0],(String)atributo[1],(Integer)atributo[2],(Integer)atributo[3],(Integer)atributo[4],(Integer)atributo[5],(Integer)atributo[6]);
            } else if (subcategoria==2) {
                return new Correios((String)atributo[0],(String)atributo[1],(Integer)atributo[2],(Integer)atributo[3],(Integer)atributo[4],(Integer)atributo[5],(Float)atributo[6]);
            }
        }
        
        return null; 

}
    

}