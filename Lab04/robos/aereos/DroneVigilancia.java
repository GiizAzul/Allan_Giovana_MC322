package robos.aereos;

import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.terrestres.RoboTerrestre;
import interfaces.*;

public class DroneVigilancia extends RoboAereo {

    private boolean camuflado;
    private float alcance_radar;
    private float angulo_camera;

    public DroneVigilancia(String nome, String dir, MateriaisRobo m, int x, int y, int vel, int h, int hmax, Ambiente amb, int alc_rad, int ang_radar, float ang_cam) {
        super(nome, dir, m, x, y, vel, h, hmax, amb, alc_rad, ang_radar);
        this.angulo_camera = ang_cam;
        this.camuflado = false;
    }

    public ArrayList<Entidade> varrerArea(Ambiente ambiente, int centroX, int centroY, int raio) {
        // Sistema de varredura, melhor quanto mais alto está o drone
        // Reposiciona o drone para o centro da varredura
        // Baseado na capacidade da câmera do drone
        // Sistema de varredura não encontra drones camuflados

        // Move o drone para ficar sobre a região central
        if (!this.getGPS().isAtivo()) {
            System.out.println("GPS não está ativo, não é possível varrer a área!");
            return new ArrayList<>();
        }

        this.mover(centroX - this.getX(), centroY - this.getY(), ambiente);

        // Verifica se a câmera possui abertura para fazer a varredura
        double ang_rad = Math.toRadians(this.angulo_camera);
        if (raio > this.getZ() * Math.tan(ang_rad / 2)) {
            System.out.println("Não é possível varrer tal raio com a câmera atual!");
            return new ArrayList<>();
        }

        ArrayList<Robo> lista_robos = ambiente.getListaRobos();
        ArrayList<Obstaculo> lista_obstaculos = ambiente.getListaObstaculos();
        ArrayList<Entidade> objetos_encontrados = new ArrayList<>();
        for (Robo robo : lista_robos) {
            double distancia = robo.distanciaRobo(this);
            if (distancia > Math.sqrt(Math.pow(raio, 2) + Math.pow(this.getZ(), 2))) {
                continue;
            }

            double hMax = this.getZ() - distancia * Math.cos(ang_rad);
            if (robo instanceof RoboAereo) {
                RoboAereo roboAereo = (RoboAereo) robo;
                if (roboAereo.getZ() > hMax) {
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
            if (distancia > Math.sqrt(Math.pow(raio, 2) + Math.pow(this.getZ(), 2))) {
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