package robos.subsistemas;

import excecoes.ambiente.ErroComunicacaoException;
import interfaces.Comunicavel;

public interface ModuloComunicacao {
    void enviarMensagem(String mensagem, Comunicavel destinatario) throws ErroComunicacaoException;
    String receberMensagem() throws ErroComunicacaoException;
}
