package robos.aereos;
import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import excecoes.robos.gerais.ColisaoException;
import excecoes.robos.gerais.MovimentoInvalidoException;
import excecoes.sensor.*;
import interfaces.*;
import robos.equipamentos.sensores.*;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;


/**
 * Classe que representa um robô aéreo, especialização de Robo
 * com capacidade de voar em diferentes altitudes
 */
public class RoboAereo extends Robo implements Identificantes {

    private int altitudeMaxima = -1;  // Altitude máxima que o robô pode atingir
    private Barometro sensorBarometro;      // Sensor de pressão atmosférica
    private Radar sensorRadar; // Sensor de radar para detecção de objetos

    /**
     * Construtor de RoboAereo
     * 
     * @param n    Nome do Robô
     * @param d    Direção inicial
     * @param x    Posição X inicial
     * @param y    Posição Y inicial
     * @param h    Altura inicial
     * @param hmax Altura máxima permitida
     */
    public RoboAereo(String n, String d, MateriaisRobo m, int x, int y, int vel, int h, int hmax, Ambiente ambiente) {
        super(n, d, m, x, y, h, vel);
        this.altitudeMaxima = hmax;
        this.sensorBarometro = new Barometro(this);
        this.sensorRadar = new Radar(this, ambiente, 100, 30);
        this.addSensor(sensorBarometro);
        this.addSensor(sensorRadar);
    }

    public RoboAereo(String n, String d, MateriaisRobo m, int x, int y, int vel, int h, int hmax, Ambiente ambiente, float alc_radar, float ang_radar) {
        super(n, d, m, x, y, h, vel);
        this.altitudeMaxima = hmax;
        this.sensorBarometro = new Barometro(this);
        this.sensorRadar = new Radar(this, ambiente, alc_radar, ang_radar);
        this.addSensor(sensorBarometro);
        this.addSensor(sensorRadar);
    }
    
    /**
     * Realiza o movimento vertical (eixo Z) do robô aéreo.
     * 
     * @param passoZ   O incremento (positivo) ou decremento (negativo) a ser
     *                 aplicado em cada passo do movimento.
     * @param metros   A distância total a ser percorrida. O valor é subtraído da
     *                 altitude atual para calcular a altitude alvo.
     * @param ambiente O ambiente em que o robô está se movendo, usado para
     *                 verificar colisões.
     * 
     *                 O método move o robô verticalmente, verificando:
     *                 - Se a altitude máxima foi atingida (limitando o movimento
     *                 quando necessário)
     *                 - Se há colisões com outros objetos (robôs ou obstáculos)
     * 
     *                 O movimento é interrompido se houver colisão ou se a altitude
     *                 máxima for atingida.
     */
    private void movimentoZ(int passo, int metros, Ambiente ambiente) throws SensorException, MovimentoInvalidoException, ColisaoException {
        // Calcula a altitude alvo baseada na direção do movimento
        int altitudeAlvo = passo > 0 ? getZ() + metros : getZ() - metros;
        
        // Impede que a altitude alvo seja negativa (abaixo do solo)
        if (altitudeAlvo < 0) {
            throw new MovimentoInvalidoException("Não é possível descer abaixo do nível do solo.");
        }
        
        // Impede que a altitude alvo exceda a altitude máxima
        if (altitudeAlvo > this.altitudeMaxima) {
            throw new MovimentoInvalidoException("Altura máxima permitida é " + this.altitudeMaxima + " metros.");
        }
        
        // Realiza o movimento passo a passo
        for (int z = getZ() + passo; z != altitudeAlvo + passo; z += passo) {
            Object obj = ambiente.identificarEntidadePosicao(getX(), getY(), z);
            if (obj != null) {
                if (obj instanceof Robo) {
                    throw new ColisaoException(((Robo) obj).getNome() + " na posição X:" + getX() +
                            " Y:" + getY() + " Z:" + z);
                } else {
                    throw new ColisaoException(((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + getX() +
                            " Y:" + getY() + " Z:" + z);
                }
            }
            ambiente.moverEntidade(this, getX(), getY(), z);

            setPosicaoZ(z);
        }
    }

    /**
     * Aumenta a altitude do robô aéreo
     * @param metros Quantidade de metros a subir
     * @param ambiente Ambiente onde o robô se encontra
     */
    public void subir(int metros, Ambiente ambiente) throws SensorException, ColisaoException, MovimentoInvalidoException {
        this.movimentoZ(1, metros, ambiente);
    }

    /**
     * Diminui a altitude do robô aéreo
     * @param metros Quantidade de metros a descer
     */
    public void descer(int metros, Ambiente ambiente) throws SensorException, ColisaoException, MovimentoInvalidoException {
        this.movimentoZ(-1, metros, ambiente);
    }

    /**
     * Obtém a altitude máxima do robô
     * @return Altitude máxima em metros
     */
    public int getAltitudeMaxima() {
        return this.altitudeMaxima;
    }

    public double getPressao() throws SensorException {
        return this.sensorBarometro.acionar();
    }

    /**
     * Define uma nova altitude máxima
     * @param hMax Nova altitude máxima
     */
    public void setAltitudeMaxima(int hMax) {
        this.altitudeMaxima = hMax;
    }

    /**
     * Calcula a distância entre este robô aéreo e outro robô aéreo
     * @param alvo Robô aéreo alvo
     * @return Distância euclidiana 3D (considerando altitudes)
     */
    public double distanciaRobo(Robo alvo) throws SensorException {
        verificarGPSAtivo();

        return Math.sqrt(Math.pow(alvo.getXInterno() - this.getX(), 2)
                + Math.pow(alvo.getYInterno() - this.getY(), 2)
                + Math.pow(alvo.getZ() - this.getZ(), 2));
    }

    /**
     * Move o robô aéreo para uma nova posição 3D
     * @param X Nova coordenada X
     * @param Y Nova coordenada Y
     * @param Z Nova altitude
     * @param ambiente Ambiente onde o robô se encontra
     */
    public void mover(int X, int Y, int Z, Ambiente ambiente) throws SensorException, ColisaoException, MovimentoInvalidoException {
        int deltaX = X - getX();
        int deltaY = Y - getY();

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            for (int x = getX() + passoX; x != X + passoX; x += passoX) {
                Object obj = ambiente.identificarEntidadePosicao(x, getY(),getZ());
                if (obj != null) {
                    if (obj instanceof RoboAereo) {
                        throw new ColisaoException("O robô " + getNome() + " colidiu com o objeto: " + 
                                ((RoboAereo) obj).getNome() + " na posição X:" + x + " Y:" + getY() + " Z:" + getZ());
                    } else if (((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        ambiente.removerEntidade(this);
                        throw new ColisaoException("O robô " + getNome() + " colidiu com o objeto: " + ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + x + " Y:" + getY() + "sendo destruído no processo!");
                    } else {
                        throw new ColisaoException("O robô " + getNome() + " colidiu com o objeto:" +
                                ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + x + " Y:" + getY() + " Z:" + getZ());
                    }
                }
                ambiente.moverEntidade(this, x, getY(), getZ());
                setPosicaoX(x);
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = getY() + passoY; y != Y + passoY; y += passoY) {
                Object obj = ambiente.identificarEntidadePosicao(getX(), y, getZ());

                if (obj != null) {
                    if (obj instanceof RoboAereo) {
                        throw new ColisaoException("O robô " + getNome() + " colidiu com o objeto: " + 
                                ((RoboAereo) obj).getNome() + " na posição X:" + getX() + " Y:" + y + " Z:" + getZ());
                    } else if (((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        ambiente.removerEntidade(this);
                        throw new ColisaoException("O robô " + getNome() + " colidiu com o objeto: " + ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + getX() + " Y:" + y + "sendo destruído no processo!");
                    } else {
                        throw new ColisaoException("O robô " + getNome() + " colidiu com o objeto:" +
                                ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + getX() + " Y:" + y + " Z:" + getZ());
                    }
                }

                ambiente.moverEntidade(this, getX(), y, getZ());
                setPosicaoY(y);
            }
        }

        
        int h = this.getZ();
        if (Z > h) {
            this.subir(Z - h, ambiente);
        } else {
            this.descer(h - Z, ambiente);
        }
    }

    /**
     * Sobrescreve o método da classe pai para identificar Robôs
     * @return Lista de robôs que são encontrados por meio do sensor de Radar
     */
    @Override
    public ArrayList<Robo> identificarRobo() throws SensorException {
        ArrayList<Entidade> objetosEncontrados = this.sensorRadar.acionar();
        ArrayList<Robo> robosEncontrados = new ArrayList<Robo>();
        for (Entidade objectEnc : objetosEncontrados) {
            if (objectEnc.getTipo() == TipoEntidade.ROBO) {
                robosEncontrados.add((Robo) objectEnc);
            }
        }
        return robosEncontrados;
    }

    public ArrayList<Obstaculo> identificarObstaculo() throws SensorException {
        ArrayList<Entidade> objetosEncontrados = this.sensorRadar.acionar();
        ArrayList<Obstaculo> obstaculosEncontrados = new ArrayList<Obstaculo>();
        for (Entidade objectEnc : objetosEncontrados) {
            if (objectEnc.getTipo() == TipoEntidade.OBSTACULO) {
                obstaculosEncontrados.add((Obstaculo) objectEnc);
            }
        }
        return obstaculosEncontrados;
    }

    public Radar getRadar() {
        return this.sensorRadar;
    }

    public Barometro getBarometro() {
        return this.sensorBarometro;
    }

    public String executarTarefa(Object... argumentos) {
        String result = super.executarTarefa(argumentos);
        if (result != ""){
            return result;
        }
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "mover":
                int x = (Integer) argumentos[1];
                int y = (Integer) argumentos[2];
                int z = (Integer) argumentos[3];
                Ambiente ambiente = (Ambiente) argumentos[4];
                try {
                    mover(x, y, z, ambiente);
                } catch (SensorException | ColisaoException | MovimentoInvalidoException e) {
                    return "Erro ao mover o robô: " + e.getMessage();
                }
                return "";
                
            case "identificar":
                ArrayList<Obstaculo> listaoObstaculos;
                ArrayList<Robo> listaoRobos;
                try {
                    listaoObstaculos = identificarObstaculo();
                    listaoRobos = identificarRobo();
                } catch (SensorException e) {
                    return "Erro ao identificar objetos: " + e.getMessage();
                }

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
                return "";
        }
    }

}
