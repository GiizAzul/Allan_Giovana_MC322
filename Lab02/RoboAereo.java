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
    }

    public void descer(int metros) {
        this.altitude -= metros;
    }

}
