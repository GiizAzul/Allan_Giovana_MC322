package robos.subsistemas;

import java.util.ArrayList;

import excecoes.sensor.SensorException;
import interfaces.Entidade;

public interface GerenciadorSensores {
    ArrayList<Entidade> identificarObjetos() throws SensorException;
    double getLeituraSensor(String sensor) throws SensorException;
    void calibrarSensores();
}
