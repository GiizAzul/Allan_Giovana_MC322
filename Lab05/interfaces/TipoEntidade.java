package interfaces;

/**
 * Enum que define os diferentes tipos de entidades que podem existir no ambiente.
 * Cada tipo possui uma representação visual específica para exibição no mapa.
 */
public enum TipoEntidade{
    VAZIO("__"),        // Representa uma posição vazia no ambiente
    ROBO("🤖"),         // Representa um robô no ambiente
    OBSTACULO("O"),     // Representa um obstáculo no ambiente
    DESCONHECIDO("?");  // Representa uma entidade não identificada

    private final String representacao;

    /**
     * Construtor do enum que define a representação visual de cada tipo de entidade
     * @param representacao String que representa visualmente o tipo de entidade no mapa
     */
    TipoEntidade(String representacao){
        this.representacao = representacao;
    }

    /**
     * Retorna a representação visual da entidade
     * @return String contendo o símbolo que representa a entidade no mapa
     */
    public String getRepresentacao(){
        return representacao;
    }
}