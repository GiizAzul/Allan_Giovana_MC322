package robos.missao;

// No pacote de missoes
import ambiente.Ambiente;
import robos.geral.Robo;
import robos.terrestres.Correios;

public class MissaoEntregarPacote implements Missao {
    private String idPacote;
    private int destinoX;
    private int destinoY;
    private float peso;

    public MissaoEntregarPacote(String idPacote, int destinoX, int destinoY, float peso) {
        this.idPacote = idPacote;
        this.destinoX = destinoX;
        this.destinoY = destinoY;
        this.peso = peso;
    }

    @Override
    public void executar(Robo robo, Ambiente ambiente) {
        if (robo instanceof Correios) {
            Correios correio = (Correios) robo;
            System.out.println("Iniciando missão: Entregar pacote " + idPacote);
            try {
                correio.executarTarefa("carregar", idPacote, peso);
                System.out.println(correio.executarTarefa("entregar", idPacote, destinoX, destinoY, ambiente));
                System.out.println("Missão de entrega concluída.");
            } catch (Exception e) {
                System.err.println("Falha na missão de entrega: " + e.getMessage());
            }
        }
    }
}