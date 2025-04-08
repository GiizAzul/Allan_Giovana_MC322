public class TanqueGuerra extends RoboTerrestre {
    private int municaoMax;
    private int municaoAtual;
    private int alcance;

    public TanqueGuerra(String nome, String direcao, int posicaoX, int posicaoY, int velocidadeMaxima, int municaoMax,
            int alcance) {
        super(nome, direcao, posicaoX, posicaoY, velocidadeMaxima);

        this.municaoMax = municaoMax;
        this.municaoAtual = municaoMax;
        this.alcance = alcance;
        setIntegridade(100);
    }

    public String atirar(int alvoX, int alvoY, int nTiros, Ambiente ambiente) {
        Robo alvo = (Robo) ambiente.identificarObjetoPosicao(alvoX, alvoY);
        if (municaoAtual < nTiros) {
            return "Munição insuficiente";

        } else if (alvoX == getPosicaoX() && alvoY == getPosicaoY()) {
            return "Não é possível atirar na própria posição";

        } else {
            int dX = getPosicaoX() - alvoX;
            int dY = getPosicaoY() - alvoY;
            if (Math.pow(dX, 2) + Math.pow(dY, 2) <= Math.pow(alcance, 2)) {
                municaoAtual -= nTiros;
                if (alvo == null) {
                    return String.format("Disparado realizado no alvo (%d, %d)\nNenhum alvo foi atingido!\n", alvoX,
                            alvoY);
                } else {
                    String defesa = alvo.defender(nTiros);
                    String result = String.format(
                            "Disparo realizado no alvo (%d, %d)\n" +
                                    "Robô foi %s foi atingido!\n" +
                                    defesa,
                            alvoX, alvoY, alvo.getNome());
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
