package ambiente;
import java.util.ArrayList;

import interfaces.Comunicavel;
import robos.geral.*;

/**
 * Classe responsável por gerenciar a comunicação entre robôs no ambiente.
 * Armazena e controla o histórico de mensagens trocadas entre entidades comunicáveis.
 */
public class CentralComunicacao {
    private ArrayList<String> mensagens;

    /**
     * Construtor que inicializa a central de comunicação com uma lista vazia de mensagens
     */
    public CentralComunicacao(){
        mensagens = new ArrayList<String>();
    }

    /**
     * Exibe todo o histórico de mensagens armazenadas na central
     * @return String formatada contendo todas as mensagens do histórico
     */
    public String exibirMensagens(){
        String texto = "Histórico de Comunicação:\n";
        for (String msg : mensagens){
            texto += msg;
        }
        texto+="Fim do Histórico de Comunicação";
        return texto;
    }

    /**
     * Registra uma nova mensagem no histórico da central de comunicação
     * @param remetente O robô que está enviando a mensagem
     * @param destinatario O robô que receberá a mensagem
     * @param msg O conteúdo da mensagem a ser enviada
     */
    public void registrarMensagem(Comunicavel remetente, Comunicavel destinatario, String msg){
        String envio = String.format("(De: %s | Para: %s): %s\n", ((Robo)remetente).getNome(), ((Robo)destinatario).getNome(), msg);
        mensagens.add(envio);
    }
}
