package robos.equipamentos.sensores;
import excecoes.sensor.*;
import robos.geral.Robo;

/**
 * Classe que implementa um sensor de GPS para robôs.
 * O GPS é responsável por fornecer as coordenadas de posição do robô no ambiente.
 * Estende a classe Sensor com tipo parametrizado int[] para retornar
 * as coordenadas [x, y, z] da posição.
 */
public class GPS extends Sensor<int[]> {
    private Robo robo; // Referência ao robô ao qual o GPS está acoplado

    /**
     * Construtor padrão do GPS.
     * Inicializa um GPS sem vínculo a um robô específico.
     */
    public GPS() {
        super();
    }

    /**
     * Construtor que inicializa o GPS vinculado a um robô específico.
     * 
     * @param robo Robô ao qual o GPS será vinculado
     */
    public GPS(Robo robo) {
        super();
        this.robo = robo;
    }

    /**
     * Aciona o sensor de GPS e obtém as coordenadas atuais do robô.
     * Retorna um array de 3 posições [x, y, z], onde z é sempre 0 para robôs terrestres.
     * Para robôs aéreos, a altitude (z) deve ser obtida pelo sensor de barômetro.
     * 
     * @return Array de inteiros com as coordenadas [x, y, z] do robô, ou null se o sensor estiver inativo ou não vinculado
     */
    @Override
    public int[] acionar() throws SensorException {
        // Verifica as condições necessárias antes de acionar o sensor
        if (robo == null) {
           throw new SensorAusenteException("Robo não possui um sensor de GPS.");
        } 
        
        if (this.isAtivo() == false) {
           throw new SensorInativoException("GPS está inativo.");
        } 

        // Acessa diretamente as posições internas do robô
        return new int[] {
            this.robo.getXInterno(), 
            this.robo.getYInterno(),
            0 // Z é por padrão 0 para Robos
        };
    }
    
    /**
     * Método específico para obter apenas a coordenada X do robô.
     * Útil quando apenas a posição horizontal no eixo X é necessária.
     * 
     * @return Coordenada X do robô, ou -1 se o sensor estiver inativo ou não vinculado
     */
    public int obterPosicaoX() throws SensorException{
        int[] posicoes = this.acionar();
        return posicoes[0];
    }
    
    /**
     * Método específico para obter apenas a coordenada Y do robô.
     * Útil quando apenas a posição horizontal no eixo Y é necessária.
     * 
     * @return Coordenada Y do robô, ou -1 se o sensor estiver inativo ou não vinculado
     */
    public int obterPosicaoY() throws SensorException{
        int[] posicoes = this.acionar();
        return posicoes[1];
    }
    
    /**
     * Vincula o GPS a um robô específico.
     * Este método permite que um GPS criado sem vínculo a um robô
     * seja posteriormente associado a um robô específico.
     * 
     * @param robo Robô ao qual o GPS será vinculado
     */
    public void vincularRobo(Robo robo) {
        this.robo = robo;
    }
}