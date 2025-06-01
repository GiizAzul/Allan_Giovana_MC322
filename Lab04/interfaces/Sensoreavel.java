package interfaces;
import excecoes.robos.gerais.RoboDesligadoException;

public interface Sensoreavel {
    void acionarSensores() throws RoboDesligadoException;
}