package robos.terrestres;
import ambiente.Ambiente;
import ambiente.Obstaculo;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import interfaces.*;

public class TanqueGuerra extends RoboTerrestre implements Atacante{
    private int municaoMax;
    private int municaoAtual;
    private int alcance;

    public TanqueGuerra(String nome, String direcao, Ambiente ambiente, MateriaisRobo material, int posicaoX, int posicaoY, int velocidade, int velocidadeMaxima, int municaoMax,
            int alcance) {
        super(nome, direcao, ambiente, material, posicaoX, posicaoY, velocidade, velocidadeMaxima);

        this.municaoMax = municaoMax;
        this.municaoAtual = municaoMax;
        this.alcance = alcance;
        setIntegridade(100);
    }

    public String executarTarefa(Object... argumentos){
        String result = super.executarTarefa(argumentos);
        if (result != ""){
            return result;
        }
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "atirar":
                int alvoX = (Integer) argumentos[1];
                int alvoY = (Integer) argumentos[2];
                int nTiros = (Integer) argumentos[3];
                Ambiente ambiente = (Ambiente) argumentos[4];
                return atirar(alvoX,alvoY, 0,nTiros,ambiente);
        
            case "recarregar":
                int nBalas = (Integer) argumentos[1];
                return recarregar(nBalas);

            default:
                return "";
        }
    }

    public String atirar(int alvoX, int alvoY, int alvoZ, int nTiros, Ambiente ambiente) {
        Entidade alvo = ambiente.identificarEntidadePosicao(alvoX, alvoY, 0);
        if (municaoAtual < nTiros) {
            return "Munição insuficiente";

        } else if (alvoX == getX() && alvoY == getY()) {
            return "Não é possível atirar na própria posição";

        } else {
            int dX = getX() - alvoX;
            int dY = getY() - alvoY;
            if (Math.pow(dX, 2) + Math.pow(dY, 2) <= Math.pow(alcance, 2)) {
                municaoAtual -= nTiros;
                if (alvo == null) {
                    return String.format("Disparado realizado no alvo (%d, %d)\nNenhum alvo foi atingido!\n", alvoX,
                            alvoY);
                } else {
                    String result = null;
                    if (alvo.getTipo() == TipoEntidade.ROBO) {
                        Robo alvodef = (Robo) alvo;
                        String defesa = alvodef.defender(nTiros);
                        result = String.format(
                                "Disparo realizado no alvo (%d, %d)\n" +
                                        "Robô %s foi atingido!\n" +
                                        defesa,
                                alvoX, alvoY, alvodef.getNome());
                    } else if (alvo.getTipo() == TipoEntidade.OBSTACULO) {
                        Obstaculo alvodef = (Obstaculo) alvo;
                        String defesa = alvodef.defender(nTiros, ambiente);
                        result = String.format(
                                "Disparo realizado no alvo (%d, %d)\n" +
                                        "Obstáculo %s foi atingido!\n" +
                                        defesa,
                                alvoX, alvoY, alvodef.getTipoObstaculo());

                    }
                    return result;
                }
            } else {
                return "Alvo fora do alcance";
            }
        }
    }

    public String recarregar(int nBalas) {
        municaoAtual += nBalas;
        if (municaoAtual > municaoMax) {
            municaoAtual = municaoMax;
        }
        return "Recarregamento concluido";
    }
}
