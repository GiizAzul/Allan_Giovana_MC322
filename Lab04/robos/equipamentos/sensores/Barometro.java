package robos.equipamentos.sensores;
import excecoes.sensor.*;
import robos.geral.Robo;

public class Barometro extends Sensor<Double> {
    private double pressaoAtmosferica; // em hPa
    private Robo robo;
    
    /**
     * Construtor para a classe Barometro.
     * 
     * @param robo O robô ao qual este barômetro será associado
     * 
     * Inicializa o barômetro com a pressão atmosférica padrão ao nível do mar (1013.25 hPa).
     * Esta pressão pode variar dependendo da altitude e condições climáticas.
     */
    public Barometro(Robo robo) {
        super();
        this.robo = robo;
        this.pressaoAtmosferica = 1013.25; // Pressão atmosférica padrão ao nível do mar
    }

    /**
     * Construtor default para a classe Barômetro.
     * Inicializa o barômetro com a pressão atmosférica padrão ao nível do mar (1013.25 hPa).
     */
    public Barometro() {
        super();
        this.pressaoAtmosferica = 1013.25; // Pressão atmosférica padrão ao nível do mar
    }

    /**
     * Obtém a pressão atmosférica em hPa.
     * 
     * @param <E> O tipo do parâmetro que o sensor pode receber
     * @param altitude A altitude em metros (pode ser um valor inteiro ou decimal)
     * @return Double com a pressão atmosférica em hPa para a altitude informada
     */
    @Override
    public Double acionar() throws SensorException {
        // Leitura da pressão atmosférica em função da altitude
        if (robo == null) {
           throw new SensorAusenteException("Robo não possui um barômetro.");
        }
        if (this.isAtivo() == false) {
           throw new SensorInativoException("Barômetro está inativo.");
        }   

        double alt = robo.getZInterno();
        this.pressaoAtmosferica = 1013.25 * Math.pow(1 - (0.0065 * alt) / 288.15, 5.25588);
        return this.pressaoAtmosferica;
    }
    
}
