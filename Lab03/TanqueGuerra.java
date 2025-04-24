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
        Object alvo = (Object) ambiente.identificarObjetoPosicao(alvoX, alvoY);
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
                    String result = null;
                    if (alvo instanceof Robo) {
                        Robo alvodef = (Robo) alvo;
                        String defesa = alvodef.defender(nTiros);
                        result = String.format(
                                "Disparo realizado no alvo (%d, %d)\n" +
                                        "Robô %s foi atingido!\n" +
                                        defesa,
                                alvoX, alvoY, alvodef.getNome());
                    } else if (alvo instanceof Obstaculo) {
                        Obstaculo alvodef = (Obstaculo) alvo;
                        String defesa = alvodef.defender(nTiros, ambiente);
                        result = String.format(
                                "Disparo realizado no alvo (%d, %d)\n" +
                                        "Obstáculo %s foi atingido!\n" +
                                        defesa,
                                alvoX, alvoY, alvodef.getTipo());

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
