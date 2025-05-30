package robos.geral;

import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.equipamentos.sensores.*;
import interfaces.*;
import excecoes.*;
import excecoes.sensor.*;

/**
 * Classe que representa um robô com funcionalidades básicas de movimento e
 * sensoriamento.
 */
public abstract class Robo implements Entidade, Destrutivel {
    // Propriedades
    private final int id; // Identificador único do robô
    private static int cont_robo = 0; // Contador estático para gerar IDs únicos
    private String nome; // Nome do robô
    private String direcao; // Direção atual do robô (Norte, Sul, Leste, Oeste)
    private int posicaoX; // Coordenada X da posição do robô
    private int posicaoY; // Coordenada Y da posição do robô
    private int posicaoZ;
    private int integridade; // Nível de integridade do robô (0-100)
    private int velocidade; // Velocidade do robô
    private boolean estado; // Estado de operação do robô (true = ligado, false = desligado)
    private MateriaisRobo material; // Material do robô
    private TipoEntidade tipo; // Tipo de entidade (ROBO, OBSTACULO, etc.)

    // Lista de sensores do robô padrões
    private GPS gps; // Sensor de GPS -> Informar a posição dos Robôs
    private ArrayList<Sensor<?>> listaSensores; // Lista de sensores do robô

    // Acessível somente para subclasses
    protected boolean visivel = true; // Indica se o robô está visível para outros robôs

    /**
     * Construtor que inicializa um robô com parâmetros básicos
     * 
     * @param nome       Nome do robô
     * @param direcao    Direção inicial do robô
     * @param posicaoX   Posição inicial X
     * @param posicaoY   Posição inicial Y
     * @param posicaoZ   Posição inicial Z
     * @param velocidade Velocidade inicial do robô
     */
    public Robo(String nome, String direcao, MateriaisRobo material, int posicaoX, int posicaoY, int posicaoZ,
            int velocidade) {
        this.id = ++cont_robo;
        this.nome = nome;
        this.direcao = direcao;
        this.material = material;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.posicaoZ = posicaoZ;
        this.estado = true;
        this.integridade = 100;
        this.velocidade = velocidade;
        this.gps = new GPS(this);
        this.listaSensores = new ArrayList<>();
        this.listaSensores.add(gps);
        this.tipo = TipoEntidade.ROBO;
    }

    public int getId() {
        return id;
    }

    /**
     * Move o robô de acordo com o delta especificado
     * 
     * @param deltaX   Deslocamento na direção X
     * @param deltaY   Deslocamento na direção Y
     * @param ambiente Ambiente onde o robô está
     */

    public void mover(int deltaX, int deltaY, Ambiente ambiente) throws ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        // Verifica se o robô está dentro dos limites do ambiente
        if (posicaoX + deltaX < 0 || posicaoY + deltaY < 0 || posicaoX + deltaX >= ambiente.getTamX()
                || posicaoY + deltaY >= ambiente.getTamY()) {
            throw new ForaDosLimitesException("O robô " + this.nome + " tentou se mover para fora dos limites do ambiente");
        }

        int destinoX = posicaoX + deltaX;
        int destinoY = posicaoY + deltaY;

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            for (int x = posicaoX + passoX; x != destinoX + passoX; x += passoX) {
                Object obj = ambiente.identificarEntidadePosicao(x, posicaoY, posicaoZ);
                if (obj != null) {
                    if (obj instanceof Obstaculo && 
                        ((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        // Tratamento especial para buraco
                        ambiente.removerEntidade(this);
                        throw new RoboDestruidoPorBuracoException(
                                "O robô " + this.nome + " caiu em um BURACO na posição X:" + x + " Y:" + posicaoY + " e foi destruído");
                    } else {
                        // Para outros obstáculos ou robôs, apenas para o movimento
                        throw new ColisaoException("O robô " + this.nome + " interrompeu o movimento devido a um objeto na posição X:" + x + " Y:" + posicaoY);
                    }
                }
                posicaoX = x; // Atualiza a posição X antes da colisão
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = posicaoY + passoY; y != destinoY + passoY; y += passoY) {
                Object obj = ambiente.identificarEntidadePosicao(posicaoX, y, posicaoZ);
                
                if (obj != null) {
                    if (obj instanceof Obstaculo && 
                        ((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        // Tratamento especial para buraco
                        ambiente.removerEntidade(this);
                        throw new RoboDestruidoPorBuracoException(
                                "O robô " + this.nome + " caiu em um BURACO na posição X:" + posicaoX + " Y:" + y + " e foi destruído");
                    } else {
                        // Para outros obstáculos ou robôs, apenas para o movimento
                        throw new ColisaoException("O robô " + this.nome + " interrompeu o movimento devido a um objeto na posição X:" + posicaoX + " Y:" + y);
                    }
                }
                ambiente.moverEntidade(this, posicaoX, y, posicaoZ);
                posicaoY = y; // Atualiza a posição Y antes da colisão
            }
        }
    }

    /**
     * Retorna uma string com a posição atual do robô
     * Se o GPS estiver inativo ou não disponível, retorna uma mensagem de erro
     * 
     * @return String formatada com nome e posição do robô
     */
    public String exibirPosicao() throws SensorInativoException {
        verificarGPSAtivo();

        int x = this.getX();
        int y = this.getY();
        int z = this.getZ();
        return this.nome + " está na posição X:" + x + " Y:" + y + " Z:" + z;

    }

    public String getDescricao() {
        try {
            return exibirPosicao() + "\nSua direção é " + direcao + "\nO robô está "
                    + (estado ? "ligado\n" : "desligado\n");
        } catch (SensorInativoException e) {
            return "Erro ao exibir posição: " + e.getMessage();
        }
    }

    public String getRepresentacao() {
        return tipo.getRepresentacao();
    }

    /**
     * Método interno para acesso direto à posição X
     * Este método é usado apenas pelo GPS e internamente
     */
    public int getXInterno() {
        return this.posicaoX;
    }

    /**
     * Método interno para acesso direto à posição Y
     * Este método é usado apenas pelo GPS e internamente
     */
    public int getYInterno() {
        return this.posicaoY;
    }

    /**
     * Método interno para acesso direto à posição Y
     * Este método é usado apenas pelo GPS e internamente
     */
    public int getZInterno() {
        return this.posicaoZ;
    }

    /**
     * Obtém a coordenada X atual do robô através do sensor GPS
     * 
     * @return Posição X
     */
    public int getX() throws SensorInativoException {
        verificarGPSAtivo();
        return this.gps.obterPosicaoX();
    }

    /**
     * Obtém a coordenada Y atual do robô através do sensor GPS
     * 
     * @return Posição Y
     */
    public int getY() throws SensorInativoException {
        verificarGPSAtivo();
        return this.gps.obterPosicaoY();
    }

    /**
     * Obtém a coordenada Z atual
     * 
     * @return Posição Z
     */
    public int getZ() throws SensorInativoException {
        verificarGPSAtivo();
        return this.posicaoZ;
    }

    public int getVelocidade() {
        return this.velocidade;
    }

    public MateriaisRobo getMateriaisRobo() {
        return this.material;
    }

    public void setPosicaoX(int posicaoX) {
        this.posicaoX = posicaoX;
    }

    public void setPosicaoY(int posicaoY) {
        this.posicaoY = posicaoY;
    }

    public void setPosicaoZ(int posicaoZ) {
        this.posicaoZ = posicaoZ;
    }

    /**
     * Obtém a direção atual do robô
     * 
     * @return Direção
     */
    public String getDirecao() {
        return direcao;
    }

    /**
     * Define uma nova direção para o robô
     * 
     * @param direcao Nova direção
     */
    public void setDirecao(String direcao) {
        this.direcao = direcao;
    }

    /**
     * Verifica se o robô está ligado
     * 
     * @return Estado de operação
     */
    public boolean getEstado() {
        return estado;
    }

    /**
     * Define o estado de operação do robô
     * 
     * @param estado Novo estado de operação
     */
    public void ligar() {
        this.estado = true;
    }

    public void desligar() {
        this.estado = false;
    }

    /**
     * Obtém o nome do robô
     * 
     * @return Nome do robô
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define um novo nome para o robô
     * 
     * @param nome Novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Verifica se o robô está visível
     * 
     * @return Estado de visibilidade
     */
    public boolean getVisivel() {
        return this.visivel;
    }

    /**
     * Obtém o nível de integridade atual do robô
     * 
     * @return Nível de integridade
     */
    public int getIntegridade() {
        return integridade;
    }

    /**
     * Define um novo nível de integridade para o robô
     * Se a integridade for <= 0, o robô ficará inoperante
     * 
     * @param integridade Novo nível de integridade
     */
    public void setIntegridade(int integridade) {
        if (integridade <= 0) {
            this.integridade = 0;
            this.estado = false;
        } else {
            this.integridade = integridade;
        }
    }

    /**
     * Identifica outros robôs na direção especificada.
     * Na implementação base, um robô genérico não possui capacidade de detectar
     * outros robôs.
     * Subclasses equipadas com sensores apropriados devem sobrescrever este método.
     * 
     * @return Lista vazia na classe base; em subclasses, retorna robôs detectados
     *         pelos sensores
     */
    public ArrayList<Robo> identificarRobo() {
        return new ArrayList<Robo>();
    }

    /**
     * Identifica obstáculos não-robóticos na direção especificada.
     * Na implementação base, um robô genérico não possui capacidade de detectar
     * obstáculos.
     * Subclasses equipadas com sensores apropriados devem sobrescrever este método.
     * 
     * @return Lista vazia na classe base; em subclasses, retorna obstáculos
     *         detectados pelos sensores
     */
    public ArrayList<Obstaculo> identificarObstaculo() {
        return new ArrayList<Obstaculo>();
    }

    /**
     * Aplica dano ao robô e verifica seu estado de operação
     * 
     * @param dano Quantidade de dano a ser aplicada
     * @return Mensagem indicando o estado do robô após o dano
     */
    public String defender(int dano, Ambiente ambiente) {
        this.integridade -= dano;

        if (integridade <= 0) {
            this.estado = false;
            this.integridade = 0;
            return "O robô " + getNome() + " está desligado devido ao dano tomado";
        }

        return "O robô " + getNome() + " ainda está ligado";
    }

    /**
     * Calcula a distância euclidiana entre este robô e outro
     * 
     * @param robo Robô alvo para cálculo de distância
     * @return Distância calculada
     */
    public double distanciaRobo(Robo robo) throws SensorInativoException {
        verificarGPSAtivo();

        return Math.sqrt(Math.pow(robo.getXInterno() - this.getX(), 2)
                + Math.pow(robo.getYInterno() - this.getY(), 2));
    }

    /**
     * Calcula a distância euclidiana entre este robô e obstaculo
     * 
     * @param obstaculo obstaculo alvo para cálculo de distância
     * @return Distância calculada
     */
    public double distanciaObstaculo(Obstaculo obstaculo) throws SensorInativoException {
        verificarGPSAtivo();

        if (getX() <= obstaculo.getX1() && getX() <= obstaculo.getX2()) {
            return Math.min(Math.abs(getY() - obstaculo.getY1()), Math.abs(getY() - obstaculo.getY2()));
        } else if (getY() <= obstaculo.getY1() && getY() <= obstaculo.getY2()) {
            return Math.min(Math.abs(getX() - obstaculo.getX1()), Math.abs(getX() - obstaculo.getX2()));
        } else {
            return Math.min(Math.min(Math.sqrt(Math.pow(obstaculo.getX1() - this.getX(), 2)
                    + Math.pow(obstaculo.getY1() - this.getY(), 2)),
                    Math.sqrt(Math.pow(obstaculo.getX2() - this.getX(), 2)
                            + Math.pow(obstaculo.getY1() - this.getY(), 2))),
                    Math.min(Math.sqrt(Math.pow(obstaculo.getX1() - this.getX(), 2)
                            + Math.pow(obstaculo.getY2() - this.getY(), 2)),
                            Math.sqrt(Math.pow(obstaculo.getX2() - this.getX(), 2)
                                    + Math.pow(obstaculo.getY2() - this.getY(), 2))));
        }
    }

    /**
     * Retorna as direções possíveis para um robô
     * 
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

    public void addSensor(Sensor<?> sensor) {
        listaSensores.add(sensor);
    }

    public ArrayList<Sensor<?>> getListaSensores() {
        return listaSensores;
    }

    /**
     * Retorna o GPS do robô, se existir
     */
    public GPS getGPS() {
        return this.gps;
    }

    protected void verificarGPSAtivo() throws SensorInativoException {
        if (this.gps == null || !this.gps.isAtivo()) {
            throw new SensorInativoException("O GPS do robô " + this.nome + " está inativo ou não disponível");
        }
    }

    public TipoEntidade getTipo() {
        return this.tipo;
    }

    public String executarTarefa(Object... argumentos) throws 
    AlvoInvalidoException, MunicaoInsuficienteException, SensorInativoException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "direção":
                String direcao = (String) argumentos[1];
                setDirecao(direcao);
                return "";

            case "mover base":
                int deltaX = (Integer) argumentos[1];
                int deltaY = (Integer) argumentos[2];
                Ambiente ambiente = (Ambiente) argumentos[3];
                try {
                    mover(deltaX, deltaY, ambiente);
                } catch (ForaDosLimitesException | RoboDestruidoPorBuracoException | ColisaoException e) {
                    return "Não foi possível mover o Robô: " + e.getMessage();
                }

                return "";

            case "desligar":
                desligar();
                return "O robô " + nome + " foi desligado";

            case "ligar":
                ligar();
                return "O robô " + nome + " foi ligado";

            default:
                return "";
        }
    }
}