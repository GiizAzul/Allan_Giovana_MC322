package robos.aereos;

import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import interfaces.Atacante;
import interfaces.Entidade;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import excecoes.*;
import excecoes.sensor.SensorInativoException;

public class DroneAtaque extends RoboAereo implements Atacante {

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
    public String defender(int dano, Ambiente ambiente) {
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

    public String executarTarefa(Object... argumentos) throws AlvoInvalidoException, MunicaoInsuficienteException, SensorInativoException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
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
                try {
                    return atirar(alvoX, alvoY, alvoZ, nTiros, ambiente);
                } catch (SensorInativoException | MunicaoInsuficienteException | AlvoInvalidoException e) {
                    return e.getMessage();
                }

            case "atirar robo":
                Robo robo = (Robo) argumentos[1];
                nTiros = (Integer) argumentos[2];
                ambiente = (Ambiente) argumentos[3];
                try {
                    return atirar(robo, nTiros, ambiente);
                } catch (SensorInativoException | MunicaoInsuficienteException | AlvoInvalidoException e) {
                    return e.getMessage();
                }

            case "identificar":
                ArrayList<Obstaculo> listaoObstaculos = identificarObstaculo();
                ArrayList<Robo> listaoRobos = identificarRobo();
                if (listaoObstaculos.isEmpty() && listaoRobos.isEmpty()) {
                    return "Nenhum objeto encontrado!";
                } else {
                    for (Obstaculo o : listaoObstaculos) {
                        result += String.format(
                                "Obstáculo encontrado: %s, X1: %d, X2: %d, Y1: %d, Y2: %d, Altura: %d\n",
                                o.getTipoObstaculo(), o.getX1(), o.getX2(), o.getY1(), o.getY2(),
                                o.getAltura());
                    }
                    for (Robo r : listaoRobos) {
                        result += String.format("Robô encontrado: %s, X: %d, Y: %d, Z: %d\n",
                                r.getNome(), r.getXInterno(), r.getYInterno(), r.getZInterno());
                    }
                }
                return result;

            default:
            // TO DO: Tarefa Exception
                return "";
        }

    }

    public String atirar(int alvoX, int alvoY, int alvoZ, int nTiros, Ambiente ambiente) throws SensorInativoException, MunicaoInsuficienteException, AlvoInvalidoException {

        verificarGPSAtivo();

        if (this.municao < nTiros) {
            throw new MunicaoInsuficienteException();
        } 
        if (alvoX == getXInterno() && alvoY == getYInterno() && alvoZ == getZInterno()) {
            throw new AlvoInvalidoException("Não é possível atirar no próprio robô");
        } 

        int dX = this.getX() - alvoX;
        int dY = this.getY() - alvoY;
        int dZ = this.getZ() - alvoZ;

        return executarTiro(dX, dY, dZ, alvoX, alvoY, alvoZ, nTiros, ambiente);

    }

    private String atirar(Robo robo, int nTiros, Ambiente ambiente)
    throws SensorInativoException, MunicaoInsuficienteException, AlvoInvalidoException {
        verificarGPSAtivo();
        if (this.municao < nTiros) {
            throw new MunicaoInsuficienteException();
        }

        return this.atirar(robo.getX(), robo.getY(), robo.getZ(), nTiros, ambiente);
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
                    String defesa = alvodef.defender(nTiros, ambiente);
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

    public String recarregar(int nBalas) {
        municao += nBalas;
        return "Recarregamento concluido";
    }

}