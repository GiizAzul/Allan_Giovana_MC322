// Crie um novo pacote: robos.subsistemas
package robos.subsistemas.movimento;

import ambiente.Ambiente;
import excecoes.ambiente.ForaDosLimitesException;
import excecoes.robos.gerais.ColisaoException;
import excecoes.robos.gerais.MovimentoInvalidoException;
import excecoes.robos.gerais.RoboDestruidoPorBuracoException;
import excecoes.robos.gerais.VelocidadeMaximaException;
import excecoes.sensor.SensorException;
import robos.aereos.RoboAereo;
import robos.geral.Robo;
import robos.terrestres.RoboTerrestre;

/**
 * Interface que define o contrato para os subsistemas de controle de movimento dos robôs.
 * Abstrai a lógica de como diferentes tipos de robôs se movem no ambiente.
 */
public interface ControleMovimento {

    /**
     * Move um robô terrestre no plano XY.
     * @param robo O robô terrestre a ser movido.
     * @param deltaX Deslocamento em X.
     * @param deltaY Deslocamento em Y.
     * @param velocidade A velocidade do movimento.
     * @param ambiente O ambiente da simulação.
     */
    void mover(RoboTerrestre robo, int deltaX, int deltaY, int velocidade, Ambiente ambiente)
            throws VelocidadeMaximaException, SensorException, ColisaoException, RoboDestruidoPorBuracoException, MovimentoInvalidoException;

    /**
     * Move um robô aéreo para uma nova posição 3D.
     * @param robo O robô aéreo a ser movido.
     * @param novoX Nova coordenada X.
     * @param novoY Nova coordenada Y.
     * @param novaZ Nova coordenada Z (altitude).
     * @param ambiente O ambiente da simulação.
     */
    void mover(RoboAereo robo, int novoX, int novoY, int novaZ, Ambiente ambiente)
            throws SensorException, ColisaoException, MovimentoInvalidoException, RoboDestruidoPorBuracoException;

    void mover(Robo robo, int deltaX, int deltaY, Ambiente ambiente) throws ForaDosLimitesException, SensorException, RoboDestruidoPorBuracoException, ColisaoException;

}
