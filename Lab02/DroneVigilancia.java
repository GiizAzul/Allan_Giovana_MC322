import java.util.ArrayList;

public class DroneVigilancia extends RoboAereo {

    private boolean camuflado;
    private float alcance_radar;
    private float angulo_camera;


    public DroneVigilancia(String nome, String dir, int x, int y, int h, int hmax, int muni, boolean camu, float alc_rad, float ang_cam) {
        super(nome, dir, x, y, h, hmax);
        this.alcance_radar = alc_rad;
        this.camuflado = camu;
        this.angulo_camera = ang_cam;
    }

    // Adicionar um override aqui
    public Object[] identificarObstaculos(Ambiente ambiente) {
        ArrayList<Robo> robos_ambiente = ambiente.getListaRobos();

        for (Robo robo : robos_ambiente) {
            if (this.distancia(robo, getPosicaoX(), getPosicaoY()) < this.alcance_radar)            
        }

    }

    public boolean visivel() {
        return this.camuflado;
    }

    public ArrayList<Robo> varrer_area(Ambiente ambiente, int centroX, int centroY, int raio) {

        // Move o drone para ficar sobre a região central
        this.mover(centroX - this.getPosicaoX(), centroY - this.getPosicaoY());

        // Verifica se a câmera possui abertura para fazer a varredura
        double ang_rad = Math.toRadians(this.angulo_camera);
        if (raio > this.getAltitude()*Math.tan(ang_rad / 2)) {
            return new ArrayList<>();
        }

        ArrayList<Robo> lista_robos = ambiente.getListaRobos();
        ArrayList<Robo> robos_encontrados = new ArrayList<>();
        for (Robo robo : lista_robos) {
            if (this.distancia(robo) <= raio) {
                robos_encontrados.add(robo);
            }
        }

        return robos_encontrados;
    }

    private double distancia(RoboTerrestre robo) {
        return Math.sqrt(Math.pow(robo.getPosicaoX() - this.getPosicaoX(), 2) + Math.pow(robo.getPosicaoY() - this.getPosicaoY(), 2));
    }

    private double distancia(RoboAereo alvo) {
        return Math.sqrt(Math.pow(alvo.getPosicaoX() - this.getPosicaoX(), 2) + Math.pow(alvo.getPosicaoY() - this.getPosicaoY(), 2) + Math.pow(alvo.getAltitude() - this.getAltitude(), 2));
    }

    public void acionar_camuflagem() {
        this.camuflado = true;
    }

    public void desabilitar_camuflagem() {
        this.camuflado = false;
    }
}