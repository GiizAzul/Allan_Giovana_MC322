public class DroneAtaque extends RoboAereo {

    private int municao;
    private int alcance;
    private int escudo;

    public DroneAtaque(String nome, String dir, int x, int y, int alt, int altMax,
            int muni, int alc) {

        super(nome, dir, x, y, alt, altMax);
        this.municao = muni;
        this.alcance = alc;
        this.escudo = 100;
    }

    @Override
    public String defender(int dano) {
        if (this.escudo > 0) {
            if (this.escudo > dano) {
                this.escudo -= dano;
                return String.format("O robô %s defendeu o dano em seu escudo!\n", this.getNome());
            } else {
                dano -= this.escudo;
                this.setIntegridade(this.getIntegridade() - dano);
                return String.format("O robô %s defendeu parcialmente com o escudo mas sofreu danos!\n",
                        this.getNome());
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

    public String atirar(int alvoX, int alvoY, int alvoZ, int nTiros, Ambiente ambiente) {
        // Verifica se há munição suficiente
        if (this.municao < nTiros) {
            return "Munição insuficiente";
        }

        // Verifica se alvo está no alcance
        int dX = this.getPosicaoX() - alvoX;
        int dY = this.getPosicaoY() - alvoY;
        int dZ = this.getAltitude() - alvoZ;

        return executarTiro(dX, dY, dZ, alvoX, alvoY, alvoZ, nTiros, ambiente);

    }

    public String atirar(int alvoX, int alvoY, int nTiros, Ambiente ambiente) {
        // Verifica se há munição suficiente
        if (this.municao < nTiros) {
            return "Munição insuficiente";
        }

        // Verifica se alvo está no alcance
        int dX = this.getPosicaoX() - alvoX;
        int dY = this.getPosicaoY() - alvoY;

        return executarTiro(dX, dY, this.getAltitude(), alvoX, alvoY, 0, nTiros, ambiente);

    }

    public String atirar(Robo robo, int nTiros, Ambiente ambiente) {
        if (this.municao < nTiros) {
            return "Munição insuficiente";
        }

        if (robo instanceof RoboTerrestre) {
            String result = this.atirar(robo.getPosicaoX(), robo.getPosicaoY(), nTiros, ambiente);
            return result;
        } else if (robo instanceof RoboAereo) {
            RoboAereo roboAereo = (RoboAereo) robo;
            String result = this.atirar(roboAereo.getPosicaoX(), roboAereo.getPosicaoY(), roboAereo.getAltitude(),
                    nTiros, ambiente);
            return result;
        } else {
            return "Robô inválido!";
        }
    }

    private String executarTiro(int dX, int dY, int dZ, int aX, int aY, int aZ, int nTiros, Ambiente ambiente) {
        if (Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2) <= Math.pow(this.alcance, 2)) {
            this.municao -= nTiros;
            Robo alvo = (Robo) ambiente.identificarObjetoPosicao(aX, aY, aZ);
            if (alvo == null) {
                return String.format("Disparado realizado nas coordenadas (%d, %d, %d)\nNenhum alvo foi atingido!\n",
                        aX, aY, aZ);
            } else {
                String defesa = alvo.defender(nTiros);
                String result = String.format(
                        "Disparo realizado no alvo (%d, %d, %d)\n" +
                                "Robô foi %s foi atingido!\n" +
                                defesa,
                        aX, aY, aZ, alvo.getNome());
                return result;
            }
        } else {
            return "Alvo fora do alcance";
        }
    }
}