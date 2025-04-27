package ambiente;

public enum TipoObstaculo {
    PAREDE(3, 100, false, 0.2f),
    ARVORE(5, 50, false, 0.2f),
    PREDIO(10, 200, false, 0.7f),
    BURACO(0, 0, true, 0),
    OUTRO(-1, -1, false, 0); // Altura -1 representa valor variavel

    private final int alturaPadrao;
    private final int integridade;
    private final boolean indestrutivel;

    // Se fator for 0, o obstáculo não é visível
    private final float fatorReducaoAlcanceRadar;

    TipoObstaculo(int alturaPadrao, int integridade, boolean indestrutivel, float fatorReducaoAlcanceRadar) {
        this.alturaPadrao = alturaPadrao;
        this.integridade = integridade;
        this.indestrutivel = indestrutivel;
        this.fatorReducaoAlcanceRadar = fatorReducaoAlcanceRadar;
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

    public float getFatorReducaoAlcanceRadar() {
        return fatorReducaoAlcanceRadar;
    }
}