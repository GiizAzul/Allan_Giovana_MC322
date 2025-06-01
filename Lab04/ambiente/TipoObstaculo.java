package ambiente;

public enum TipoObstaculo {
    PAREDE(3, 100, false, 0.2f,"⬜"),
    ARVORE(5, 50, false, 0.2f, "🌲"),
    PREDIO(10, 200, false, 0.7f,"🏢"),
    BURACO(1, 0, true, 0, "🕳️ "),
    OUTRO(-1, -1, false, 0, "O"); // Altura -1 representa valor variavel

    private final int alturaPadrao;
    private final int integridade;
    private final boolean indestrutivel;
    private final String representacao;

    // Se fator for 0, o obstáculo não é visível
    private final float fatorReducaoAlcanceRadar;

    TipoObstaculo(int alturaPadrao, int integridade, boolean indestrutivel, float fatorReducaoAlcanceRadar, String representacao) {
        this.alturaPadrao = alturaPadrao;
        this.integridade = integridade;
        this.indestrutivel = indestrutivel;
        this.fatorReducaoAlcanceRadar = fatorReducaoAlcanceRadar;
        this.representacao=representacao;
    }

    public String getRepresentacao(){
        return representacao;
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