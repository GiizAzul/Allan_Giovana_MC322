/**
 * Classe abstrata genérica que representa um sensor básico para robôs.
 * Cada tipo de sensor específico pode retornar diferentes tipos de dados.
 * 
 * @param <T> O tipo de dado retornado pelo sensor ao ser acionado
 */
abstract class Sensor<T> {
    /** Indica se o sensor está ativo ou não */
    private boolean ativo;

    /**
     * Construtor padrão. Inicializa o sensor como ativo.
     */
    Sensor() { 
        this.ativo = true;
    }

    /**
     * Ativa o sensor para que possa ser acionado e retornar dados.
     */
    void ativar() { 
        this.ativo = true;
    }

    /**
     * Desativa o sensor, impedindo acionamento.
     */
    void desativar() { 
        this.ativo = false;
    }

    /**
     * Verifica se o sensor está ativo ou não.
     * 
     * @return true se o sensor estiver ativo, false caso contrário
     */
    boolean isAtivo() { 
        return this.ativo;
    }

    /**
     * Realiza o acionamento do sensor e retorna dados do tipo especificado.
     * Cada subclasse deve implementar este método de acordo com seu tipo.
     * 
     * @return Dados coletados pelo sensor
     */
    abstract T acionar();
}
