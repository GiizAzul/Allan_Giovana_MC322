package excecoes.logger;

public class FalhaEscritaLogException extends LoggerException {
        
    public FalhaEscritaLogException() {
        super("[FalhaEscritaLogException] Não foi possível escrever o log.");
    }

    public FalhaEscritaLogException(String mensagem) {
        super("[FalhaEscritaLogException] " + mensagem);
    }

}
