package robos.aereos;
import ambiente.Ambiente;
import ambiente.Obstaculo;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.terrestres.RoboTerrestre;

public class DroneAtaque extends RoboAereo {

    private int municao;
    private int alcance;
    private int escudo;

    public DroneAtaque(String nome, String dir, MateriaisRobo m, int x, int y, int vel, int alt, int altMax, Ambiente amb,
            int muni, int alc) {

        super(nome, dir, m, x, y, vel, alt, altMax, amb);
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
        int dX = this.getX() - alvoX;
        int dY = this.getY() - alvoY;
        int dZ = this.getZ() - alvoZ;

        return executarTiro(dX, dY, dZ, alvoX, alvoY, alvoZ, nTiros, ambiente);

    }

    public String atirar(int alvoX, int alvoY, int nTiros, Ambiente ambiente) {
        // Verifica se há munição suficiente
        if (this.municao < nTiros) {
            return "Munição insuficiente";
        }

        // Verifica se alvo está no alcance
        int dX = this.getX() - alvoX;
        int dY = this.getY() - alvoY;

        return executarTiro(dX, dY, this.getZ(), alvoX, alvoY, 0, nTiros, ambiente);

    }

    public String atirar(Robo robo, int nTiros, Ambiente ambiente) {
        if (this.municao < nTiros) {
            return "Munição insuficiente";
        }

        if (robo instanceof RoboTerrestre) {
            String result = this.atirar(robo.getX(), robo.getY(), nTiros, ambiente);
            return result;
        } else if (robo instanceof RoboAereo) {
            RoboAereo roboAereo = (RoboAereo) robo;
            String result = this.atirar(roboAereo.getX(), roboAereo.getY(), roboAereo.getZ(),
                    nTiros, ambiente);
            return result;
        } else {
            return "Robô inválido!";
        }
    }

    private String executarTiro(int dX, int dY, int dZ, int aX, int aY, int aZ, int nTiros, Ambiente ambiente) {
        if (Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2) <= Math.pow(this.alcance, 2)) {
            this.municao -= nTiros;
            Object alvo = ambiente.identificarEntidadePosicao(aX, aY, aZ);
            if (alvo == null) {
                return String.format("Disparado realizado nas coordenadas (%d, %d, %d)\nNenhum alvo foi atingido!\n",
                        aX, aY, aZ);
            } else {
                if (alvo instanceof Robo) {
                    Robo alvodef = (Robo) alvo;
                    String defesa = alvodef.defender(nTiros);
                    String result = String.format(
                            "Disparo realizado no alvo (%d, %d, %d)\n" +
                                    "Robô foi %s foi atingido!\n" +
                                    defesa,
                            aX, aY, aZ, alvodef.getNome());
                    return result;
                } else {
                    Obstaculo alvodef = (Obstaculo) alvo;
                    String defesa = alvodef.defender(nTiros, ambiente);
                    String result = String.format(
                            "Disparo realizado no alvo (%d, %d, %d)\n" +
                                    "Obstáculo foi %s foi atingido!\n" +
                                    defesa,
                            aX, aY, aZ, alvodef.getTipo());
                    return result;
                }
            }

        } else {
            return "Alvo fora do alcance";
        }
    }
}