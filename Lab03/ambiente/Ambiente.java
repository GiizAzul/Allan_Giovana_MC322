package ambiente;
import java.util.ArrayList;

import robos.Robo;
import robos.aereos.DroneAtaque;
import robos.aereos.DroneVigilancia;
import robos.aereos.RoboAereo;
import robos.terrestres.Correios;
import robos.terrestres.TanqueGuerra;

public class Ambiente {
    // Dimensões do ambiente
    private int tamX;
    private int tamY;
    private int tamZ;

    // Listas para armazenar robôs e obstáculos
    private ArrayList<Robo> listaRobos;
    private ArrayList<Obstaculo> listaObstaculos;

    /**
     * Construtor que inicializa o ambiente com as dimensões especificadas
     */
    public Ambiente(int tamX, int tamY, int tamZ) {
        this.tamX = tamX;
        this.tamY = tamY;
        this.tamZ = tamZ;
        listaRobos = new ArrayList<Robo>();
        listaObstaculos = new ArrayList<Obstaculo>();
    }

    /**
     * Verifica se uma posição 2D está dentro dos limites do ambiente
     */
    public boolean dentroDosLimites(int x, int y) {
        return (x <= this.tamX && x >= 0) && (y <= this.tamY && y >= 0);
    }

    /**
     * Verifica se uma posição 3D está dentro dos limites do ambiente
     */
    public boolean dentroDosLimites(int x, int y, int z) {
        return (x <= this.tamX && x >= 0) && (y <= this.tamY && y >= 0) && (z <= this.tamZ && z >= 0);
    }

    /**
     * Adiciona um robô ao ambiente
     */
    public void adicionarRobo(Robo robo) {
        if (this.dentroDosLimites(robo.getPosicaoX(), robo.getPosicaoY())) {
            listaRobos.add(robo);
        }
    }

    public void removerRobo(Robo robo){
        listaRobos.remove(robo);
    }

    /**
     * Retorna a lista de robôs no ambiente
     */
    public ArrayList<Robo> getListaRobos() {
        return listaRobos;
    }

    /**
     * Adiciona um obstáculo ao ambiente
     */
    public void adicionarObstaculo(Obstaculo obstaculo) {
        if (this.dentroDosLimites(obstaculo.getX1(), obstaculo.getY1()) && 
            this.dentroDosLimites(obstaculo.getX2(), obstaculo.getY2())) {
            listaObstaculos.add(obstaculo);
        }
            
    }

    public void removerObstaculo(Obstaculo obstaculo){
        listaObstaculos.remove(obstaculo);
    }

    /**
     * Retorna a lista de obstáculos no ambiente
     */
    public ArrayList<Obstaculo> getListaObstaculos() {
        return listaObstaculos;
    }

    // Getters para as dimensões do ambiente
    public int getTamX() {
        return tamX;
    }

    public int getTamY() {
        return tamY;
    }

    public int getTamZ() {
        return tamZ;
    }

    /**
     * Cria um robô baseado no tipo e subcategoria
     * @param tipo Tipo de robô (1: Terrestre, 2: Aéreo)
     * @param subcategoria Subtipo do robô (1: TanqueGuerra/DroneAtaque, 2: Correios/DroneVigilancia)
     * @param atributo Array de atributos específicos para cada tipo de robô
     * @return Instância de Robo criada ou null se houver algum problema
     */
    public Robo criarRobo(int tipo, int subcategoria, Object... atributo) {
        // Validação dos parâmetros
        if (tipo < 1 || tipo > 2 || subcategoria < 1 || subcategoria > 2) {
            System.out.println("Tipo ou subcategoria inválidos");
            return null;
        }
        
        try {
            // Extrair parâmetros comuns
            String nome = (String) atributo[0];
            String direcao = (String) atributo[1];
            int posX = (Integer) atributo[2];
            int posY = (Integer) atributo[3];
            
            // Verificar se a posição está dentro dos limites
            if (!dentroDosLimites(posX, posY)) {
                System.out.println("Posição fora dos limites do ambiente");
                return null;
            }

            if (identificarObjetoPosicao(posX, posY)!=null){
                System.out.println("Já existe um objeto nesta posição");
                return null;
            }
            
            // Criar robô baseado no tipo e subcategoria
            switch (tipo) {
                case 1: // Robôs terrestres
                    int velocidadeMaxima = (Integer) atributo[4];
                    if (subcategoria == 1) { // TanqueGuerra
                        int municaoMax = (Integer) atributo[5];
                        int alcance = (Integer) atributo[6];
                        return new TanqueGuerra(nome, direcao, posX, posY,
                                                velocidadeMaxima, municaoMax, alcance);
                    } else { // Correios
                        int capacidadeMax = (Integer) atributo[5];
                        int pesoMaximo = (Integer) atributo[6];
                        return new Correios(nome, direcao, posX, posY, 
                                           velocidadeMaxima, capacidadeMax, pesoMaximo);
                    }
                    
                case 2: // Robôs aéreos
                    int altitude = (Integer) atributo[4];
                    int altitudeMaxima = (Integer) atributo[5];
                    
                    // Verificar se altitude está dentro dos limites
                    if (!dentroDosLimites(posX, posY, altitude)) {
                        System.out.println("Altitude fora dos limites do ambiente");
                        return null;
                    }
                    
                    if (subcategoria == 1) { // DroneAtaque
                        int municao = (Integer) atributo[6];
                        int alcance = (Integer) atributo[7];
                        return new DroneAtaque(nome, direcao, posX, posY, 
                                              altitude, altitudeMaxima, municao, alcance);
                    } else { // DroneVigilancia
                        float alcanceRadar = (Float) atributo[6];
                        float anguloCamera = (Float) atributo[7];
                        return new DroneVigilancia(nome, direcao, posX, posY, 
                                                 altitude, altitudeMaxima, alcanceRadar, anguloCamera);
                    }
                    
                default:
                    return null;
            }
        } catch (ClassCastException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Erro ao criar robô: parâmetros incorretos");
            return null;
        }
    }

    /**
     * Identifica um objeto na posição 2D específica (no plano do solo)
     */
    public Object identificarObjetoPosicao(int posX, int posY) {
        for (Robo robo : this.listaRobos) {
            if (robo.getPosicaoX() == posX && robo.getPosicaoY() == posY) {
                return robo;
            }
        }
        for (Obstaculo obstaculo : this.listaObstaculos) {
            if (obstaculo.getX1() <= posX && obstaculo.getX2() >= posX && obstaculo.getY1() <= posY && obstaculo.getY2() >= posY) {
                return obstaculo;
            }
        }
        return null;
    }

    /**
     * Identifica um objeto na posição 3D específica
     * Se posZ for 0, busca no plano do solo
     */
    public Object identificarObjetoPosicao(int posX, int posY, int posZ) {
        if (posZ == 0) {
            return this.identificarObjetoPosicao(posX, posY);
        }
        for (Robo robo : this.listaRobos) {
            if (robo instanceof RoboAereo) {
                RoboAereo roboAir = (RoboAereo) robo;
                if (roboAir.getPosicaoX() == posX && roboAir.getPosicaoY() == posY && roboAir.getAltitude() == posZ) {
                    return robo;
                }
            }
        }
        for (Obstaculo obstaculo : this.listaObstaculos) {
            if (obstaculo.getX1() <= posX && obstaculo.getX2() >= posX && obstaculo.getY1() <= posY && obstaculo.getY2() >= posY && obstaculo.getAltura()>= posZ) {
                return obstaculo;
            }
        }
        return null;
    }
}