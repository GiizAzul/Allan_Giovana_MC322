package ambiente;
import interfaces.*;
public class Obstaculo implements Entidade, Destrutivel{
    private final int x1;
    private final int x2;
    private final int y1;
    private final int y2;
    private int altura;
    private final TipoObstaculo tipoObstaculo;
    private final TipoEntidade tipo;
    private int integridade;
    private boolean indestrutivel;

    public Obstaculo(TipoObstaculo tipoObstaculo, int x1, int x2, int y1, int y2) {
        this.tipoObstaculo = tipoObstaculo;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.altura = tipoObstaculo.getAlturaPadrao();
        this.integridade = tipoObstaculo.getIntegridade();
        this.indestrutivel = tipoObstaculo.isIndestrutivel();
        this.tipo=TipoEntidade.OBSTACULO;
    }

    public int getX(){
        return 0;
    }

    public int getY(){
        return 0;
    }

    public int getZ(){
        return 0;
    }

    public String getDescricao(){
        return "Obstáculo" + getTipo() + "na posição" + "X1: " + getX1() + "X2: " + getX2()+ "Y1: " + getY1() + "Y2: " + getY2() + "Altura: " + getAltura();
    }

    public String getRepresentacao(){
        return tipoObstaculo.getRepresentacao();
    }

    public TipoEntidade getTipo(){
        return tipo;
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

    public TipoObstaculo getTipoObstaculo() {
        return tipoObstaculo;
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
                ambiente.removerEntidade(this);
                return "O obstáculo foi destruído";
            }

            return "O obstáculo ainda não foi destruído";
        }
    }

}
