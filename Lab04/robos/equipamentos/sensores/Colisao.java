package robos.equipamentos.sensores;
import java.util.ArrayList;
import robos.terrestres.RoboTerrestre;
import robos.geral.Robo;
import ambiente.Ambiente;
import ambiente.Obstaculo;
import excecoes.sensor.*;
import interfaces.*;

/**
 * Classe que implementa um sensor de colisão para robôs terrestres.
 * Este sensor detecta colisões do robô com outros objetos no ambiente,
 * como outros robôs ou obstáculos, e mantém referência ao último objeto colidido.
 * Estende a classe Sensor com tipo parametrizado Integer para retornar
 * códigos numéricos que indicam o tipo de colisão detectada.
 */
public class Colisao extends Sensor<Integer> {
    private RoboTerrestre robo;              // Robô terrestre ao qual o sensor está acoplado
    private Robo ultimoRoboColidido;         // Referência ao último robô com o qual houve colisão
    private Obstaculo ultimoObstaculoColidido; // Referência ao último obstáculo com o qual houve colisão
    private Ambiente ambiente;               // Referência ao ambiente onde o robô opera

    /**
     * Construtor do sensor de colisão.
     * 
     * @param robo Robô terrestre ao qual o sensor será acoplado
     * @param ambiente Ambiente onde o sensor operará e detectará colisões
     */
    public Colisao(RoboTerrestre robo, Ambiente ambiente) {
        super();
        this.robo = robo;
        this.ambiente = ambiente;
    }

    /**
     * Aciona o sensor para verificar colisões na posição atual do robô.
     * Verifica se há outros robôs ou obstáculos na mesma posição.
     * 
     * @return Código que indica o tipo de colisão:
     *         0 - Nenhuma colisão detectada
     *         1 - Colisão com outro robô
     *         2 - Colisão com um obstáculo
     */
    public Integer acionar() throws SensorException {
        // Faz as verificações necessárias antes de acionar o sensor
        if (robo == null) {
           throw new SensorAusenteException("Robo não possui um sensor de colisão.");
        } 

        if (ambiente == null) {
            throw new SensorAusenteException("Sensor de colisão não está associado à um ambiente.");
        }
        
        if (this.isAtivo() == false) {
           throw new SensorInativoException("Barômetro está inativo.");
        } 

        int posX = this.robo.getXInterno();
        int posY = this.robo.getYInterno();
        
        // Procura colisões com outros robôs

        ArrayList<Entidade> listaEntidade = ambiente.getEntidades();
        for (Entidade entidade : listaEntidade){
            if (entidade.getTipo() == TipoEntidade.ROBO){
                Robo colRobo = (Robo) entidade;
                if (colRobo != this.robo && colRobo.getXInterno() == posX && colRobo.getYInterno() == posY) {
                    this.ultimoRoboColidido = colRobo;
                    return 1; // Colisão com outro robô
                }
            } else if (entidade.getTipo() == TipoEntidade.OBSTACULO){
                Obstaculo obstaculo = (Obstaculo) entidade;
                if (obstaculo.getX1() <= this.robo.getXInterno() && obstaculo.getX2() >= posX && 
                    obstaculo.getY1() <= posY && obstaculo.getY2() >= posY) {
                    this.ultimoObstaculoColidido = obstaculo;
                    return 2; // Colisão com obstáculo
                }
            }
        }
        return 0; // Sem colisão
    }

    /**
     * Obtém a referência ao último robô com o qual houve colisão.
     * 
     * @return Último robô colidido ou null se não houver colisão com robô
     */
    public Robo getUltimoRoboColidido() {
        return ultimoRoboColidido;
    }
    
    /**
     * Obtém a referência ao último obstáculo com o qual houve colisão.
     * 
     * @return Último obstáculo colidido ou null se não houver colisão com obstáculo
     */
    public Obstaculo getUltimoObstaculoColidido() {
        return ultimoObstaculoColidido;
    }
}
