package excecoes.logger;

public class FalhaEscritaLogException extends Exception {
        
    public FalhaEscritaLogException() {
        super("Não foi possível escrever o log.");
    }

    public FalhaEscritaLogException(String mensagem) {
        super(mensagem);
    }

}
