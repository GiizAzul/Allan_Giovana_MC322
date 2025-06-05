package excecoes.robos.gerais;

public class MovimentoInvalidoException extends Exception {

    public MovimentoInvalidoException() {
        super("Não é possível realizar o movimento.");
    }

    public MovimentoInvalidoException(String mensagem) {
        super(mensagem);
    }
    
}
