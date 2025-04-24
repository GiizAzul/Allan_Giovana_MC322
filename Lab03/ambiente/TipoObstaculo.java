package ambiente;

public enum TipoObstaculo {
    PAREDE(3, 100, false),
    ARVORE(5, 50, false),
    PREDIO(10, 200, false),
    BURACO(0, 0, true),
    OUTRO(-1, -1, false); // Altura -1 representa valor variavel

    private final int alturaPadrao;
    private final int integridade;
    private final boolean indestrutivel;

    TipoObstaculo(int alturaPadrao, int integridade, boolean indestrutivel) {
        this.alturaPadrao = alturaPadrao;
        this.integridade = integridade;
        this.indestrutivel = indestrutivel;
    }

    public int getAlturaPadrao() {
        return alturaPadrao;
    }

    public int getIntegridade() {
        return integridade;
    }

    public boolean isIndestrutivel() {
        return indestrutivel;
    }
}