public class DroneAtaque extends RoboAereo {

    private int municao;
    private int alcance;
    private int escudo;

    public DroneAtaque(String nome, String dir, int x, int y, int alt, int altMax, 
                   int muni, int auto, int alc) {
        
        super(nome, dir, x, y, alt, altMax);
        this.municao = muni;
        this.alcance = alc;
        this.escudo = 100;
    }

    @Override
    public String defender(int dano){
        if (this.escudo > 0 ) {
            if (this.escudo > dano) {
                this.escudo -= dano;
                return String.format("O robô %s defendeu o dano em seu escudo!\n", this.getNome());
            } else {
                dano -= this.escudo;
                this.setIntegridade(this.getIntegridade() - dano);
                return String.format("O robô %s defendeu parcialmente com o escudo mas sofreu danos!\n", this.getNome());
            }
        } else {
            this.setIntegridade(this.getIntegridade() - dano);
            if (this.getOperando()) {
                return String.format("O robô %s está inoperante devido ao dano tomado", this.getNome());
            } else {
                return String.format("O robô %s ainda está operante", this.getNome());
            }
        }
    }
    public String atirar(int alvoX, int alvoY, int alvoZ, int nTiros) {
        // Verifica se há munição suficiente
        if (this.municao < nTiros) {
            return "Munição insuficiente";
        }

        // Verifica se alvo está no alcance
        int dX = this.getPosicaoX() - alvoX;
        int dY = this.getPosicaoY() - alvoY;

        if (Math.pow(dX, 2) + Math.pow(dY, 2) <= Math.pow(this.alcance,2)) {
            this.municao -= nTiros;
            return "Disparo realizado!";
        } else {
            return "Alvo fora do alcance";
        }
    }

    public String atirar(int alvoX, int alvoY, int nTiros) {
        // Verifica se há munição suficiente
        if (this.municao < nTiros) {
            return "Munição insuficiente";
        }

        // Verifica se alvo está no alcance
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
}