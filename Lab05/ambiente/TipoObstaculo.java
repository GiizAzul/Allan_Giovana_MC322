package ambiente;

/**
 * Enum que define os diferentes tipos de obstáculos disponíveis no ambiente.
 * Cada tipo possui características específicas como altura, integridade e efeitos no radar.
 */
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

    /**
     * Construtor do enum que define as características de cada tipo de obstáculo
     * @param alturaPadrao Altura padrão do obstáculo em unidades do ambiente
     * @param integridade Pontos de vida/resistência do obstáculo
     * @param indestrutivel Se true, o obstáculo não pode ser destruído
     * @param fatorReducaoAlcanceRadar Fator que reduz o alcance do radar (0.0 a 1.0)
     * @param representacao Símbolo visual usado para representar o obstáculo no mapa
     */
    TipoObstaculo(int alturaPadrao, int integridade, boolean indestrutivel, float fatorReducaoAlcanceRadar, String representacao) {
        this.alturaPadrao = alturaPadrao;
        this.integridade = integridade;
        this.indestrutivel = indestrutivel;
        this.fatorReducaoAlcanceRadar = fatorReducaoAlcanceRadar;
        this.representacao=representacao;
    }

    /**
     * Retorna a representação visual do obstáculo
     * @return String contendo o símbolo que representa o obstáculo no mapa
     */
    public String getRepresentacao(){
        return representacao;
    }

    /**
     * Retorna a altura padrão do obstáculo
     * @return Altura padrão em unidades do ambiente
     */
    public int getAlturaPadrao() {
        return alturaPadrao;
    }

    /**
     * Retorna a integridade (pontos de vida) do obstáculo
     * @return Quantidade de dano que o obstáculo pode receber antes de ser destruído
     */
    public int getIntegridade() {
        return integridade;
    }

    /**
     * Verifica se o obstáculo é indestrutível
     * @return true se o obstáculo não pode ser destruído, false caso contrário
     */
    public boolean isIndestrutivel() {
        return indestrutivel;
    }

    /**
     * Retorna o fator de redução do alcance do radar
     * @return Valor entre 0.0 e 1.0 que representa quanto o obstáculo reduz o alcance do radar
     *         (0.0 = invisível ao radar, 1.0 = bloqueia completamente o radar)
     */
    public float getFatorReducaoAlcanceRadar() {
        return fatorReducaoAlcanceRadar;
    }
}