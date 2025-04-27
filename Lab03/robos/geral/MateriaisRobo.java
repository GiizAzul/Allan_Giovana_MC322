package robos.geral;

public enum MateriaisRobo {
    ALUMINIO(0.9f),
    ACO(1),
    PLASTICO(0.5f),
    FIBRA_VIDRO(0.4f),
    FIBRA_CARBONO(0.2f),
    OUTRO(1);

    private final float fatorReducaoAlcanceRadar;

    MateriaisRobo(float fatorReducaoAlcanceRadar) {
        this.fatorReducaoAlcanceRadar = fatorReducaoAlcanceRadar;
    }

    public float getFatorReducaoAlcanceRadar() {
        return fatorReducaoAlcanceRadar;
    }
}
