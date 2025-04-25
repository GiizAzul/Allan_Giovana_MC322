package robos.aereos;

import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import robos.Robo;
import robos.terrestres.RoboTerrestre;

public class DroneVigilancia extends RoboAereo {

    private boolean camuflado;
    private float alcance_radar;
    private float angulo_camera;

    public DroneVigilancia(String nome, String dir, int x, int y, int h, int hmax, float alc_rad, float ang_cam) {
        super(nome, dir, x, y, h, hmax);
        this.alcance_radar = alc_rad;
        this.angulo_camera = ang_cam;
        this.camuflado = false;
    }

    public ArrayList<Robo> identificarRobo(Ambiente ambiente) {
        // Busca todos os robos que o Radar consegue alcançar com base na posição do
        // Drone
        // Sistema de Radar encontra até Drones camuflados
        // Baseado no alcance do radar do robô

        ArrayList<Robo> robos_ambiente = ambiente.getListaRobos();
        ArrayList<Robo> encontrados = new ArrayList<>();

        for (Robo robo : robos_ambiente) {
            if (robo.distanciaRobo(this) < this.alcance_radar) {
                encontrados.add(robo);
            }
        }

        return encontrados;

    }

    public ArrayList<Obstaculo> identificarObstaculo(Ambiente ambiente) {
        // Busca todos os obstáculos que o Radar consegue alcançar com base na posição
        // do
        // Drone
        // Baseado no alcance do radar do robô

        ArrayList<Obstaculo> obstaculos_ambiente = ambiente.getListaObstaculos();
        ArrayList<Obstaculo> encontrados = new ArrayList<>();

        for (Obstaculo obstaculo : obstaculos_ambiente) {
            if (distanciaObstaculo(obstaculo) < this.alcance_radar) {
                encontrados.add(obstaculo);
            }
        }

        return encontrados;

    }

    public ArrayList<Object> varrerArea(Ambiente ambiente, int centroX, int centroY, int raio) {
        // Sistema de varredura, melhor quanto mais alto está o drone
        // Reposiciona o drone para o centro da varredura
        // Baseado na capacidade da câmera do drone
        // Sistema de varredura não encontra drones camuflados

        // Move o drone para ficar sobre a região central
        this.mover(centroX - this.getPosicaoX(), centroY - this.getPosicaoY(), ambiente);

        // Verifica se a câmera possui abertura para fazer a varredura
        double ang_rad = Math.toRadians(this.angulo_camera);
        if (raio > this.getAltitude() * Math.tan(ang_rad / 2)) {
            System.out.println("Não é possível varrer tal raio com a câmera atual!");
            return new ArrayList<>();
        }

        ArrayList<Robo> lista_robos = ambiente.getListaRobos();
        ArrayList<Obstaculo> lista_obstaculos = ambiente.getListaObstaculos();
        ArrayList<Object> objetos_encontrados = new ArrayList<>();
        for (Robo robo : lista_robos) {
            double distancia = robo.distanciaRobo(this);
            if (distancia > Math.sqrt(Math.pow(raio, 2) + Math.pow(this.getAltitude(), 2))) {
                continue;
            }

            double hMax = this.getAltitude() - distancia * Math.cos(ang_rad);
            if (robo instanceof RoboAereo) {
                RoboAereo roboAereo = (RoboAereo) robo;
                if (roboAereo.getAltitude() > hMax) {
                    // Fora da região de visualização da câmera
                    continue;
                }

            } else if (!(robo instanceof RoboTerrestre)) {
                // RoboTerrestre automaticamente já está na região de varredura
                System.out.printf("Robo %s não avaliado!\n", robo.getNome());
            }

            if (robo.getVisivel()) {
                objetos_encontrados.add(robo);
            }

        }

        for (Obstaculo obstaculo : lista_obstaculos) {
            double distancia = distanciaObstaculo(obstaculo);
            if (distancia > Math.sqrt(Math.pow(raio, 2) + Math.pow(this.getAltitude(), 2))) {
                continue;
            }

            objetos_encontrados.add(obstaculo);
        }

        return objetos_encontrados;

    }

    public boolean isCamuflado() {
        return this.camuflado;
    }

    public void acionarCamuflagem() {
        this.camuflado = true;
        this.visivel = false;
    }

    public void desabilitarCamuflagem() {
        this.visivel = true;
        this.camuflado = false;
    }

    public float getAlcanceRadar() {
        return alcance_radar;
    }

    public void setAlcanceRadar(float alcance_radar) {
        this.alcance_radar = alcance_radar;
    }

    public float getAnguloCamera() {
        return angulo_camera;
    }

    public void setAnguloCamera(float angulo_camera) {
        this.angulo_camera = angulo_camera;
    }
}