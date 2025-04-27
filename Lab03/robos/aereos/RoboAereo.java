package robos.aereos;
import java.util.ArrayList;
import java.util.Comparator;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.equipamentos.sensores.*;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.terrestres.RoboTerrestre;

/**
 * Classe que representa um robô aéreo, especialização de Robo
 * com capacidade de voar em diferentes altitudes
 */
public class RoboAereo extends Robo {

    private int altitude = -1;        // Altitude atual do robô aéreo
    private int altitudeMaxima = -1;  // Altitude máxima que o robô pode atingir
    private Barometro sensorBarometro;      // Sensor de pressão atmosférica
    private Radar sensorRadar;

    /**
     * Construtor de RoboAereo
     * @param n Nome do Robô
     * @param d Direção inicial
     * @param x Posição X inicial
     * @param y Posição Y inicial
     * @param h Altura inicial
     * @param hmax Altura máxima permitida
     */
    public RoboAereo(String n, String d, MateriaisRobo m, int x, int y, int vel, int h, int hmax, Ambiente ambiente) {
        super(n, d, m, x, y, vel);
        this.altitude = h;
        this.altitudeMaxima = hmax;
        this.sensorBarometro = new Barometro(this);
        this.sensorRadar = new Radar(this, ambiente, 100, 30);
        this.addSensor(sensorBarometro);
        this.addSensor(sensorRadar);
    }

    public RoboAereo(String n, String d, MateriaisRobo m, int x, int y, int vel, int h, int hmax, Ambiente ambiente, int alc_radar, int ang_radar) {
        super(n, d, m, x, y, vel);
        this.altitude = h;
        this.altitudeMaxima = hmax;
        this.sensorBarometro = new Barometro(this);
        this.sensorRadar = new Radar(this, ambiente, alc_radar, ang_radar);
        this.addSensor(sensorBarometro);
        this.addSensor(sensorRadar);
    }
    
    /**
     * Realiza o movimento vertical (eixo Z) do robô aéreo.
     * 
     * @param passoZ O incremento (positivo) ou decremento (negativo) a ser aplicado em cada passo do movimento.
     * @param metros A distância total a ser percorrida. O valor é subtraído da altitude atual para calcular a altitude alvo.
     * @param ambiente O ambiente em que o robô está se movendo, usado para verificar colisões.
     * 
     * O método move o robô verticalmente, verificando:
     * - Se a altitude máxima foi atingida (limitando o movimento quando necessário)
     * - Se há colisões com outros objetos (robôs ou obstáculos)
     * 
     * O movimento é interrompido se houver colisão ou se a altitude máxima for atingida.
     */
    private void movimentoZ(int passo, int metros, Ambiente ambiente) {
        // Calcula a altitude alvo baseada na direção do movimento
        int altitudeAlvo = passo > 0 ? this.altitude + metros : this.altitude - metros;
        
        // Impede que a altitude alvo seja negativa (abaixo do solo)
        if (altitudeAlvo < 0) {
            System.out.println("Não é possível descer abaixo do nível do solo.");
            altitudeAlvo = 0;
        }
        
        // Impede que a altitude alvo exceda a altitude máxima
        if (altitudeAlvo > this.altitudeMaxima) {
            System.out.println("Altura máxima permitida é " + this.altitudeMaxima + " metros.");
            altitudeAlvo = this.altitudeMaxima;
        }
        
        // Realiza o movimento passo a passo
        for (int z = this.altitude + passo; passo > 0 ? z <= altitudeAlvo : z >= altitudeAlvo; z += passo) {
            Object obj = ambiente.identificarObjetoPosicao(getPosicaoX(), getPosicaoY(), z);
            if (obj != null) {
                System.out.print("O robô " + getNome() + " colidiu com o objeto: ");
                if (obj instanceof Robo) {
                    System.out.println(((Robo)obj).getNome() + " na posição X:" + getPosicaoX() + 
                                      " Y:" + getPosicaoY() + " Z:" + z);
                } else {
                    System.out.println(((Obstaculo)obj).getTipo() + " na posição X:" + getPosicaoX() + 
                                      " Y:" + getPosicaoY() + " Z:" + z);
                }
                return; // Interrompe o movimento antes da colisão
            }
            this.altitude = z;
        }
        // Atualiza a altitude final
        this.altitude = altitudeAlvo;
    }

    /**
     * Aumenta a altitude do robô aéreo
     * @param metros Quantidade de metros a subir
     * @param ambiente Ambiente onde o robô se encontra
     */
    public void subir(int metros, Ambiente ambiente) {
        this.movimentoZ(1, metros, ambiente);
    }

    /**
     * Diminui a altitude do robô aéreo
     * @param metros Quantidade de metros a descer
     */
    public void descer(int metros, Ambiente ambiente) {
        this.movimentoZ(-1, metros, ambiente);
    }

    /**
     * Obtém a altitude atual do robô
     * @return Altitude em metros
     */
    public int getAltitude() {
        return this.altitude;
    }

    /**
     * Obtém a altitude máxima do robô
     * @return Altitude máxima em metros
     */
    public int getAltitudeMaxima() {
        return this.altitudeMaxima;
    }

    public double getPressao() {
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
     * Calcula a distância entre este robô aéreo e um robô terrestre
     * @param robo Robô terrestre alvo
     * @return Distância euclidiana 3D
     */
    public double distanciaRobo(RoboTerrestre robo) {
        return Math.sqrt(Math.pow(robo.getPosicaoX() - this.getPosicaoX(), 2)
                + Math.pow(robo.getPosicaoY() - this.getPosicaoY(), 2) + Math.pow(0 - this.getAltitude(), 2));
    }

    /**
     * Calcula a distância entre este robô aéreo e outro robô aéreo
     * @param alvo Robô aéreo alvo
     * @return Distância euclidiana 3D (considerando altitudes)
     */
    public double distanciaRobo(RoboAereo alvo) {
        return Math.sqrt(Math.pow(alvo.getPosicaoX() - this.getPosicaoX(), 2)
                + Math.pow(alvo.getPosicaoY() - this.getPosicaoY(), 2)
                + Math.pow(alvo.getAltitude() - this.getAltitude(), 2));
    }

    /**
     * Move o robô aéreo para uma nova posição 3D
     * @param X Nova coordenada X
     * @param Y Nova coordenada Y
     * @param Z Nova altitude
     * @param ambiente Ambiente onde o robô se encontra
     */
    public void mover(int X, int Y, int Z, Ambiente ambiente) {
        int deltaX = X - getPosicaoX();
        int deltaY = Y - getPosicaoY();

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            for (int x = getPosicaoX() + passoX; x != X + passoX; x += passoX) {
                Object obj = ambiente.identificarObjetoPosicao(x, getPosicaoY(),altitude);
                if (obj != null) {
                    System.out.print("O robô "+getNome()+" colidiu com o objeto: ");
                    if (obj instanceof RoboAereo){
                        System.out.println(((RoboAereo)obj).getNome()+" na posição X:"+x+" Y:"+getPosicaoY()+" Z:"+altitude);
                    }else if(((Obstaculo)obj).getTipo()==TipoObstaculo.BURACO){
                        System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+x+" Y:"+getPosicaoY());
                        System.out.println("O robô "+getNome()+" caiu no buraco e foi destruido");
                        ambiente.removerRobo(this);
                    }else{
                        System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+x+" Y:"+getPosicaoY()+" Z:"+altitude);
                    }
                    return; // Para uma casa antes do obstáculo
                }
                setPosicaoX(x);
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = getPosicaoY() + passoY; y != Y + passoY; y += passoY) {
                Object obj = ambiente.identificarObjetoPosicao(getPosicaoX(), y, altitude);
                if (obj != null) {
                    System.out.print("O robô "+getNome()+" colidiu com o objeto: ");
                    if (obj instanceof Robo){
                        System.out.println(((Robo)obj).getNome()+" na posição X:"+getPosicaoX()+" Y:"+y+" Z:"+altitude);
                    }else if(((Obstaculo)obj).getTipo()==TipoObstaculo.BURACO){
                        System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+getPosicaoX()+" Y:"+y);
                        System.out.println("O robô "+getNome()+" caiu no buraco e foi destruido");
                        ambiente.removerRobo(this);
                    }else{
                        System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+getPosicaoX()+" Y:"+y+" Z:"+altitude);
                    }
                    return; // Para uma casa antes do obstáculo
                }
                setPosicaoY(y);
            }
        }

        
        int h = this.getAltitude();
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
    public ArrayList<Robo> identificarRobo() {
        ArrayList<Object> objetosEncontrados = this.sensorRadar.acionar();
        ArrayList<Robo> robosEncontrados = new ArrayList<Robo>();
        for (Object objectEnc : objetosEncontrados) {
            if (objectEnc instanceof Robo) {
                robosEncontrados.add((Robo) objectEnc);
            }
        }
        return robosEncontrados;
    }

    public ArrayList<Obstaculo> identificarobstaculo() {
        ArrayList<Object> objetosEncontrados = this.sensorRadar.acionar();
        ArrayList<Obstaculo> obstaculosEncontrados = new ArrayList<Obstaculo>();
        for (Object objectEnc : objetosEncontrados) {
            if (objectEnc instanceof Obstaculo) {
                obstaculosEncontrados.add((Obstaculo) objectEnc);
            }
        }
        return obstaculosEncontrados;
    }

    /**
     * Sobrescreve o método da classe pai para exibir posição incluindo altitude
     * @return String formatada com nome e posição 3D do robô
     */
    @Override
    public String exibirPosicao() {
        return String.format("%s está em (%d, %d, %d)", super.getNome(), super.getPosicaoX(), super.getPosicaoY(),
                this.getAltitude());
    }

    public Radar getRadar() {
        return this.sensorRadar;
    }
}
