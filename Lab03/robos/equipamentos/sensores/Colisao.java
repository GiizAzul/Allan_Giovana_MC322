package robos.equipamentos.sensores;
import java.util.ArrayList;
import robos.terrestres.RoboTerrestre;
import robos.geral.Robo;
import ambiente.Ambiente;
import ambiente.Obstaculo;

public class Colisao extends Sensor<Integer> {
    private RoboTerrestre robo;
    private Robo ultimoRoboColidido;
    private Obstaculo ultimoObstaculoColidido;
    private Ambiente ambiente;

    public Colisao(RoboTerrestre robo, Ambiente ambiente) {
        super();
        this.robo = robo;
        this.ambiente = ambiente;
    }

    public Integer acionar() {
        int posX = this.robo.getPosicaoXInterna();
        int posY = this.robo.getPosicaoYInterna();
        
        ArrayList<Robo> listaRobos = ambiente.getListaRobos();
        ArrayList<Obstaculo> listaObstaculos = ambiente.getListaObstaculos();
        for (Robo colRobo : listaRobos) {
            if (colRobo != this.robo && colRobo.getPosicaoXInterna() == posX && colRobo.getPosicaoY() == posY) {
                this.ultimoRoboColidido = colRobo;
                return 1; // Colisão com outro robô
            }
        }
        for (Obstaculo obstaculo : listaObstaculos) {
            if (obstaculo.getX1() <= this.robo.getPosicaoX() && obstaculo.getX2() >= posX && obstaculo.getY1() <= posY && obstaculo.getY2() >= posY) {
                this.ultimoObstaculoColidido = obstaculo;
                return 2; // Colisão com obstáculo
            }
        }

        return 0; // Sem colisão
    }

    public Robo getUltimoRoboColidido() {
        return ultimoRoboColidido;
    }
    
    public Obstaculo getUltimoObstaculoColidido() {
        return ultimoObstaculoColidido;
    }

}
