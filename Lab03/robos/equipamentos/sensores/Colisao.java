package robos.equipamentos.sensores;
import java.util.ArrayList;
import robos.terrestres.RoboTerrestre;
import robos.geral.Robo;
import ambiente.Ambiente;
import ambiente.Obstaculo;

public class Colisao extends Sensor<Boolean> {
    private RoboTerrestre robo;
    private Ambiente ambiente;

    public Colisao(RoboTerrestre robo, Ambiente ambiente) {
        super();
        this.robo = robo;
        this.ambiente = ambiente;
    }

    public Boolean acionar() {
        int posX = this.robo.getPosicaoXInterna();
        int posY = this.robo.getPosicaoYInterna();
        
        ArrayList<Robo> listaRobos = ambiente.getListaRobos();
        ArrayList<Obstaculo> listaObstaculos = ambiente.getListaObstaculos();
        for (Robo colRobo : listaRobos) {
            if (colRobo != this.robo && colRobo.getPosicaoXInterna() == posX && colRobo.getPosicaoY() == posY) {
                return true; // Colisão com outro robô
            }
        }
        for (Obstaculo obstaculo : listaObstaculos) {
            if (obstaculo.getX1() <= this.robo.getPosicaoX() && obstaculo.getX2() >= posX && obstaculo.getY1() <= posY && obstaculo.getY2() >= posY) {
                   return true; // Colisão com obstáculo
            }
        }

        return false;
    }

}
