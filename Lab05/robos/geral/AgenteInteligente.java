package robos.geral;

import ambiente.Ambiente;
import robos.missao.Missao;
import robos.subsistemas.movimento.ControleMovimento;

public abstract class AgenteInteligente extends Robo {
    protected Missao missao;

    public void definirMissao(Missao m) {
        this.missao = m;
    }

    public boolean temMissao() {
        return missao != null;
    }

    public abstract void executarMissao(Ambiente a);

    public AgenteInteligente(String nome, String direcao, MateriaisRobo material, int posicaoX, int posicaoY,
            int posicaoZ, int velocidade, ControleMovimento controleMovimento) {
        super(nome, direcao, material, posicaoX, posicaoY, posicaoZ, velocidade, controleMovimento);

    }

}
