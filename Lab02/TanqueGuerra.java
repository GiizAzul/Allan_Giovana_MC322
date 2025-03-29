public class TanqueGuerra extends RoboTerrestre {
    private int municaoMax;
    private int municaoAtual;
    private int alcance;

    public TanqueGuerra(String nome, String direcao, int posicaoX, int posicaoY, int velocidadeMaxima, int municaoMax, int alcance){
        super(nome, direcao, posicaoX, posicaoY, velocidadeMaxima);

        this.municaoMax=municaoMax;
        this.municaoAtual=municaoMax;
        this.alcance=alcance;
        setIntegridade(100);
    }

    public String atirar(int alvoX, int alvoY, int nTiros, Ambiente ambiente) {
        Robo alvo = (Robo)identificarObstaculoPosicao(ambiente, alvoY, alvoX);
        if (municaoAtual < nTiros) {
            return "Munição insuficiente";
        } else if (alvoX == getPosicaoX() && alvoY == getPosicaoY()){
            return "Não é possível atirar na própria posição";
        } else if ( alvo != null){
            int dX = getPosicaoX() - alvoX;
            int dY = getPosicaoY() - alvoY;
            if (Math.pow(dX, 2) + Math.pow(dY, 2) <= Math.pow(alcance,2)) {
                municaoAtual -= nTiros;
                return "Disparo do " + getNome() + " realizado no robo " + alvo.getNome() + "\n" + alvo.defender(nTiros);
            } else {
                return "Alvo fora do alcance";
            }
        } else{
            return "Não há alvos nesta posição";
        }   
    }

    public String recarregar(int nBalas){
        municaoAtual+=nBalas;
        if (municaoAtual>municaoMax){
            municaoAtual=municaoMax;
        }
        return "Recarregamento concluido";
        }
    }
