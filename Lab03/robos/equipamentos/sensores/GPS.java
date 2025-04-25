package robos.equipamentos.sensores;
import robos.Robo;

public class GPS extends Sensor<int[]> {
    private Robo robo;

    public GPS() {
        super();
    }

    public GPS(Robo robo) {
        super();
        this.robo = robo;
    }

    @Override
    public int[] acionar() {
        if (!isAtivo() || this.robo == null) {
            return null;
        }
        
        // Acessa diretamente as posições internas do robô
        return new int[] {
            this.robo.getPosicaoXInterna(), 
            this.robo.getPosicaoYInterna(),
            0 // Z é por padrão 0 para Robos
        };
    }
    
    // Método específico para obter a coordenada X
    public int obterPosicaoX() {
        if (!isAtivo() || this.robo == null) {
            return -1; // Código de erro quando o sensor está inativo
        }
        return this.robo.getPosicaoXInterna();
    }
    
    // Método específico para obter a coordenada Y
    public int obterPosicaoY() {
        if (!isAtivo() || this.robo == null) {
            return -1; // Código de erro quando o sensor está inativo
        }
        return this.robo.getPosicaoYInterna();
    }
    
    // Método para vincular o GPS a um robô
    public void vincularRobo(Robo robo) {
        this.robo = robo;
    }
}