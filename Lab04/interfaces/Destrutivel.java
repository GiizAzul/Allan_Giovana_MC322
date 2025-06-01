package interfaces;

import ambiente.Ambiente;

/**
 * Interface que define as capacidades de defesa para entidades que podem ser destruídas.
 * Implementada por obstáculos e robôs que podem receber dano e ser removidos do ambiente.
 */
public interface Destrutivel {
    
    /**
     * Processa o dano recebido e retorna o resultado da defesa
     * @param dano Quantidade de dano a ser aplicada à entidade
     * @param ambiente Referência ao ambiente para possível remoção da entidade se destruída
     * @return String descrevendo o resultado da defesa (resistiu, foi destruída, etc.)
     */
    String defender(int dano, Ambiente ambiente);
}
