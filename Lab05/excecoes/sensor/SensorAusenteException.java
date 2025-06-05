package excecoes.sensor;

public class SensorAusenteException extends SensorException {
    public SensorAusenteException(String message) {
        super(message);
    }

    public SensorAusenteException() {
        super("Sensor ausente no robô");
    }
    
}
