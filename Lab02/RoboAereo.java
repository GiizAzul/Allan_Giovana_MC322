public class RoboAereo extends Robo {
    
    private int altitude;
    private int altitudeMaxima;

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

    public void subir(int metros) {
        this.altitude += metros;
        if (this.altitude>this.altitudeMaxima){
            this.altitude=this.altitudeMaxima;
        }
    }

    public void descer(int metros) {
        this.altitude -= metros;
        if (this.altitude < 0){
            this.altitude=0;
        }
    }

    public int getAltitude() {
        return this.altitude;
    }

}
