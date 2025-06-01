package interfaces;
import excecoes.robos.gerais.RoboDesligadoException;

/**
 * Interface que define a capacidade de acionar sensores para robôs.
 * Implementada por robôs que possuem sistemas de sensoriamento que precisam ser ativados.
 */
public interface Sensoreavel {
    
    /**
     * Aciona os sensores do robô para coleta de dados do ambiente
     * @throws RoboDesligadoException Se o robô estiver desligado e não puder acionar os sensores
     */
    void acionarSensores() throws RoboDesligadoException;
}