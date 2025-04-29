package robos.geral;

/**
 * Materiais utilizados na construção de robôs.
 * Cada material possui um fator que influencia a detecção por radar.
 * Valores menores indicam materiais menos detectáveis por radar.
 */
public enum MateriaisRobo {
    /** Alumínio: Metal leve com boa reflexividade (90% do alcance) */
    ALUMINIO(0.9f),
    
    /** Aço: Metal pesado com alta reflexividade (100% do alcance) */
    ACO(1),
    
    /** Plástico: Material sintético com média reflexividade (50% do alcance) */
    PLASTICO(0.5f),
    
    /** Fibra de Vidro: Composto com baixa reflexividade (40% do alcance) */
    FIBRA_VIDRO(0.4f),
    
    /** Fibra de Carbono: Material stealth com reflexividade mínima (20% do alcance) */
    FIBRA_CARBONO(0.2f),
    
    /** Outro: Material genérico (100% do alcance) */
    OUTRO(1);

    private final float fatorReducaoAlcanceRadar;

    /**
     * @param fatorReducaoAlcanceRadar Multiplicador de alcance do radar (0-1)
     */
    MateriaisRobo(float fatorReducaoAlcanceRadar) {
        this.fatorReducaoAlcanceRadar = fatorReducaoAlcanceRadar;
    }

    /**
     * @return Fator de redução do alcance do radar para este material
     */
    public float getFatorReducaoAlcanceRadar() {
        return fatorReducaoAlcanceRadar;
    }
}
