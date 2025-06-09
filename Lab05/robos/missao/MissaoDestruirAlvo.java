package robos.missao;

import ambiente.Ambiente;
import robos.geral.Robo;
import robos.terrestres.TanqueGuerra;

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
    public void executar(Robo robo, Ambiente ambiente) {
        // Verifica se o robô é realmente um TanqueGuerra para usar suas funções específicas
        if (robo instanceof TanqueGuerra) {
            TanqueGuerra tanque = (TanqueGuerra) robo;
            System.out.println("MISSÃO: Disparando contra (" + alvoX + ", " + alvoY + ")");
            try {
                // Utiliza o método já existente no TanqueGuerra
                String resultado = tanque.executarTarefa("atirar", alvoX, alvoY, nTiros, ambiente);
                System.out.println("Resultado do disparo: " + resultado);
            } catch (Exception e) {
                System.err.println("Falha ao executar a missão de destruição: " + e.getMessage());
            }
        } else {
            System.out.println("Missão DestruirAlvo só pode ser executada por um TanqueGuerra.");
        }
    }
}