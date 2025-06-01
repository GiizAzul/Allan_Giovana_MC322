package interfaces;
import java.util.ArrayList;

import ambiente.Obstaculo;
import excecoes.sensor.SensorException;
import robos.geral.*;

public interface Identificantes {
    ArrayList<Obstaculo> identificarObstaculo() throws SensorException;
    ArrayList<Robo> identificarRobo() throws SensorException;
}
