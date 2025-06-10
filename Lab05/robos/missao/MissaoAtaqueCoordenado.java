package robos.missao;

// No pacote de missoes
import ambiente.Ambiente;
import robos.aereos.DroneAtaque;
import robos.geral.Robo;

public class MissaoAtaqueCoordenado implements Missao {
    private int alvoX;
    private int alvoY;
    private int altitudeAtaque;
    private int nTiros;

    public MissaoAtaqueCoordenado(int alvoX, int alvoY, int altitudeAtaque, int nTiros) {
        this.alvoX = alvoX;
        this.alvoY = alvoY;
        this.altitudeAtaque = altitudeAtaque;
        this.nTiros = nTiros;
    }

    @Override
    public String executar(Robo robo, Ambiente ambiente) {
        if (robo instanceof DroneAtaque) {
            DroneAtaque drone = (DroneAtaque) robo;
            String resultado = "Iniciando missão: Ataque aéreo coordenado.\n";
            try {
                // Mover-se para a posição de ataque
                drone.mover(alvoX, alvoY, altitudeAtaque+1, ambiente);
                resultado += "Posicionado para o ataque.\n";

                resultado += drone.atirar(alvoX, alvoY, altitudeAtaque, nTiros, ambiente);
                resultado += "Missão de ataque aéreo concluída.\n";

            } catch (Exception e) {
                resultado += "Falha na missão de ataque: " + e.getMessage()+"\n";
            }
            return resultado;
        } else {
            return "Missão AtaqueCoordenado só pode ser executada por um DroneAtaque.";
        }
    }
}
