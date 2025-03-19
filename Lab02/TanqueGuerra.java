public class TanqueGuerra extends RoboTerrestre {
    private String tipo;
    private int integridade;
    private int municaoMax;
    private int municaoAtual;
    private int alcance;
    private boolean operando;

    public TanqueGuerra(String nome, String direcao, int posicaoX, int posicaoY, int velocidadeMaxima, String tipo, int municaoMax, int alcance){
        super(nome, direcao, posicaoX, posicaoY, velocidadeMaxima);
        this.tipo=tipo;
        this.integridade=100;
        this.municaoMax=municaoMax;
        this.municaoAtual=municaoMax;
        this.alcance=alcance;
        this.operando=true;
    }

    public String atirar(int alvoX, int alvoY, int nTiros) {
        // Verifica se alvo está no alcance
        if (municaoAtual < nTiros) {
            return "Munição insuficiente";
        }

        int dX = getPosicaoX() - alvoX;
        int dY = getPosicaoY() - alvoY;

        if (Math.pow(dX, 2) + Math.pow(dY, 2) <= Math.pow(alcance,2)) {
            municaoAtual -= nTiros;
            return "Disparo realizado!";
        } else {
            return "Alvo fora do alcance";
        }
    }

    public String defender(int dano){
        integridade-=dano;

        if (integridade<=0){
            operando=false;
            return "O robô está inoperante devido ao dano tomado";
        }

        return "O robô ainda está operando";
    }

    public void recarregar(int nBalas){
        municaoAtual+=nBalas;
        if (municaoAtual>municaoMax){
            municaoAtual=municaoMax;
        }
    }
}
