public class RoboAereo extends Robo {
    
    private int altitude;
    private int altitudeMaxima;

    public RoboAereo(String nome, String direcao, int posicaoX, int posicaoY, int altitude, int altitudeMaxima) {
        super(nome, direcao, posicaoX, posicaoY);

        this.altitude = altitude;
        this.altitudeMaxima = altitudeMaxima;
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

}
