package interfaces;

public enum TipoEntidade{
    VAZIO('.'),
    ROBO('#'),
    OBSTACULO('*'),
    DESCONHECIDO('?');

    private final char representacao;

    TipoEntidade(char representacao){
        this.representacao=representacao;
    }

    public Character getRepresentacao(){
        return representacao;
    }
}