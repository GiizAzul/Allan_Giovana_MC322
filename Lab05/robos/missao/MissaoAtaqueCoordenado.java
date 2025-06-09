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
    public void executar(Robo robo, Ambiente ambiente) {
        if (robo instanceof DroneAtaque) {
            DroneAtaque drone = (DroneAtaque) robo;
            System.out.println("Iniciando missão: Ataque aéreo coordenado.");
            try {
                // Mover-se para a posição de ataque
                drone.mover(alvoX, alvoY, altitudeAtaque, ambiente);
                System.out.println("Posicionado para o ataque.");

                // Atirar no alvo no solo (Z=0)
                System.out.println(drone.atirar(alvoX, alvoY, 0, nTiros, ambiente));
                System.out.println("Missão de ataque aéreo concluída.");

            } catch (Exception e) {
                System.err.println("Falha na missão de ataque: " + e.getMessage());
            }
        }
    }
}
