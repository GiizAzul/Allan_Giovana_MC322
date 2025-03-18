public class DroneVigilancia extends RoboAereo {

    private boolean camuflado;
    private boolean em_vigia;
    private float resolucao_filmagem;
    private float resolucao_minima;
    private float angulo_abertura;


    public DroneVigilancia(String nome, String direcao, int posicaoX, int posicaoY, int altitude, int altitudeMaxima, int qtdMunicao, String tipoArmamento, int autonomia, int alcance, boolean camuflado, boolean em_vigia, float resolucao_filmagem, float resolucao_minima, float angulo_abertura) {
        super(nome, direcao, posicaoX, posicaoY, altitude, altitudeMaxima);
        
        this.camuflado = camuflado;
        this.resolucao_filmagem = resolucao_filmagem;
        this.resolucao_minima = resolucao_minima;
        this.angulo_abertura = angulo_abertura;
        this.em_vigia = em_vigia;
    }

    public String vigiar_area(int centroX, int centroY, int raio) {
        if (this.em_vigia) {
            return "Drone já está em vigilância";
        }

        return "Vigiando área:";
    }

    public void parar_vigia() {
        this.em_vigia = false;
    }

    public void acionar_camuflagem() {
        this.camuflado = true;
    }

    public void desabilitar_camuflagem() {
        this.camuflado = false;
    }
}