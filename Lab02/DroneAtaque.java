public class DroneAtaque extends RoboAereo {

    private int qtdMunicao;
    private String tipoArmamento;
    private int alcance;
    private int autonomia;

    public DroneAtaque(String nome, String direcao, int posicaoX, int posicaoY, int altitude, int altitudeMaxima, int qtdMunicao, String tipoArmamento, int autonomia, int alcance) {
        super(nome, direcao, posicaoX, posicaoY, altitude, altitudeMaxima);
        this.qtdMunicao = qtdMunicao;
        this.tipoArmamento = tipoArmamento;
        this.autonomia = autonomia;
        this.alcance = alcance;
    }

    public String atirar(int alvoX, int alvoY, int nTiros) {
        // Verifica se alvo está no alcance
        if (this.qtdMunicao < nTiros) {
            return "Munição insuficiente";
        }

        int dX = this.getPosicaoX() - alvoX;
        int dY = this.getPosicaoY() - alvoY;

        if (Math.pow(dX, 2) + Math.pow(dY, 2) <= Math.pow(this.alcance,2)) {
            this.qtdMunicao -= nTiros;
            return "Disparo realizado!";
        } else {
            return "Alvo fora do alcance";
        }

    }
}