package ambiente;
import java.util.ArrayList;

import interfaces.Comunicavel;
import robos.geral.*;

public class CentralComunicacao {
    private ArrayList<String> mensagens;

    public CentralComunicacao(){
        mensagens = new ArrayList<String>();
    }

    public String exibirMensagens(){
        String texto = "Histórico de Comunicação:\n";
        for (String msg : mensagens){
            texto+=msg;
        }
        texto+="Fim do Histórico de Comunicação";
        return texto;
    }

    public void registrarMensagem(Comunicavel remetente, Comunicavel destinatario, String msg){
        String envio = String.format("(De: %s | Para: %s): %s\n", ((Robo)remetente).getNome(), ((Robo)destinatario).getNome(), msg);
        mensagens.add(envio);
    }
}
