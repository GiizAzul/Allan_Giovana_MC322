package interfaces;

import ambiente.CentralComunicacao;
import excecoes.*;

public interface Comunicavel {
    String enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central) throws ErroComunicacaoException, RoboDesligadoException;

    String receberMensagem(Comunicavel remetente, String mensagem) throws ErroComunicacaoException, RoboDesligadoException;

}
