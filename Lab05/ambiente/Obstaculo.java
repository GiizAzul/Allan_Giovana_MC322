package ambiente;

import interfaces.*;

/**
 * Classe que representa um obstáculo no ambiente.
 * Implementa as interfaces Entidade e Destrutivel para permitir interação com robôs.
 */
public class Obstaculo implements Entidade, Destrutivel {
    private final int x1;
    private final int x2;
    private final int y1;
    private final int y2;
    private int altura;
    private final TipoObstaculo tipoObstaculo;
    private final TipoEntidade tipo;
    private int integridade;
    private boolean indestrutivel;

    /**
     * Construtor que cria um obstáculo com tipo e coordenadas específicas
     * @param tipoObstaculo Tipo do obstáculo (define características como altura, integridade)
     * @param x1 Coordenada X inicial do obstáculo
     * @param x2 Coordenada X final do obstáculo
     * @param y1 Coordenada Y inicial do obstáculo
     * @param y2 Coordenada Y final do obstáculo
     */
    public Obstaculo(TipoObstaculo tipoObstaculo, int x1, int x2, int y1, int y2) {
        this.tipoObstaculo = tipoObstaculo;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.altura = tipoObstaculo.getAlturaPadrao();
        this.integridade = tipoObstaculo.getIntegridade();
        this.indestrutivel = tipoObstaculo.isIndestrutivel();
        this.tipo = TipoEntidade.OBSTACULO;
    }

    /**
     * Retorna a coordenada X do obstáculo (sempre 0 para compatibilidade com interface)
     * @return Sempre retorna 0
     */
    public int getX() {
        return 0;
    }

    /**
     * Retorna a coordenada Y do obstáculo (sempre 0 para compatibilidade com interface)
     * @return Sempre retorna 0
     */
    public int getY() {
        return 0;
    }

    /**
     * Retorna a coordenada Z do obstáculo (sempre 0 para compatibilidade com interface)
     * @return Sempre retorna 0
     */
    public int getZ() {
        return 0;
    }

    /**
     * Retorna uma descrição textual completa do obstáculo
     * @return String contendo tipo, posição e altura do obstáculo
     */
    public String getDescricao() {
        return "Obstáculo" + getTipoObstaculo() + "na posição" + "X1: " + getX1() + "X2: " + getX2() + "Y1: " + getY1()
                + "Y2: " + getY2() + "Altura: " + getAltura();
    }

    /**
     * Retorna a representação visual do obstáculo para o mapa
     * @return String com o símbolo que representa o obstáculo
     */
    public String getRepresentacao() {
        return tipoObstaculo.getRepresentacao();
    }

    /**
     * Retorna o tipo da entidade (sempre OBSTACULO)
     * @return TipoEntidade.OBSTACULO
     */
    public TipoEntidade getTipo() {
        return tipo;
    }

    /**
     * Retorna a coordenada X inicial do obstáculo
     * @return Coordenada X1
     */
    public int getX1() {
        return x1;
    }

    /**
     * Retorna a coordenada X final do obstáculo
     * @return Coordenada X2
     */
    public int getX2() {
        return x2;
    }

    /**
     * Retorna a coordenada Y inicial do obstáculo
     * @return Coordenada Y1
     */
    public int getY1() {
        return y1;
    }

    /**
     * Retorna a coordenada Y final do obstáculo
     * @return Coordenada Y2
     */
    public int getY2() {
        return y2;
    }

    /**
     * Retorna a altura atual do obstáculo
     * @return Altura do obstáculo
     */
    public int getAltura() {
        return altura;
    }

    /**
     * Define uma nova altura para o obstáculo
     * @param altura Nova altura a ser definida
     */
    public void setAltura(int altura) {
        this.altura = altura;
    }

    /**
     * Retorna o tipo específico do obstáculo
     * @return TipoObstaculo do obstáculo
     */
    public TipoObstaculo getTipoObstaculo() {
        return tipoObstaculo;
    }

    /**
     * Retorna a integridade atual do obstáculo
     * @return Pontos de integridade restantes
     */
    public int getIntegridade() {
        return integridade;
    }

    /**
     * Define a integridade do obstáculo
     * @param integridade Nova integridade a ser definida
     */
    public void setIntegridade(int integridade) {
        this.integridade = integridade;
    }

    /**
     * Verifica se o obstáculo é indestrutível
     * @return true se for indestrutível, false caso contrário
     */
    public boolean isIndestrutivel() {
        return indestrutivel;
    }

    /**
     * Define se o obstáculo é indestrutível
     * @param indestrutivel true para tornar indestrutível, false caso contrário
     */
    public void setIndestrutivel(boolean indestrutivel) {
        this.indestrutivel = indestrutivel;
    }

    /**
     * Processa o dano recebido pelo obstáculo e verifica se foi destruído
     * @param dano Quantidade de dano a ser aplicada
     * @param ambiente Referência ao ambiente para remoção se destruído
     * @return String descrevendo o resultado da defesa
     */
    public String defender(int dano, Ambiente ambiente) {
        if (indestrutivel) {
            return "O obstáculo é indestrutível";
        } else {
            this.integridade -= dano;

            if (integridade <= 0) {
                ambiente.removerEntidade(this);
                return "O obstáculo foi destruído";
            }

            return "O obstáculo ainda não foi destruído";
        }
    }

    /**
     * Método interno para acesso direto à posição X inicial (usado pelo ambiente)
     * @return Coordenada X1 do obstáculo
     */
    public int getXInterno() {
        return this.x1;
    }

    /**
     * Método interno para acesso direto à posição Y inicial (usado pelo ambiente)
     * @return Coordenada Y1 do obstáculo
     */
    public int getYInterno() {
        return this.y1;
    }

    /**
     * Método interno para acesso direto à altura (usado pelo ambiente)
     * @return Altura do obstáculo
     */
    public int getZInterno() {
        return this.altura;
    }
}
