package interfaces;

import excecoes.sensor.SensorException;
import excecoes.logger.LoggerException;

/**
 * Interface que define as características básicas de uma entidade no ambiente.
 * Implementada por robôs e obstáculos para permitir interação e posicionamento.
 */
public interface Entidade {
    
    /**
     * Retorna a coordenada X da entidade através dos sensores
     * @return Coordenada X da posição atual
     * @throws SensorException Se houver problemas com os sensores ao obter a posição
     */
    int getX() throws SensorException, LoggerException;
    
    /**
     * Retorna a coordenada Y da entidade através dos sensores
     * @return Coordenada Y da posição atual
     * @throws SensorException Se houver problemas com os sensores ao obter a posição
     */
    int getY() throws SensorException, LoggerException;
    
    /**
     * Retorna a coordenada Z (altura) da entidade através dos sensores
     * @return Coordenada Z da posição atual
     * @throws SensorException Se houver problemas com os sensores ao obter a posição
     */
    int getZ() throws SensorException, LoggerException;
    
    /**
     * Retorna a coordenada X da entidade diretamente, sem usar sensores
     * Método interno usado pelo ambiente para acesso direto à posição
     * @return Coordenada X real da entidade
     */
    int getXInterno();
    
    /**
     * Retorna a coordenada Y da entidade diretamente, sem usar sensores
     * Método interno usado pelo ambiente para acesso direto à posição
     * @return Coordenada Y real da entidade
     */
    int getYInterno();
    
    /**
     * Retorna a coordenada Z da entidade diretamente, sem usar sensores
     * Método interno usado pelo ambiente para acesso direto à posição
     * @return Coordenada Z real da entidade
     */
    int getZInterno();
    
    /**
     * Retorna o tipo da entidade (ROBO, OBSTACULO, etc.)
     * @return TipoEntidade que classifica esta entidade
     */
    TipoEntidade getTipo();
    
    /**
     * Retorna uma descrição textual detalhada da entidade
     * @return String contendo informações descritivas da entidade
     * @throws SensorException Se houver problemas com os sensores ao obter informações
     */
    String getDescricao() throws SensorException, LoggerException;
    
    /**
     * Retorna a representação visual da entidade para exibição no mapa
     * @return String contendo o símbolo que representa a entidade
     */
    String getRepresentacao();
}
