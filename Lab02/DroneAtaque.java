/*public class DroneAtaque extends RoboAereo {

    private int municao;
    private int alcance;

    public DroneAtaque(String nome, String dir, int x, int y, int alt, int altMax, 
                   int muni, int auto, int alc) {
        
        super(nome, dir, x, y, alt, altMax);
        this.municao = muni;
        this.alcance = alc;
    }

    public String atirar(int alvoX, int alvoY, int nTiros) {
        // Verifica se alvo está no alcance
        if (this.municao < nTiros) {
            return "Munição insuficiente";
        }

        int dX = this.getPosicaoX() - alvoX;
        int dY = this.getPosicaoY() - alvoY;

        if (Math.pow(dX, 2) + Math.pow(dY, 2) <= Math.pow(this.alcance,2)) {
            this.municao -= nTiros;
            return "Disparo realizado!";
        } else {
            return "Alvo fora do alcance";
        }

    }

    public String atirar(Robo robo, int nTiros) {
        // Verifica se alvo está no alcance
        if (this.municao < nTiros) {
            return "Munição insuficiente";
        }

        int dX = this.getPosicaoX() - robo.getPosicaoX();
        int dY = this.getPosicaoY() - robo.getPosicaoY();

        if (Math.pow(dX, 2) + Math.pow(dY, 2) <= Math.pow(this.alcance,2)) {
            this.municao -= nTiros;
            return "Disparo realizado!";
        } else {
            return "Alvo fora do alcance";
        }
    }
}*/