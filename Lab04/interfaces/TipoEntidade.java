package interfaces;

public enum TipoEntidade{
    VAZIO("__"),
    ROBO("🤖"),
    OBSTACULO("O"),
    DESCONHECIDO("?");
    private final String representacao;

    TipoEntidade(String representacao){
        this.representacao=representacao;
    }

    public String getRepresentacao(){
        return representacao;
    }
}