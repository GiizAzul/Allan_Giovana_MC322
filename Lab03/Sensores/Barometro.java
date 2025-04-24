package Sensores;

public class Barometro extends Sensor<Double> {
    private double pressaoAtmosferica; // em hPa

    public Barometro() {
        super();
        this.pressaoAtmosferica = 1013.25; // Pressão atmosférica padrão ao nível do mar
    }

    public Barometro(double altitude) {
        super();
        this.pressaoAtmosferica = this.acionar(altitude); // Pressão atmosférica padrão ao nível do mar
    }

    /**
     * Obtém a pressão atmosférica em hPa.
     * 
     * @param <E> O tipo do parâmetro que o sensor pode receber
     * @param altitude A altitude em metros (pode ser um valor inteiro ou decimal)
     * @return true se o sensor estiver ativo, false caso contrário
     */
    @Override
    public <E> Double acionar(E altitude) {
        // Leitura da pressão atmosférica em função da altitude
        double alt = (double) altitude;
        this.pressaoAtmosferica = 1013.25 * Math.pow(1 - (0.0065 * alt) / 288.15, 5.25588);
        return this.pressaoAtmosferica;
    }
    
}
