package robos.missao;


// No pacote de missoes
import ambiente.Ambiente;
import robos.aereos.DroneAtaque;
import robos.geral.Robo;
import utils.Logger;

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
    public String executar(Robo robo, Ambiente ambiente, Logger logger) {
        if (robo instanceof DroneAtaque) {
            logger.ativarLogger();
            DroneAtaque drone = (DroneAtaque) robo;
            String resultado = "MISSÃO: Ataque aéreo coordenado.\n";
            try {
                // Mover-se para a posição de ataque
                drone.mover(alvoX, alvoY, altitudeAtaque+1, ambiente);
                resultado += "Posicionado para o ataque.\n";

                resultado += drone.atirar(alvoX, alvoY, altitudeAtaque, nTiros, ambiente);
                resultado += "\nMissão de ataque aéreo concluída.";

                logger.escreverLogSucesso(resultado);

            } catch (Exception e) {
                resultado += "Falha na missão de ataque: " + e.getMessage();
                try{
                    logger.escreverLogFalha(resultado);
                } catch(Exception ex){
                    resultado+="";
                }
            }
            return resultado;
        } else {
            return "Missão AtaqueCoordenado só pode ser executada por um DroneAtaque.";
        }
    }
}
