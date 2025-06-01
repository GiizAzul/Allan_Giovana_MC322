package excecoes.robos.especificos;

public class PacoteNaoEncontrado extends Exception {

    public PacoteNaoEncontrado() {
        super("Pacote não encontrado.");
    }

    public PacoteNaoEncontrado(String mensagem) {
        super(mensagem);
    }
    
}
