package excecoes.sensor;

public class SensorException extends Exception {
    public SensorException(String message) {
        super(message);
    }

    public SensorException() {
        super("Erro relacionado ao sensor do robô");
    }
    
}
