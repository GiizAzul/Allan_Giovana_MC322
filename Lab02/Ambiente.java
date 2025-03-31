import java.util.ArrayList;

public class Ambiente{
    //propriedades
    private int tamX; 
    private int tamY;
    private int tamZ;
    
    private ArrayList<Robo> listaRobos;

    public Ambiente(int tamX, int tamY, int tamZ){  //constructor q inicializa tamanho do ambiente        
        this.tamX=tamX;
        this.tamY=tamY;
        this.tamZ=tamZ;
        listaRobos = new ArrayList<Robo>();
    }

    public boolean dentroDosLimites(int x, int y){ //método que verifica se robo está dentro dos limites
        return (x<= this.tamX && x>=0) && (y<= this.tamY && y>=0); //posição deve ser entre 0 e limites
    }

    public boolean dentroDosLimites(int x, int y, int z){ //método que verifica se robo está dentro dos limites
        return (x<= this.tamX && x>=0) && (y<= this.tamY && y>=0) && (z<= this.tamZ && z>=0); //posição deve ser entre 0 e limites
    }

    public void adicionarRobo(Robo robo){
        listaRobos.add(robo);
    }

    public ArrayList<Robo> getListaRobos(){
        return listaRobos;
    }

    public int getTamX() {
        return tamX;
    }

    public int getTamY() {
        return tamY;
    }

    public int getTamZ() {
        return tamZ;
    }

    public Robo criarRobo(int tipo, int subcategoria, Object... atributo) {
        if (tipo == 1){
            if (subcategoria==1) {
                return new TanqueGuerra((String)atributo[0],(String)atributo[1],(Integer)atributo[2],(Integer)atributo[3],(Integer)atributo[4],(Integer)atributo[5],(Integer)atributo[6]);
            } else if (subcategoria==2) {
                return new Correios((String)atributo[0],(String)atributo[1],(Integer)atributo[2],(Integer)atributo[3],(Integer)atributo[4],(Integer)atributo[5],(Float)atributo[6]);
            }
        } else if (tipo == 2) {
            if (subcategoria == 1) {
                return new DroneAtaque((String)atributo[0],(String)atributo[1],(Integer)atributo[2],(Integer)atributo[3],(Integer)atributo[4],(Integer)atributo[5], (Integer)atributo[6], (Integer)atributo[7]);
            } else if (subcategoria == 2) {
                return new DroneVigilancia((String) atributo[0], (String)atributo[1],(Integer)atributo[2],(Integer)atributo[3],(Integer)atributo[4],(Integer)atributo[5], (Float) atributo[6], (Float) atributo[7]);
            }
        }
        return null; 
    }

    public Object identificarObjetoPosicao(int posX, int posY){
        for (Robo robo : this.listaRobos){
            if (robo.getPosicaoX() == posX && robo.getPosicaoY() == posY){
                return robo;
            }
        }
        return null;
    }

    public Object identificarObjetoPosicao(int posX, int posY, int posZ){
        if (posZ == 0) {
            return this.identificarObjetoPosicao(posX, posY);
        }
        for (Robo robo : this.listaRobos){
            if (robo instanceof RoboAereo) {
                RoboAereo roboAir = (RoboAereo) robo;
                if (roboAir.getPosicaoX() == posX && roboAir.getPosicaoY() == posY && roboAir.getAltitude() == posZ){
                    return robo;
                }
            }
        }
        return null;
    }
}