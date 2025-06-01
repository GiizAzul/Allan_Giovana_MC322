package interfaces;
import excecoes.*;
import excecoes.robos.gerais.RoboDesligadoException;

public interface Sensoreavel {
    void acionarSensores() throws RoboDesligadoException;
}