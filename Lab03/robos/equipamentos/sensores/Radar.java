package robos.equipamentos.sensores;

import java.util.ArrayList;
import ambiente.Ambiente;
import ambiente.Obstaculo;
import robos.geral.Robo;
import robos.aereos.RoboAereo;

public class Radar extends Sensor<ArrayList<Object>> {
    private RoboAereo robo;
    private Ambiente ambiente;
    private int raioAlcance;
    private int anguloAlcance;

    public Radar(RoboAereo roboAir, Ambiente ambienteSensor, int raioAlcance, int anguloAlcance) {
        super();
        this.raioAlcance = raioAlcance;
        this.anguloAlcance = anguloAlcance;
        this.ambiente = ambienteSensor;
        this.robo = roboAir;
    }


    public ArrayList<Object> acionar() {
        ArrayList<Obstaculo> obstaculosAmbiente = ambiente.getListaObstaculos();
        ArrayList<Obstaculo> obstaculosEncontrados = new ArrayList<Obstaculo>();
        
        for (Obstaculo obstaculo : obstaculosAmbiente) {
            if (verificacaoObstaculo(obstaculo)) {
                obstaculosEncontrados.add(obstaculo);
            }
        }

        ArrayList<Robo> robosAmbiente = ambiente.getListaRobos();
        ArrayList<Robo> robosEncontrados = new ArrayList<Robo>();
        for (Robo roboAmbiente : robosAmbiente) {
            if (verificacaoRobo(roboAmbiente)) {
                robosEncontrados.add(roboAmbiente);
            }
        }

        ArrayList<Object> resultado = new ArrayList<Object>();
        resultado.addAll(obstaculosEncontrados);
        resultado.addAll(robosEncontrados);
        
        return resultado;
    }

    public boolean verificacaoObstaculo(Obstaculo obstaculo) {
        float xObstaculo = (obstaculo.getX1() + obstaculo.getX2()) / 2;
        float yObstaculo = (obstaculo.getY1() + obstaculo.getY2()) / 2;
        float zObstaculo = 0; // Por padrão
        if (this.getDistanciaPontoRadar3D(xObstaculo, yObstaculo, zObstaculo) > getAlcanceCorrigido(obstaculo)) {
            return false; // Não encontrável
        }

        return nucleoComumVerificacao(xObstaculo, yObstaculo, zObstaculo);

    }

    public boolean verificacaoRobo(Robo roboAmbiente) {
        int xRoboAmbiente = roboAmbiente.getPosicaoXInterna();
        int yRoboAmbiente = roboAmbiente.getPosicaoYInterna();
        int zRoboAmbiente = 0;
        if (roboAmbiente instanceof RoboAereo) {
            zRoboAmbiente = ((RoboAereo) roboAmbiente).getAltitude();
        } 

        if (!roboAmbiente.getVisivel()) {
            return false;
        }

        if (this.getDistanciaPontoRadar3D(xRoboAmbiente, yRoboAmbiente, zRoboAmbiente) > getAlcanceCorrigido(roboAmbiente)) {
            return false; // Não encontrável
        }

        return nucleoComumVerificacao(xRoboAmbiente, yRoboAmbiente, zRoboAmbiente);

    }

    private boolean nucleoComumVerificacao(float x, float y, float z) {
        float anguloRotacao = 0;

        switch (this.robo.getDirecao()) {
            case "Norte":
                anguloRotacao = -90f;
                break;
            case "Sul":
                anguloRotacao = -270f;
                break;
            case "Leste":
                anguloRotacao = 0f;
                break;
            case "Oeste":
                anguloRotacao = -180f;
                break;
            default:
                break;
        }

        double[] pontoRotacionado = rotacionarPonto(x, y, anguloRotacao);
        double r = this.getDistanciaPontoRadar2D(pontoRotacionado[0], pontoRotacionado[1]);
        double anguloObjeto = Math.atan((this.robo.getAltitude() - z) / (r));
        if (Math.abs(anguloObjeto) > this.anguloAlcance) {
            return false;
        }

        return true;
    }

    private double[] rotacionarPonto(float xP, float yP, float angulo) {
        double radiano = Math.toRadians(angulo);
        double xRotacionado = (xP * Math.cos(radiano)) - (yP * Math.sin(radiano));
        double yRotacionado = (xP * Math.sin(radiano)) + (yP * Math.cos(radiano));

        return new double[] {xRotacionado, yRotacionado};
    }

    public double getDistanciaPontoRadar2D(double x, double y) {
        return Math.sqrt(Math.pow(x - robo.getPosicaoX(), 2) + Math.pow(y - robo.getPosicaoY(), 2));
    }

    public double getDistanciaPontoRadar3D(double x, double y, double z) {
        return Math.sqrt(Math.pow(x - robo.getPosicaoX(), 2) + Math.pow(y - robo.getPosicaoY(), 2) + Math.pow(z - robo.getAltitude(), 2));
    }

    public double getAlcanceCorrigido(Obstaculo obstaculo) {
        return this.raioAlcance * obstaculo.getTipo().getFatorReducaoAlcanceRadar();
    }

    public double getAlcanceCorrigido(Robo robo) {
        return this.raioAlcance * robo.getMateriaisRobo().getFatorReducaoAlcanceRadar();
    }

    public int getRaioAlcance() {
        return raioAlcance;
    }

    public void setRaioAlcance(int alcanceRadar) {
        this.raioAlcance = alcanceRadar;
    }

    public int getAnguloAlcance() {
        return anguloAlcance;
    }

    public void setAnguloAlcance(int anguloAlcance) {
        this.anguloAlcance = anguloAlcance;
    }

}
