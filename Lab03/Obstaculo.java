public class Obstaculo {
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private int altura;
    private TipoObstaculo tipo;
    private int integridade;

    public Obstaculo(TipoObstaculo tipo, int x1, int x2, int y1, int y2){
        this.tipo = tipo;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.altura = tipo.getAlturaPadrao();
        this.integridade = tipo.getIntegridade(); 
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

    public TipoObstaculo getTipo() {
        return tipo;
    }

    public int getIntegridade() {
        return integridade;
    }

    public void setIntegridade(int integridade) {
        this.integridade = integridade;
    }

    

}
