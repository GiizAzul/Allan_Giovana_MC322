package interfaces;

import excecoes.*;
import robos.geral.Robo;

public interface Comunicavel {
    void enviarMensagem(Comunicavel destinatario, String mensagem) throws ErroComunicacaoException, RoboDesligadoException;

    void receberMensagem(Comunicavel remetente, String mensagem) throws ErroComunicacaoException, RoboDesligadoException;

}
