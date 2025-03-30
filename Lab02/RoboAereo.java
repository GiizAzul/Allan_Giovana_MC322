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
            this.altitude=0;
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

}
