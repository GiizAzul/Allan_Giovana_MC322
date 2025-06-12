package excecoes.ambiente;

public class ArquivoInvalidoException extends Exception {
        
    public ArquivoInvalidoException() {
        super("O arquivo está inválido!");
    }

    public ArquivoInvalidoException(String mensagem) {
        super(mensagem);
    }

}
