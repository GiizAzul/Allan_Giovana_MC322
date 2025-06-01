package interfaces;

import ambiente.Ambiente;
import excecoes.robos.especificos.AlvoInvalidoException;
import excecoes.robos.especificos.MunicaoInsuficienteException;
import excecoes.sensor.SensorException;

/**
 * Interface que define as capacidades de ataque para robôs que podem realizar ataques.
 * Implementada por robôs que possuem armamentos como TanqueGuerra e DroneAtaque.
 */
public interface Atacante {
    
    /**
     * Executa um ataque contra uma posição específica no ambiente
     * @param alvoX Coordenada X do alvo a ser atacado
     * @param alvoY Coordenada Y do alvo a ser atacado
     * @param alvoZ Coordenada Z (altura) do alvo a ser atacado
     * @param nTiros Número de tiros a serem disparados
     * @param ambiente Referência ao ambiente onde ocorre o ataque
     * @return String descrevendo o resultado do ataque
     * @throws SensorException Se houver problemas com os sensores durante o ataque
     * @throws MunicaoInsuficienteException Se não houver munição suficiente para o ataque
     * @throws AlvoInvalidoException Se o alvo for inválido, como si próprio
     */
    String atirar(int alvoX, int alvoY, int alvoZ, int nTiros, Ambiente ambiente)
    throws SensorException, MunicaoInsuficienteException, AlvoInvalidoException;
    
    /**
     * Recarrega a munição do robô atacante
     * @param nBalas Número de balas/projéteis a serem adicionados ao arsenal
     * @return String confirmando a recarga e quantidade atual de munição
     */
    String recarregar(int nBalas);
}
