package ambiente;

import java.util.ArrayList;

import excecoes.ambiente.ArquivoInvalidoException;
import excecoes.ambiente.ForaDosLimitesException;
import excecoes.robos.gerais.ColisaoException;
import robos.aereos.DroneAtaque;
import robos.aereos.DroneVigilancia;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.terrestres.Correios;
import robos.terrestres.TanqueGuerra;
import utils.Logger;
import interfaces.*;

/**
 * Classe responsável por gerenciar o ambiente 3D onde os robôs e obstáculos estão inseridos.
 * Controla as dimensões do espaço, posicionamento de entidades e verificação de colisões.
 */
public class Ambiente {
    // Dimensões do ambiente
    private final int tamX;
    private final int tamY;
    private final int tamZ;

    // Listas para armazenar robôs e obstáculos
    private ArrayList<Entidade> entidades;

    // Mapa 3D para controle de ocupação do espaço
    private TipoEntidade[][][] mapa;

    // Logger geral do sistema
    private Logger logger;

    /**
     * Construtor que inicializa o ambiente com as dimensões especificadas
     * @param tamX Tamanho do ambiente no eixo X
     * @param tamY Tamanho do ambiente no eixo Y
     * @param tamZ Tamanho do ambiente no eixo Z (altura)
     */
    public Ambiente(int tamX, int tamY, int tamZ) throws ArquivoInvalidoException {
        this.tamX = tamX;
        this.tamY = tamY;
        this.tamZ = tamZ;
        entidades = new ArrayList<Entidade>();
        mapa = new TipoEntidade[tamY][tamX][tamZ];
        this.logger = new Logger("LogSistemaRobos.txt");
        this.inicializarMapa();
    }

    /**
     * Inicializa o mapa 3D preenchendo todas as posições como VAZIO
     */
    private void inicializarMapa() {
        for (int y = 0; y < tamY; y++) {
            for (int x = 0; x < tamX; x++) {
                for (int z = 0; z < tamZ; z++) {
                    mapa[y][x][z] = TipoEntidade.VAZIO;
                }
            }
        }
    }

    /**
     * Verifica se uma posição 2D está dentro dos limites do ambiente
     * @param x Coordenada X
     * @param y Coordenada Y
     * @return true se está dentro dos limites, false caso contrário
     */
    public boolean dentroDosLimites(int x, int y) {
        return (x <= this.tamX && x >= 0) && (y <= this.tamY && y >= 0);
    }

    /**
     * Verifica se uma posição 3D está dentro dos limites do ambiente
     * @param x Coordenada X
     * @param y Coordenada Y
     * @param z Coordenada Z (altura)
     * @return true se está dentro dos limites, false caso contrário
     */
    public boolean dentroDosLimites(int x, int y, int z) {
        return (x <= this.tamX && x >= 0) && (y <= this.tamY && y >= 0) && (z <= this.tamZ && z >= 0);
    }

    /**
     * Adiciona uma entidade (robô ou obstáculo) ao ambiente
     * @param entidade A entidade a ser adicionada
     * @throws ForaDosLimitesException Se a entidade estiver fora dos limites do ambiente
     */
    public void adicionarEntidade(Entidade entidade) throws ForaDosLimitesException {
        if (entidade.getTipo() == TipoEntidade.ROBO) {
            if (this.dentroDosLimites(entidade.getXInterno(), entidade.getYInterno(), entidade.getZInterno())) {
                entidades.add(entidade);
                mapa[entidade.getYInterno()][entidade.getXInterno()][entidade.getZInterno()] = TipoEntidade.ROBO;
            }
        } else if (entidade.getTipo() == TipoEntidade.OBSTACULO) {
            Obstaculo obstaculo = (Obstaculo) entidade;
            if (this.dentroDosLimites(obstaculo.getX1(), obstaculo.getY1()) &&
                    this.dentroDosLimites(obstaculo.getX2(), obstaculo.getY2())) {
                entidades.add(obstaculo);
                // Marca todas as posições ocupadas pelo obstáculo no mapa
                for (int x = obstaculo.getX1(); x <= obstaculo.getX2(); x++) {
                    for (int y = obstaculo.getY1(); y <= obstaculo.getY2(); y++) {
                        for (int z = 0; z <= obstaculo.getAltura(); z++) {
                            mapa[y][x][z] = TipoEntidade.OBSTACULO;
                        }
                    }
                }
            }
        }
    }

    /**
     * Remove uma entidade do ambiente e atualiza o mapa
     * @param entidade A entidade a ser removida
     */
    public void removerEntidade(Entidade entidade) {
        entidades.remove(entidade);
        if (entidade.getTipo() == TipoEntidade.ROBO) {
            mapa[entidade.getYInterno()][entidade.getXInterno()][entidade.getZInterno()] = TipoEntidade.VAZIO;
        } else if (entidade.getTipo() == TipoEntidade.OBSTACULO) {
            Obstaculo obstaculo = (Obstaculo) entidade;
            // Limpa todas as posições ocupadas pelo obstáculo no mapa
            for (int x = obstaculo.getX1(); x <= obstaculo.getX2(); x++) {
                for (int y = obstaculo.getY1(); y <= obstaculo.getY2(); y++) {
                    for (int z = 0; z <= obstaculo.getAltura(); z++) {
                        mapa[y][x][z] = TipoEntidade.VAZIO;
                    }
                }
            }
        }
    }

    /**
     * Verifica se uma posição específica está ocupada
     * @param x Coordenada X
     * @param y Coordenada Y
     * @param z Coordenada Z
     * @return true se a posição está ocupada, false caso contrário
     */
    public boolean estaOcupado(int x, int y, int z) {
        if (mapa[y][x][z] == TipoEntidade.VAZIO) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Retorna a lista de entidades no ambiente
     * @return ArrayList contendo todas as entidades
     */
    public ArrayList<Entidade> getEntidades() {
        return entidades;
    }

    /**
     * Move uma entidade para uma nova posição no ambiente
     * @param entidade A entidade a ser movida
     * @param novoX Nova coordenada X
     * @param novoY Nova coordenada Y
     * @param novoZ Nova coordenada Z
     */
    public void moverEntidade(Entidade entidade, int novoX, int novoY, int novoZ) {
        // Limpa a posição antiga
        mapa[entidade.getYInterno()][entidade.getXInterno()][entidade.getZInterno()] = TipoEntidade.VAZIO;
        // Marca a nova posição
        mapa[novoY][novoX][novoZ] = entidade.getTipo();
    }

    /**
     * Verifica se há colisão em uma posição específica
     * @param x Coordenada X
     * @param y Coordenada Y
     * @param z Coordenada Z
     * @throws ColisaoException Se a posição estiver ocupada
     */
    public void verificarColisoes(int x, int y, int z) throws ColisaoException {
        if (estaOcupado(x, y, z)){
            throw new ColisaoException(null);
        }
    }

    /**
     * Cria uma representação visual 2D do ambiente
     * @return Matriz de strings representando o ambiente visualmente
     */
    public String[][] visualizarAmbiente() {
        String[][] vizuMapa = new String[tamX][tamY];
        // Inicializa o mapa visual como vazio
        for (int x = 0; x < tamX; x++) {
            for (int y = 0; y < tamY; y++) {
                vizuMapa[y][x] = TipoEntidade.VAZIO.getRepresentacao();
            }
        }
        
        // Percorre o mapa 3D e marca as entidades na visualização 2D
        for (int y = tamY - 1; y >= 0; y--) {
            for (int x = 0; x < tamX; x++) {
                for (int z = 0; z < tamZ; z++) {
                    if (mapa[y][x][z] != TipoEntidade.VAZIO){
                        if (mapa[y][x][z]!= TipoEntidade.OBSTACULO){
                            vizuMapa[vizuMapa.length - 1 - y][x]=mapa[y][x][z].getRepresentacao();
                        } else{
                            Obstaculo o = (Obstaculo) identificarEntidadePosicao(x, y, z);
                            vizuMapa[vizuMapa.length - 1 - y][x]=o.getRepresentacao();
                        }
                    }
                }
            }
        }
        return vizuMapa;
    }

    // Getters para as dimensões do ambiente
    
    /**
     * Retorna o tamanho do ambiente no eixo X
     * @return Tamanho X do ambiente
     */
    public int getTamX() {
        return tamX;
    }

    /**
     * Retorna o tamanho do ambiente no eixo Y
     * @return Tamanho Y do ambiente
     */
    public int getTamY() {
        return tamY;
    }

    /**
     * Retorna o tamanho do ambiente no eixo Z (altura)
     * @return Tamanho Z do ambiente
     */
    public int getTamZ() {
        return tamZ;
    }

    /**
     * Retorna o Logger utilizado para registro das atividades do sistema
     * @return Logger do ambiente
     */

    public Logger getLogger() {
        return this.logger;
    }

    /**
     * Cria um robô baseado no tipo e subcategoria especificados
     * Factory method para criação de diferentes tipos de robôs
     * 
     * @param tipo         Tipo de robô (1: Terrestre, 2: Aéreo)
     * @param subcategoria Subtipo do robô (1: TanqueGuerra/DroneAtaque, 2: Correios/DroneVigilancia)
     * @param atributo     Array de atributos específicos para cada tipo de robô
     * @return Instância de Robo criada ou null se houver algum problema
     */
    public Robo criarRobo(int tipo, int subcategoria, Object... atributo) {
        // Validação dos parâmetros
        if (tipo < 1 || tipo > 2 || subcategoria < 1 || subcategoria > 2) {
            System.out.println("Tipo ou subcategoria inválidos");
            return null;
        }

        try {
            // Extrair parâmetros comuns a todos os robôs
            String nome = (String) atributo[0];
            String direcao = (String) atributo[1];
            MateriaisRobo material = (MateriaisRobo) atributo[2];
            int posX = (Integer) atributo[3];
            int posY = (Integer) atributo[4];
            int posZ = (Integer) atributo[5];
            int velocidade = (Integer) atributo[6];
            int velocidadeMaxima = (Integer) atributo[7];

            // Verificar se a posição está dentro dos limites
            if (!dentroDosLimites(posX, posY, posZ)) {
                System.out.println("Posição fora dos limites do ambiente");
                return null;
            }

            // Verificar se a posição não está ocupada
            if (estaOcupado(posX, posY, posZ)) {
                System.out.println("Já existe um objeto nesta posição");
                return null;
            }

            // Criar robô baseado no tipo e subcategoria
            switch (tipo) {
                case 1: // Robôs terrestres
                    if (subcategoria == 1) { // TanqueGuerra
                        int municaoMax = (Integer) atributo[8];
                        int alcance = (Integer) atributo[9];
                        return new TanqueGuerra(nome, direcao, this, material, posX, posY, velocidade,
                                velocidadeMaxima, municaoMax, alcance);
                    } else { // Correios
                        int capacidadeMax = (Integer) atributo[8];
                        float pesoMaximo = (Float) atributo[9];
                        return new Correios(nome, direcao, this, material, posX, posY, velocidade,
                                velocidadeMaxima, capacidadeMax, pesoMaximo);
                    }

                case 2: // Robôs aéreos
                    int altitudeMaxima = (Integer) atributo[8];

                    // Verificar se altitude está dentro dos limites
                    if (!dentroDosLimites(posX, posY, posZ)) {
                        System.out.println("Altitude fora dos limites do ambiente");
                        return null;
                    }

                    if (subcategoria == 1) { // DroneAtaque
                        int municao = (Integer) atributo[9];
                        int alcance = (Integer) atributo[10];
                        return new DroneAtaque(nome, direcao, material, posX, posY, velocidade,
                                posZ, altitudeMaxima, this, municao, alcance);
                    } else { // DroneVigilancia
                        float alcanceRadar = (Float) atributo[9];
                        float anguloRadar = (Float) atributo[10];
                        float anguloCamera = (Float) atributo[11];
                        return new DroneVigilancia(nome, direcao, material, posX, posY, velocidade,
                                posZ, altitudeMaxima, this, alcanceRadar, anguloRadar, anguloCamera);
                    }

                default:
                    return null;
            }
        } catch (ClassCastException | ArrayIndexOutOfBoundsException e ) {
            System.out.println("Erro ao criar robô: parâmetros incorretos");
            return null;
        }
    }

    /**
     * Identifica uma entidade (robô ou obstáculo) na posição 3D específica
     * @param posX Coordenada X da posição a verificar
     * @param posY Coordenada Y da posição a verificar
     * @param posZ Coordenada Z da posição a verificar (se 0, busca no plano do solo)
     * @return A entidade encontrada na posição ou null se não houver nada
     */
    public Entidade identificarEntidadePosicao(int posX, int posY, int posZ) {
        for (Entidade entidade : this.entidades) {
            if (entidade.getTipo() == TipoEntidade.ROBO) {
                Robo robo = (Robo) entidade;
                if (robo.getXInterno() == posX && robo.getYInterno() == posY && robo.getZInterno() == posZ) {
                    return robo;
                }
            } else if (entidade.getTipo() == TipoEntidade.OBSTACULO) {
                Obstaculo obstaculo = (Obstaculo) entidade;
                // Verifica se a posição está dentro da área do obstáculo
                if (obstaculo.getX1() <= posX && obstaculo.getX2() >= posX && 
                    obstaculo.getY1() <= posY && obstaculo.getY2() >= posY && 
                    obstaculo.getAltura() >= posZ) {
                    return obstaculo;
                }
            }
        }
        return null;
    }
}