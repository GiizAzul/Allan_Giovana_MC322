package Sensores;

public class Barometro extends Sensor<Double> {
    private double pressaoAtmosferica; // em hPa

    public Barometro() {
        super();
        this.pressaoAtmosferica = 1013.25; // Pressão atmosférica padrão ao nível do mar
    }


    @Override
    public Double acionar() {
        // Simulação de leitura de pressão atmosférica
        this.pressaoAtmosferica += (Math.random() - 0.5) * 10; // Variação aleatória
        return this.pressaoAtmosferica;
    }
    
}
