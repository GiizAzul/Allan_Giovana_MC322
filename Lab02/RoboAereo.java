import java.util.ArrayList;
import java.util.Comparator;

public class RoboAereo extends Robo {
    
    private int altitude = -1;
    private int altitudeMaxima = -1;

    public RoboAereo(String n, String d, int x, int y, int h) {
        /*
         * n -> Nome do Robô
         * d -> Direção
         * x -> Posição X
         * y -> Posição Y
         * h -> Altura Inicial
         */
        super(n, d, x, y);
        this.altitude = h;
    }

    public RoboAereo(String n, String d, int x, int y, int h, int hmax) {
        /*
         * n -> Nome do Robô
         * d -> Direção
         * x -> Posição X
         * y -> Posição Y
         * h -> Altura atual
         * hmax -> Altura máxima
         */
        super(n, d, x, y);

        this.altitude = h;
        this.altitudeMaxima = hmax;
    }

    private boolean validacao() {
        if (this.altitudeMaxima == -1) {
            return false;
        }
        return true;
    }

    public void subir(int metros) {
        if (!validacao()) { 
            System.out.println("Atributos obrigatórios não foram setados");
            return;
        }

        this.altitude += metros;
        if (this.altitude>this.altitudeMaxima){
            System.out.println("Altura máxima foi atingida!");
            this.altitude=this.altitudeMaxima;
        }
    }

    public void descer(int metros) {
        if (!validacao()) { 
            System.out.println("Atributos obrigatórios não foram setados");
            return;
        }

        this.altitude -= metros;
        if (this.altitude < 0){
            this.altitude = 0;
        }
    }

    public int getAltitude() {
        return this.altitude;
    }

    public int getAltitudeMaxima() {
        return this.altitudeMaxima;
    }

    public void setAltitudeMaxima(int hMax) {
        this.altitudeMaxima = hMax;
    }

    public double distanciaRobo(RoboTerrestre robo) {
        return Math.sqrt(Math.pow(robo.getPosicaoX() - this.getPosicaoX(), 2) + Math.pow(robo.getPosicaoY() - this.getPosicaoY(), 2) + Math.pow(0 - this.getAltitude(), 2));
    }

    public double distanciaRobo(RoboAereo alvo) {
        return Math.sqrt(Math.pow(alvo.getPosicaoX() - this.getPosicaoX(), 2) + Math.pow(alvo.getPosicaoY() - this.getPosicaoY(), 2) + Math.pow(alvo.getAltitude() - this.getAltitude(), 2));
    }

    public void mover(int X, int Y, int Z) {
        super.mover(X - this.getPosicaoX(), Y - this.getPosicaoY());
        int h = this.getAltitude();
        if (Z > h) {
            this.subir(Z - h);
        } else {
            this.descer(h - Z);
        }
    }

    @Override
    public ArrayList<Robo> identificarObstaculo(Ambiente ambiente, String direcao){

        ArrayList<Robo> listaRobo = ambiente.getListaRobos();
        ArrayList<Robo> obstaculos = new ArrayList<>();
        ArrayList<RoboAereo> robosAereos = new ArrayList<>();

        for (Robo robo: listaRobo) {
            if (robo instanceof RoboAereo && robo.getVisivel()) {
                RoboAereo roboAir = (RoboAereo) robo;
                if (roboAir.getAltitude() == this.getAltitude()) {
                    robosAereos.add(roboAir);
                }
            }
        }
        

        if (direcao.equals("Norte")){
            for (RoboAereo robo : robosAereos) {
                if (robo.getPosicaoX()==this.getPosicaoX() && robo.getPosicaoY()>this.getPosicaoY()){
                    obstaculos.add(robo);
                }
            }
            obstaculos.sort(Comparator.comparingInt(Robo::getPosicaoY));
        } else if (direcao.equals("Sul")){
            for (RoboAereo robo : robosAereos) {
                if (robo.getPosicaoX()==this.getPosicaoX() && robo.getPosicaoY()<this.getPosicaoY()){
                    obstaculos.add(robo);
                }
            obstaculos.sort(Comparator.comparingInt(Robo::getPosicaoY).reversed());
            }
        } else if (direcao.equals("Leste")){
            for (RoboAereo robo : robosAereos) {
                if (robo.getPosicaoY()==this.getPosicaoY() && robo.getPosicaoX()>this.getPosicaoX()){
                    obstaculos.add(robo);
                }
            obstaculos.sort(Comparator.comparingInt(Robo::getPosicaoX));
            }
        } else if (direcao.equals("Oeste")) {
            for (RoboAereo robo : robosAereos) {
                if (robo.getPosicaoY()==this.getPosicaoY() && robo.getPosicaoX()<this.getPosicaoX()){
                    obstaculos.add(robo);
                }
            obstaculos.sort(Comparator.comparingInt(Robo::getPosicaoX).reversed());
            }
        }
        return obstaculos;
    }


}
