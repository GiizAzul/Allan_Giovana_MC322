package Sensores;

public class Barometro extends Sensor<Double> {
    private double pressaoAtmosferica; // em hPa

    /**
     * Construtor padrão que inicializa o sensor com a pressão atmosférica padrão.
     */
    public Barometro() {
        super();
        this.pressaoAtmosferica = 1013.25; // Pressão atmosférica padrão ao nível do mar
    }

    /**
     * Construtor que inicializa o sensor com uma altitude específica.
     * 
     * @param altitude A altitude em metros (pode ser um valor inteiro ou decimal)
     */

    public Barometro(double altitude) {
        super();
        this.pressaoAtmosferica = this.acionar(altitude);
    }

    /**
     * Obtém a pressão atmosférica em hPa.
     * 
     * @param <E> O tipo do parâmetro que o sensor pode receber
     * @param altitude A altitude em metros (pode ser um valor inteiro ou decimal)
     * @return Double com a pressão atmosférica em hPa para a altitude informada
     */
    @Override
    public <E> Double acionar(E altitude) {
        // Leitura da pressão atmosférica em função da altitude
        double alt = (double) altitude;
        this.pressaoAtmosferica = 1013.25 * Math.pow(1 - (0.0065 * alt) / 288.15, 5.25588);
        return this.pressaoAtmosferica;
    }
    
}
