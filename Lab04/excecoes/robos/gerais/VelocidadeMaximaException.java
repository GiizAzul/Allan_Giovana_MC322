package excecoes.robos.gerais;

public class VelocidadeMaximaException extends Exception {
    public VelocidadeMaximaException() {
        super("A velocidade do robô ultrapassa a velocidade máxima permitida.");
    }

    public VelocidadeMaximaException(String mensagem) {
        super(mensagem);
    }
}
