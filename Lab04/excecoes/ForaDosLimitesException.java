package excecoes;

public class ForaDosLimitesException extends Exception {

    public ForaDosLimitesException() {
        super("Robô fora dos limites do ambiente.");
    }

    public ForaDosLimitesException(String mensagem) {
        super(mensagem);
    }
}
