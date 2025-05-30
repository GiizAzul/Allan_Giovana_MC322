package excecoes;

public class MunicaoInsuficienteException extends Exception {
    public MunicaoInsuficienteException() {
        super("Municao insuficiente para realizar o disparo");
    }

    public MunicaoInsuficienteException(String message) {
        super(message);
    }
}
