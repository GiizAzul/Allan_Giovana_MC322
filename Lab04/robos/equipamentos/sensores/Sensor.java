package robos.equipamentos.sensores;

import excecoes.sensor.*;

/**
 * Classe abstrata genérica que representa um sensor básico para robôs.
 * Cada tipo de sensor específico pode retornar diferentes tipos de dados.
 * 
 * @param <T> O tipo de dado retornado pelo sensor ao ser acionado
 */
public abstract class Sensor<T> {
    /** Indica se o sensor está ativo ou não */
    private boolean ativo;

    /**
     * Construtor padrão. Inicializa o sensor como ativo.
     */
    public Sensor() { 
        this.ativo = true;
    }

    /**
     * Ativa o sensor para que possa ser acionado e retornar dados.
     */
    public void ativar() { 
        this.ativo = true;
    }

    /**
     * Desativa o sensor, impedindo acionamento.
     */
    public void desativar() { 
        this.ativo = false;
    }

    /**
     * Verifica se o sensor está ativo ou não.
     * 
     * @return true se o sensor estiver ativo, false caso contrário
     */
    public boolean isAtivo() { 
        return this.ativo;
    }

    /**
     * Realiza o acionamento do sensor e retorna dados do tipo especificado.
     * Cada subclasse deve implementar este método de acordo com seu tipo.
     * 
     * @return Dados coletados pelo sensor
     */
    abstract T acionar() throws SensorException;
}
