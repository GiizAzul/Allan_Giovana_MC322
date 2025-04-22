import java.util.ArrayList;
import java.util.Comparator;

/**
 * Classe que representa um robô com funcionalidades básicas de movimento e sensoriamento.
 */
public class Robo {
    // Propriedades
    private String nome;        // Nome do robô
    private String direcao;     // Direção atual do robô (Norte, Sul, Leste, Oeste)
    private int posicaoX;       // Coordenada X da posição do robô
    private int posicaoY;       // Coordenada Y da posição do robô
    private int integridade;    // Nível de integridade do robô (0-100)
    private boolean operando;   // Estado de operação do robô (true = operando, false = inoperante)
    private ArrayList<Sensor> listaSensores; // Lista de sensores acoplados ao robô

    // Acessível somente para subclasses
    protected boolean visivel = true;  // Indica se o robô está visível para outros robôs

    /**
     * Construtor que inicializa um robô com parâmetros básicos
     * @param nome Nome do robô
     * @param direcao Direção inicial do robô
     * @param posicaoX Posição inicial X
     * @param posicaoY Posição inicial Y
     */
    public Robo(String nome, String direcao, int posicaoX, int posicaoY) {
        this.nome = nome;
        this.direcao = direcao;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.operando = true;
        this.integridade = 100;
        this.listaSensores = new ArrayList<Sensor>();
    }

    /**
     * Move o robô de acordo com o delta especificado
     * @param deltaX Deslocamento na direção X
     * @param deltaY Deslocamento na direção Y
     */
    public void mover(int deltaX, int deltaY) {
        posicaoX += deltaX;
        posicaoY += deltaY;
    }

    /**
     * Retorna uma string com a posição atual do robô
     * @return String formatada com nome e posição do robô
     */
    public String exibirPosicao() {
        return this.nome + " está na posição X:" + this.posicaoX + " Y:" + this.posicaoY;
    }

    /**
     * Obtém a coordenada X atual do robô
     * @return Posição X
     */
    public int getPosicaoX() {
        return posicaoX;
    }

    /**
     * Obtém a coordenada Y atual do robô
     * @return Posição Y
     */
    public int getPosicaoY() {
        return posicaoY;
    }

    /**
     * Obtém a direção atual do robô
     * @return Direção
     */
    public String getDirecao() {
        return direcao;
    }

    /**
     * Define uma nova direção para o robô
     * @param direcao Nova direção
     */
    public void setDirecao(String direcao) {
        this.direcao = direcao;
    }

    /**
     * Verifica se o robô está operando
     * @return Estado de operação
     */
    public boolean getOperando() {
        return operando;
    }

    /**
     * Define o estado de operação do robô
     * @param operando Novo estado de operação
     */
    public void setOperando(boolean operando) {
        this.operando = operando;
    }

    /**
     * Obtém o nome do robô
     * @return Nome do robô
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define um novo nome para o robô
     * @param nome Novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Verifica se o robô está visível
     * @return Estado de visibilidade
     */
    public boolean getVisivel() {
        return this.visivel;
    }

    /**
     * Obtém o nível de integridade atual do robô
     * @return Nível de integridade
     */
    public int getIntegridade() {
        return integridade;
    }

    /**
     * Define um novo nível de integridade para o robô
     * Se a integridade for <= 0, o robô ficará inoperante
     * @param integridade Novo nível de integridade
     */
    public void setIntegridade(int integridade) {
        if (integridade <= 0) {
            this.integridade = 0;
            this.operando = false;
        } else {
            this.integridade = integridade;
            this.operando = true;
        }
    }

    /**
     * Identifica obstáculos (outros robôs) na direção especificada
     * @param ambiente Ambiente onde os robôs estão
     * @param direcao Direção a ser verificada
     * @return Lista de robôs que são obstáculos na direção indicada
     */
    public ArrayList<Robo> identificarObstaculo(Ambiente ambiente, String direcao) {
        ArrayList<Robo> listaRobo = ambiente.getListaRobos();
        ArrayList<Robo> obstaculos = new ArrayList<>();

        if (direcao.equals("Norte")) {
            // Identifica robôs ao norte (mesmo X, Y maior)
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoX() == this.posicaoX && robo.getPosicaoY() > this.posicaoY) {
                    obstaculos.add(robo);
                }
            }
            // Ordena por Y crescente ou seja, o mais próximo primeiro
            obstaculos.sort(Comparator.comparingInt(o -> o.posicaoY));
        } else if (direcao.equals("Sul")) {
            // Identifica robôs ao sul (mesmo X, Y menor)
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoX() == this.posicaoX && robo.getPosicaoY() < this.posicaoY) {
                    obstaculos.add(robo);
                }
                // Ordena por Y decrescente ou seja, o mais próximo primeiro
                obstaculos.sort(Comparator.comparingInt((Robo o) -> o.posicaoY).reversed());
            }
        } else if (direcao.equals("Leste")) {
            // Identifica robôs ao leste (mesmo Y, X maior)
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoY() == this.posicaoY && robo.getPosicaoX() > this.posicaoX) {
                    obstaculos.add(robo);
                }
                // Ordena por X crescente ou seja, o mais próximo primeiro
                obstaculos.sort(Comparator.comparingInt(o -> o.posicaoX));
            }
        } else if (direcao.equals("Oeste")) {
            // Identifica robôs ao oeste (mesmo Y, X menor)
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoY() == this.posicaoY && robo.getPosicaoX() < this.posicaoX) {
                    obstaculos.add(robo);
                }
                // Ordena por X decrescente ou seja, o mais próximo primeiro
                obstaculos.sort(Comparator.comparingInt((Robo o) -> o.posicaoX).reversed());
            }
        } else {
            return null; // Direção inválida
        }
        return obstaculos;
    }

    /**
     * Aplica dano ao robô e verifica seu estado de operação
     * @param dano Quantidade de dano a ser aplicada
     * @return Mensagem indicando o estado do robô após o dano
     */
    public String defender(int dano) {
        this.integridade -= dano;

        if (integridade <= 0) {
            this.operando = false;
            return "O robô " + getNome() + " está inoperante devido ao dano tomado";
        }

        return "O robô " + getNome() + " ainda está operando";
    }

    /**
     * Calcula a distância euclidiana entre este robô e outro
     * @param robo Robô alvo para cálculo de distância
     * @return Distância calculada
     */
    public double distanciaRobo(Robo robo) {
        return Math.sqrt(Math.pow(robo.getPosicaoX() - this.getPosicaoX(), 2)
                + Math.pow(robo.getPosicaoY() - this.getPosicaoY(), 2));
    }

    /**
     * Retorna as direções possíveis para um robô
     * @return Lista de strings com as direções disponíveis
     */
    public static ArrayList<String> getDirecoesPossiveis() {
        ArrayList<String> opcoes = new ArrayList<>();
        opcoes.add("Norte");
        opcoes.add("Sul");
        opcoes.add("Leste");
        opcoes.add("Oeste");
        return opcoes;
    }
}