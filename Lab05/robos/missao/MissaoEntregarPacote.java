package robos.missao;

// No pacote de missoes
import ambiente.Ambiente;
import robos.geral.Robo;
import robos.terrestres.Correios;
import utils.Logger;

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
    public String executar(Robo robo, Ambiente ambiente, Logger logger) {
        if (robo instanceof Correios) {
            logger.ativarLogger();
            Correios correio = (Correios) robo;
            String resultado="MISSÃO: Entregar pacote " + idPacote+"\n";
            try {
                correio.executarTarefa("carregar", idPacote, peso);
                resultado+=correio.executarTarefa("entregar", idPacote, destinoX, destinoY, ambiente);
                resultado+="\nMissão de entrega concluída.";
                logger.escreverLogSucesso(resultado);
            } catch (Exception e) {
                resultado+= "Falha na missão de entrega: " + e.getMessage();
                try{
                    logger.escreverLogFalha(resultado);
                } catch(Exception ex){
                    resultado+="";
                }
            }
            return resultado;
        } else {
            return "Missão EntregarPacote só pode ser executada por um Correio.";
        }
    }
}