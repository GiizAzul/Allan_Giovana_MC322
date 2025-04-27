package robos.geral;
import java.util.ArrayList;
import java.util.Comparator;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.equipamentos.sensores.*;

/**
 * Classe que representa um robô com funcionalidades básicas de movimento e
 * sensoriamento.
 */
public class Robo {
    // Propriedades
    private String nome; // Nome do robô
    private String direcao; // Direção atual do robô (Norte, Sul, Leste, Oeste)
    private int posicaoX; // Coordenada X da posição do robô
    private int posicaoY; // Coordenada Y da posição do robô
    private int integridade; // Nível de integridade do robô (0-100)
    private int velocidade; // Velocidade do robô
    private boolean operando; // Estado de operação do robô (true = operando, false = inoperante)
    private MateriaisRobo material; // Material do robô

    // Lista de sensores do robô padrões
    private GPS gps; // Sensor de GPS -> Informar a posição dos Robôs
    private ArrayList<Sensor<?>> listaSensores; // Lista de sensores do robô

    // Acessível somente para subclasses
    protected boolean visivel = true; // Indica se o robô está visível para outros robôs

    /**
     * Construtor que inicializa um robô com parâmetros básicos
     * 
     * @param nome     Nome do robô
     * @param direcao  Direção inicial do robô
     * @param posicaoX Posição inicial X
     * @param posicaoY Posição inicial Y
     * @param velocidade Velocidade inicial do robô
     */
    public Robo(String nome, String direcao, MateriaisRobo material, int posicaoX, int posicaoY, int velocidade) {
        this.nome = nome;
        this.direcao = direcao;
        this.material = material;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.operando = true;
        this.integridade = 100;
        this.velocidade = velocidade;
        this.gps = new GPS(this);
        this.listaSensores = new ArrayList<>();
        this.listaSensores.add(gps);
    }

    /**
     * Move o robô de acordo com o delta especificado
     * 
     * @param deltaX Deslocamento na direção X
     * @param deltaY Deslocamento na direção Y
     * @param ambiente Ambiente onde o robô está
     */
    public void mover(int deltaX, int deltaY, Ambiente ambiente) {
        // Verifica se o robô está dentro dos limites do ambiente
        int destinoX = posicaoX + deltaX > ambiente.getTamX() ? ambiente.getTamX() : posicaoX + deltaX;
        int destinoY = posicaoY + deltaY > ambiente.getTamY() ? ambiente.getTamY() : posicaoY + deltaY;

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            for (int x = posicaoX + passoX; x != destinoX + passoX; x += passoX) {
                Object obj = ambiente.identificarObjetoPosicao(x, posicaoY);
                if (obj != null) {
                    System.out.print("O robô "+this.nome+" colidiu com o objeto: ");
                    if (obj instanceof Robo){
                        System.out.println(((Robo)obj).getNome()+" na posição X:"+x+" Y:"+posicaoY);
                    }else if(((Obstaculo)obj).getTipo()==TipoObstaculo.BURACO){
                        System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+x+" Y:"+posicaoY);
                        System.out.println("O robô "+this.nome+" caiu no buraco e foi destruido");
                        ambiente.removerRobo(this);
                    }else{
                        System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+x+" Y:"+posicaoY);
                    }
                    return; // Para uma casa antes do obstáculo
                }
                posicaoX = x;
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = posicaoY + passoY; y != destinoY + passoY; y += passoY) {
                Object obj = ambiente.identificarObjetoPosicao(posicaoX, y);
                if (obj != null) {
                    System.out.print("O robô "+this.nome+" colidiu com o objeto: ");
                    if (obj instanceof Robo){
                        System.out.println(((Robo)obj).getNome()+" na posição X:"+posicaoX+" Y:"+y);
                    }else if(((Obstaculo)obj).getTipo()==TipoObstaculo.BURACO){
                        System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+posicaoX+" Y:"+y);
                        System.out.println("O robô "+this.nome+" caiu no buraco e foi destruido");
                        ambiente.removerRobo(this);
                    }else{
                        System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+posicaoX+" Y:"+y);
                    }
                    return; // Para uma casa antes do obstáculo
                }
                posicaoY = y;
            }
        }
    }

    /**
     * Retorna uma string com a posição atual do robô
     * Se o GPS estiver inativo ou não disponível, retorna uma mensagem de erro
     * 
     * @return String formatada com nome e posição do robô
     */
    public String exibirPosicao() {
        int x = this.getPosicaoX();
        int y = this.getPosicaoY();
        if (x == -1 || y == -1) {
            return this.nome + " está com o GPS inativo ou não disponível";
        } else {
            return this.nome + " está na posição X:" + x + " Y:" + y;
        }    
    }

    /**
     * Método interno para acesso direto à posição X
     * Este método é usado apenas pelo GPS e internamente
     */
    public int getPosicaoXInterna() {
        return this.posicaoX;
    }
    
    /**
     * Método interno para acesso direto à posição Y
     * Este método é usado apenas pelo GPS e internamente
     */
    public int getPosicaoYInterna() {
        return this.posicaoY;
    }
    
    /**
     * Obtém a coordenada X atual do robô através do sensor GPS
     * 
     * @return Posição X
     */
    public int getPosicaoX() {
        // Verificar se o GPS está disponível e ativo
        if (this.gps != null && this.gps.isAtivo()) {
            return this.gps.obterPosicaoX();
        } else {
            // Retornar -1 se o GPS não estiver disponível ou ativo
            return -1;
        }
    }
    
    /**
     * Obtém a coordenada Y atual do robô através do sensor GPS
     * 
     * @return Posição Y
     */
    public int getPosicaoY() {
        // Verificar se o GPS está disponível e ativo
        if (this.gps != null && this.gps.isAtivo()) {
            return this.gps.obterPosicaoY();
        } else {
            // Retornar -1 se o GPS não estiver disponível ou ativo
            return -1;
        }
    }

    public int getVelocidade() {
        return this.velocidade;
    }

    public MateriaisRobo getMateriaisRobo() {
        return this.material;
    }

    protected void setPosicaoX(int posicaoX) {
        this.posicaoX = posicaoX;
    }

    protected void setPosicaoY(int posicaoY) {
        this.posicaoY = posicaoY;
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
     * Verifica se o robô está operando
     * 
     * @return Estado de operação
     */
    public boolean getOperando() {
        return operando;
    }

    /**
     * Define o estado de operação do robô
     * 
     * @param operando Novo estado de operação
     */
    public void setOperando(boolean operando) {
        this.operando = operando;
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
            this.operando = false;
        } else {
            this.integridade = integridade;
            this.operando = true;
        }
    }

    /**
     * Identifica obstáculos (outros robôs) na direção especificada
     * 
     * @param ambiente Ambiente onde os robôs estão
     * @param direcao  Direção a ser verificada
     * @return Lista de robôs que são obstáculos na direção indicada
     */
    public ArrayList<Robo> identificarRobo(Ambiente ambiente, String direcao) {
        ArrayList<Robo> listaRobo = ambiente.getListaRobos();
        ArrayList<Robo> robosVistos = new ArrayList<>();

        if (direcao.equals("Norte")) {
            // Identifica robôs ao norte (mesmo X, Y maior)
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoX() == this.posicaoX && robo.getPosicaoY() > this.posicaoY) {
                    robosVistos.add(robo);
                }
            }
            // Ordena por Y crescente ou seja, o mais próximo primeiro
            robosVistos.sort(Comparator.comparingInt((Robo o) -> o.posicaoY));
        } else if (direcao.equals("Sul")) {
            // Identifica robôs ao sul (mesmo X, Y menor)
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoX() == this.posicaoX && robo.getPosicaoY() < this.posicaoY) {
                    robosVistos.add(robo);
                }
                // Ordena por Y decrescente ou seja, o mais próximo primeiro
                robosVistos.sort(Comparator.comparingInt((Robo o) -> o.posicaoY).reversed());

            }
        } else if (direcao.equals("Leste")) {
            // Identifica robôs ao leste (mesmo Y, X maior)
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoY() == this.posicaoY && robo.getPosicaoX() > this.posicaoX) {
                    robosVistos.add(robo);
                }
                // Ordena por X crescente ou seja, o mais próximo primeiro
                robosVistos.sort(Comparator.comparingInt((Robo o) -> o.posicaoX));
            }
        } else if (direcao.equals("Oeste")) {
            // Identifica robôs ao oeste (mesmo Y, X menor)
            for (Robo robo : listaRobo) {
                if (robo.getPosicaoY() == this.posicaoY && robo.getPosicaoX() < this.posicaoX) {
                    robosVistos.add(robo);
                }
                // Ordena por X decrescente ou seja, o mais próximo primeiro
                robosVistos.sort(Comparator.comparingInt((Robo o) -> o.posicaoX).reversed());
            }
        } else {
            return null; // Direção inválida
        }
        return robosVistos;
    }

    public ArrayList<Obstaculo> identificarObstaculo(Ambiente ambiente, String direcao) {

        ArrayList<Obstaculo> listaObstaculo = ambiente.getListaObstaculos();
        ArrayList<Obstaculo> obstaculosVistos = new ArrayList<>();

        if (direcao.equals("Norte")) {
            for (Obstaculo obstaculo : listaObstaculo) {
                if (obstaculo.getX1() <= this.posicaoX && obstaculo.getX2() >= this.posicaoX
                        && obstaculo.getY1() > this.posicaoY) {
                    obstaculosVistos.add(obstaculo);
                }
            }
            obstaculosVistos.sort(Comparator.comparingInt((Obstaculo o) -> o.getY1()));
        } else if (direcao.equals("Sul")) {
            for (Obstaculo obstaculo : listaObstaculo) {
                if (obstaculo.getX1() <= this.posicaoX && obstaculo.getX2() >= this.posicaoX
                        && obstaculo.getY2() < this.posicaoY) {
                    obstaculosVistos.add(obstaculo);
                }
            }
            obstaculosVistos.sort(Comparator.comparingInt((Obstaculo o) -> o.getY1()).reversed());
        } else if (direcao.equals("Leste")) {
            for (Obstaculo obstaculo : listaObstaculo) {
                if (obstaculo.getY1() <= this.posicaoY && obstaculo.getY2() >= this.posicaoY
                        && obstaculo.getX1() > this.posicaoX) {
                    obstaculosVistos.add(obstaculo);
                }
            }
            obstaculosVistos.sort(Comparator.comparingInt((Obstaculo o) -> o.getX1()));
        } else if (direcao.equals("Oeste")) {
            for (Obstaculo obstaculo : listaObstaculo) {
                if (obstaculo.getY1() <= this.posicaoY && obstaculo.getY2() >= this.posicaoY
                        && obstaculo.getX1() < this.posicaoX) {
                    obstaculosVistos.add(obstaculo);
                }
            }
            obstaculosVistos.sort(Comparator.comparingInt((Obstaculo o) -> o.getX1()).reversed());
        }
        return obstaculosVistos;
    }

    /**
     * Aplica dano ao robô e verifica seu estado de operação
     * 
     * @param dano Quantidade de dano a ser aplicada
     * @return Mensagem indicando o estado do robô após o dano
     */
    public String defender(int dano) {
        this.integridade -= dano;

        if (integridade <= 0) {
            this.operando = false;
            this.integridade = 0;
            return "O robô " + getNome() + " está inoperante devido ao dano tomado";
        }

        return "O robô " + getNome() + " ainda está operando";
    }

    /**
     * Calcula a distância euclidiana entre este robô e outro
     * 
     * @param robo Robô alvo para cálculo de distância
     * @return Distância calculada
     */
    public double distanciaRobo(Robo robo) {
        return Math.sqrt(Math.pow(robo.getPosicaoX() - this.getPosicaoX(), 2)
                + Math.pow(robo.getPosicaoY() - this.getPosicaoY(), 2));
    }

    /**
     * Calcula a distância euclidiana entre este robô e obstaculo
     * 
     * @param obstaculo obstaculo alvo para cálculo de distância
     * @return Distância calculada
     */
    public double distanciaObstaculo(Obstaculo obstaculo) {
        if (getPosicaoX()<=obstaculo.getX1()&&getPosicaoX()<=obstaculo.getX2()){
            return Math.min(Math.abs(getPosicaoY()-obstaculo.getY1()), Math.abs(getPosicaoY()-obstaculo.getY2()));
        }
        else if (getPosicaoY()<=obstaculo.getY1()&&getPosicaoY()<=obstaculo.getY2()){
            return Math.min(Math.abs(getPosicaoX()-obstaculo.getX1()), Math.abs(getPosicaoX()-obstaculo.getX2()));
        } else{
            return Math.min(Math.min(Math.sqrt(Math.pow(obstaculo.getX1() - this.getPosicaoX(), 2)
            + Math.pow(obstaculo.getY1() - this.getPosicaoY(), 2)),Math.sqrt(Math.pow(obstaculo.getX2() - this.getPosicaoX(), 2)
            + Math.pow(obstaculo.getY1() - this.getPosicaoY(), 2))),Math.min(Math.sqrt(Math.pow(obstaculo.getX1() - this.getPosicaoX(), 2)
            + Math.pow(obstaculo.getY2() - this.getPosicaoY(), 2)),Math.sqrt(Math.pow(obstaculo.getX2() - this.getPosicaoX(), 2)
            + Math.pow(obstaculo.getY2() - this.getPosicaoY(), 2))));
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
}