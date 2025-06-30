package robos.missao;

import ambiente.Ambiente;
import robos.geral.Robo;
import robos.terrestres.TanqueGuerra;
import utils.Logger;

public class MissaoDestruirAlvo implements Missao {
    private int alvoX;
    private int alvoY;
    private int nTiros;

    public MissaoDestruirAlvo(int alvoX, int alvoY, int nTiros) {
        this.alvoX = alvoX;
        this.alvoY = alvoY;
        this.nTiros = nTiros;
    }

    @Override
    public String executar(Robo robo, Ambiente ambiente, Logger logger) {
        // Verifica se o robô é realmente um TanqueGuerra para usar suas funções específicas
        if (robo instanceof TanqueGuerra) {
            TanqueGuerra tanque = (TanqueGuerra) robo;
            String resultado = "MISSÃO: Disparando contra (" + alvoX + ", " + alvoY + ")\n";
            try {
                // Utiliza o método já existente no TanqueGuerra
                resultado += tanque.executarTarefa("atirar", alvoX, alvoY, nTiros, ambiente);
                logger.escreverLogSucesso(resultado);
            } catch (Exception e) {
                resultado+="\nFalha ao executar a missão de destruição: " + e.getMessage();
                try{
                    logger.escreverLogFalha(resultado);
                } catch(Exception ex){
                    resultado+="";
                }
            }
            return resultado;
        } else {
            return "Missão DestruirAlvo só pode ser executada por um TanqueGuerra.";
        }
    }
}