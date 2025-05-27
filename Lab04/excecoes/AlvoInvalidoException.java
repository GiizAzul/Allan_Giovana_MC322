package excecoes;

public class AlvoInvalidoException extends Exception {
    public AlvoInvalidoException() {
        super("Alvo inválido para o disparo");
    }
    
    public AlvoInvalidoException(String message) {
        super(message);
    }
}
