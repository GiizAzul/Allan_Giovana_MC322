package robos.aereos;

import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import interfaces.Entidade;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;

public class DroneAtaque extends RoboAereo {

    private int municao;
    private int alcance;
    private int escudo;

    public DroneAtaque(String nome, String dir, MateriaisRobo m, int x, int y, int vel, int alt, int altMax,
            Ambiente amb,
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
            if (this.getEstado()) {
                return String.format("O robô %s está desligado devido ao dano tomado", this.getNome());
            } else {
                return String.format("O robô %s ainda está ligado", this.getNome());
            }
        }
    }

    public String executarTarefa(Object... argumentos) {
        String result = super.executarTarefa(argumentos);
        if (result != "") {
            return result;
        }
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "atirar coord":
                int alvoX = (Integer) argumentos[1];
                int alvoY = (Integer) argumentos[2];
                int alvoZ = (Integer) argumentos[3];
                int nTiros = (Integer) argumentos[4];
                Ambiente ambiente = (Ambiente) argumentos[5];
                return atirar(alvoX, alvoY, alvoZ, nTiros, ambiente);

            case "atirar robo":
                Robo robo = (Robo) argumentos[1];
                nTiros = (Integer) argumentos[2];
                ambiente = (Ambiente) argumentos[3];
                return atirar(robo, nTiros, ambiente);

            case "identificar":
                ArrayList<Entidade> listaObjetosVistos = getRadar().acionar();
                if (listaObjetosVistos.isEmpty()) {
                    return "Nenhum objeto encontrado!";
                } else {
                    for (Entidade elemento : listaObjetosVistos) {
                        if (elemento instanceof Obstaculo) {
                            Obstaculo o = (Obstaculo) elemento;
                            result+=String.format(
                                    "Obstáculo encontrado: %s, X1: %d, X2: %d, Y1: %d, Y2: %d, Altura: %d\n",
                                    o.getTipoObstaculo(), o.getX1(), o.getX2(), o.getY1(), o.getY2(),
                                    o.getAltura());
                        } else if (elemento instanceof Robo) {
                            Robo r = (Robo) elemento;
                            result+=String.format("Robô encontrado: %s, X: %d, Y: %d, Z: %d\n",
                                    r.getNome(), r.getPosicaoXInterna(), r.getPosicaoYInterna(), r.getZ());
                        }
                    }
                    return result;
                }

            default:
                return "";
        }
    }

    private String atirar(int alvoX, int alvoY, int alvoZ, int nTiros, Ambiente ambiente) {
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

    private String atirar(Robo robo, int nTiros, Ambiente ambiente) {
        if (this.municao < nTiros) {
            return "Munição insuficiente";
        } else {
            String result = this.atirar(robo.getX(), robo.getY(), robo.getZ(), nTiros, ambiente);
            return result;
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
                                    "Obstáculo %s foi atingido!\n" +
                                    defesa,
                            aX, aY, aZ, alvodef.getTipoObstaculo());
                    return result;
                }
            }

        } else {
            return "Alvo fora do alcance";
        }
    }
}