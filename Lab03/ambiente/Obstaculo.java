package ambiente;
public class Obstaculo {
    private final int x1;
    private final int x2;
    private final int y1;
    private final int y2;
    private int altura;
    private final TipoObstaculo tipo;
    private int integridade;
    private boolean indestrutivel;

    public Obstaculo(TipoObstaculo tipo, int x1, int x2, int y1, int y2) {
        this.tipo = tipo;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.altura = tipo.getAlturaPadrao();
        this.integridade = tipo.getIntegridade();
        this.indestrutivel = tipo.isIndestrutivel();
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura){
        this.altura = altura;
    }

    public TipoObstaculo getTipo() {
        return tipo;
    }

    public int getIntegridade() {
        return integridade;
    }

    public void setIntegridade(int integridade){
        this.integridade=integridade;
    }

    public boolean isIndestrutivel() {
        return indestrutivel;
    }

    public void setIndestrutivel(boolean indestrutivel){
        this.indestrutivel=indestrutivel;
    }

    public String defender(int dano, Ambiente ambiente) {
        if (indestrutivel) {
            return "O obstáculo é indestrutível";
        } else {
            this.integridade -= dano;

            if (integridade <= 0) {
                ambiente.removerObstaculo(this);
                return "O obstáculo foi destruído";
            }

            return "O obstáculo ainda não foi destruído";
        }
    }

}
