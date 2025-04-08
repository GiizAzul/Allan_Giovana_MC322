public enum TipoObstaculo {
    PAREDE(3, true, 100, false),
    ARVORE(5, true, 50, false),
    PREDIO(10, true, 200, false),
    BURACO(0, true, 0, true),
    OUTRO(-1, true, -1, false); // Altura -1 representa valor variavel

    private final int alturaPadrao;
    private final boolean bloqueiaPassagem;
    private final int integridade;
    private final boolean indestrutivel;

    TipoObstaculo(int alturaPadrao, boolean bloqueiaPassagem, int integridade, boolean indestrutivel) {
        this.alturaPadrao = alturaPadrao;
        this.bloqueiaPassagem = bloqueiaPassagem;
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

    public boolean isBloqueiaPassagem() {
        return bloqueiaPassagem;
    }
}