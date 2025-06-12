package robos.geral;

import ambiente.Ambiente;
import robos.missao.Missao;
import robos.subsistemas.movimento.ControleMovimento;

public abstract class AgenteInteligente extends Robo {
    protected Missao missao;

    /**
     * Construtor da classe AgenteInteligente
     * @param nome
     * @param direcao
     * @param material
     * @param posicaoX
     * @param posicaoY
     * @param posicaoZ
     * @param velocidade
     * @param controleMovimento - Interface que compila todos os formatos de movimentação dos Robos
     */

    public AgenteInteligente(String nome, String direcao, Ambiente ambiente, MateriaisRobo material, int posicaoX, int posicaoY,
            int posicaoZ, int velocidade, ControleMovimento controleMovimento) {
        super(nome, direcao, ambiente, material, posicaoX, posicaoY, posicaoZ, velocidade, controleMovimento);

    }

    public void definirMissao(Missao m) {
        this.missao = m;
    }

    public boolean temMissao() {
        return missao != null;
    }

    public abstract String executarMissao(Ambiente a);

}
