package excecoes.robos.especificos;

public class VarreduraInvalidaException extends Exception {

    public VarreduraInvalidaException() {
        super("Varredura inválida: parâmetros incorretos ou área fora dos limites.");
    }

    public VarreduraInvalidaException(String mensagem) {
        super(mensagem);
    }
    
}
