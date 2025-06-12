package excecoes.logger;

public class ArquivoInvalidoException extends LoggerException {
        
    public ArquivoInvalidoException() {
        super("[ArquivoInvalidoExceptino] O arquivo está inválido!");
    }

    public ArquivoInvalidoException(String mensagem) {
        super("[ArquivoInvalidoExceptino] " + mensagem);
    }

}
