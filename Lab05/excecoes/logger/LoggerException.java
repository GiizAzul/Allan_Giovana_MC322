package excecoes.logger;

public class LoggerException extends Exception {
    
    public LoggerException() {
        super("Houve uma falha no sistema de Logger");
    }

    public LoggerException(String mensagem) {
        super(mensagem);
    }

}
