// Crie no pacote: robos.subsistemas
package robos.subsistemas;

import java.util.ArrayList;
import java.util.List;

import excecoes.logger.LoggerException;
import excecoes.sensor.*;
import interfaces.Entidade;
import robos.equipamentos.sensores.*;
import robos.geral.Robo;

/**
 * Subsistema que gerencia todos os sensores de um robô.
 * Encapsula a lógica de adicionar, acessar e acionar sensores,
 * abstraindo os detalhes de implementação das classes de robôs.
 */
public class GerenciadorSensores {
    private final Robo roboPai; // O robô que possui este gerenciador
    private final List<Sensor<?>> listaSensores;

    // Referências diretas para sensores comuns para acesso rápido
    private GPS gps;
    private Radar radar;
    private Barometro barometro;
    private Colisao sensorColisao;

    public GerenciadorSensores(Robo roboPai) {
        this.roboPai = roboPai;
        this.listaSensores = new ArrayList<>();
    }

    /**
     * Adiciona um sensor ao gerenciador.
     * @param sensor O sensor a ser adicionado.
     */
    public void adicionarSensor(Sensor<?> sensor) {
        this.listaSensores.add(sensor);
        // Armazena referências específicas para evitar type casting constante
        if (sensor instanceof GPS) this.gps = (GPS) sensor;
        if (sensor instanceof Radar) this.radar = (Radar) sensor;
        if (sensor instanceof Barometro) this.barometro = (Barometro) sensor;
        if (sensor instanceof Colisao) this.sensorColisao = (Colisao) sensor;
    }

    /**
     * Obtém a posição [X, Y, Z] do robô através do GPS.
     * @return um array de int com as coordenadas.
     * @throws SensorException se o GPS estiver ausente ou inativo.
     * @throws LoggerException se houver alguma falha no sistema de Logging.
     */
    public int[] getPosicaoGPS() throws SensorException, LoggerException, LoggerException {
        if (this.gps == null) throw new SensorAusenteException("Robô " + roboPai.getNome() + " não possui GPS.");
        if (!this.gps.isAtivo()) throw new SensorInativoException("GPS do robô " + roboPai.getNome() + " está inativo.");
        return this.gps.acionar();
    }

    /**
     * Aciona o radar para identificar entidades próximas.
     * @return Uma lista de entidades detectadas.
     * @throws SensorException, LoggerException se o Radar estiver ausente ou inativo.
     */
    public ArrayList<Entidade> identificarComRadar() throws SensorException, LoggerException, LoggerException {
        if (this.radar == null) throw new SensorAusenteException("Robô " + roboPai.getNome() + " não possui Radar.");
        if (!this.radar.isAtivo()) throw new SensorInativoException("Radar do robô " + roboPai.getNome() + " está inativo.");
        return this.radar.acionar();
    }
    
    /**
     * Obtém a leitura da pressão atmosférica do barômetro.
     * @return a pressão em hPa.
     * @throws SensorException, LoggerException se o Barômetro estiver ausente ou inativo.
     */
    public double getPressaoAtmosferica() throws SensorException, LoggerException, LoggerException {
        if (this.barometro == null) throw new SensorAusenteException("Robô " + roboPai.getNome() + " não possui Barômetro.");
        if (!this.barometro.isAtivo()) throw new SensorInativoException("Barômetro do robô " + roboPai.getNome() + " está inativo.");
        return this.barometro.acionar();
    }

    /**
     * Aciona o sensor de colisão para robôs terrestres.
     * @return um inteiro indicando o tipo de colisão (0: nenhuma, 1: robô, 2: obstáculo).
     * @throws SensorException, LoggerException se o sensor de Colisão estiver ausente ou inativo.
     */
    public int verificarColisao() throws SensorException, LoggerException, LoggerException {
        if (this.sensorColisao == null) throw new SensorAusenteException("Robô " + roboPai.getNome() + " não possui Sensor de Colisão.");
        if (!this.sensorColisao.isAtivo()) throw new SensorInativoException("Sensor de Colisão do robô " + roboPai.getNome() + " está inativo.");
        return this.sensorColisao.acionar();
    }

    // Getters para os sensores específicos, permitindo acesso se necessário
    public GPS getGPS() { return this.gps; }
    public Radar getRadar() { return this.radar; }
    public Barometro getBarometro() { return this.barometro; }
    public Colisao getSensorColisao() { return this.sensorColisao; }
}