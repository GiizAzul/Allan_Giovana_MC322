package interfaces;
import java.util.ArrayList;

import ambiente.Obstaculo;
import excecoes.sensor.SensorException;
import excecoes.logger.LoggerException;
import robos.geral.*;

/**
 * Interface que define as capacidades de identificação para robôs com sensores.
 * Implementada por robôs que podem detectar obstáculos e outros robôs no ambiente.
 */
public interface Identificantes {
    
    /**
     * Identifica obstáculos presentes no alcance dos sensores do robô
     * @return ArrayList contendo todos os obstáculos detectados pelos sensores
     * @throws SensorException Se houver problemas com os sensores durante a identificação
     */
    ArrayList<Obstaculo> identificarObstaculo() throws SensorException, LoggerException;
    
    /**
     * Identifica outros robôs presentes no alcance dos sensores do robô
     * @return ArrayList contendo todos os robôs detectados pelos sensores
     * @throws SensorException Se houver problemas com os sensores durante a identificação
     */
    ArrayList<Robo> identificarRobo() throws SensorException, LoggerException;
}
