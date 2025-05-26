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

    public DroneVigilancia(String nome, String dir, MateriaisRobo m, int x, int y, int vel, int h, int hmax,
            Ambiente amb, float alc_rad, float ang_radar, float ang_cam) {
        super(nome, dir, m, x, y, vel, h, hmax, amb, alc_rad, ang_radar);
        this.angulo_camera = ang_cam;
        this.camuflado = false;
    }

    public String executarTarefa(Object... argumentos) {
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "varrer":
                String result = null;
                Ambiente ambiente = (Ambiente) argumentos[1];
                int centroX = (Integer) argumentos[2];
                int centroY = (Integer) argumentos[3];
                int raio = (Integer) argumentos[4];
                ArrayList<Entidade> objetos_encontrados = varrerArea(ambiente, centroX,
                        centroY, raio);
                if (objetos_encontrados.isEmpty()) {
                    result += "Nenhum objeto foi encontrado!";
                } else {
                    result += "Objetos encontrados:\n";
                    for (Entidade obj : objetos_encontrados) {
                        if (obj instanceof Robo) {
                            Robo r = (Robo) obj;
                            result += String.format("Robô: %s, X: %d, Y: %d, Altura: %s\n",
                                    r.getNome(), r.getX(), r.getY(), r.getZ());
                        } else {
                            Obstaculo o = (Obstaculo) obj;
                            result += String.format("Obstáculo: %s, X1: %d, X2: %d, Y1: %d, Y2: %d, Altura: %d\n",
                                    o.getTipo(), o.getX1(), o.getX2(), o.getY1(), o.getY2(),
                                    o.getAltura());
                        }
                    }
                }
                return result;

            case "camuflagem":
                if (isCamuflado())
                    desabilitarCamuflagem();
                else
                    acionarCamuflagem();
                return null;

            default:
                return null;
        }
    }

    private ArrayList<Entidade> varrerArea(Ambiente ambiente, int centroX, int centroY, int raio) {
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

        ArrayList<Entidade> lista_entidades = ambiente.getEntidades();
        ArrayList<Entidade> objetos_encontrados = new ArrayList<>();
        for (Entidade entidade : lista_entidades) {
            if (entidade.getTipo() == TipoEntidade.ROBO) {
                Robo robo = (Robo) entidade;
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
            } else if (entidade.getTipo() == TipoEntidade.ROBO) {
                Obstaculo obstaculo = (Obstaculo) entidade;
                double distancia = distanciaObstaculo(obstaculo);
                if (distancia > Math.sqrt(Math.pow(raio, 2) + Math.pow(this.getZ(), 2))) {
                    continue;
                }

                objetos_encontrados.add(obstaculo);

            }
        }

        return objetos_encontrados;

    }

    public boolean isCamuflado() {
        return this.camuflado;
    }

    private void acionarCamuflagem() {
        this.camuflado = true;
        this.visivel = false;
    }

    private void desabilitarCamuflagem() {
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