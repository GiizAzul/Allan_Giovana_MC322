import java.util.ArrayList;

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
        listaRobos.add(robo);
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
        listaObstaculos.add(obstaculo);
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
     * tipo 1: Robôs terrestres (1: TanqueGuerra, 2: Correios)
     * tipo 2: Robôs aéreos (1: DroneAtaque, 2: DroneVigilancia)
     */
    public Robo criarRobo(int tipo, int subcategoria, Object... atributo) {
        if (tipo == 1) {
            if (subcategoria == 1) {
                return new TanqueGuerra((String) atributo[0], (String) atributo[1], (Integer) atributo[2],
                        (Integer) atributo[3], (Integer) atributo[4], (Integer) atributo[5], (Integer) atributo[6]);
            } else if (subcategoria == 2) {
                return new Correios((String) atributo[0], (String) atributo[1], (Integer) atributo[2],
                        (Integer) atributo[3], (Integer) atributo[4], (Integer) atributo[5], (Float) atributo[6]);
            }
        } else if (tipo == 2) {
            if (subcategoria == 1) {
                return new DroneAtaque((String) atributo[0], (String) atributo[1], (Integer) atributo[2],
                        (Integer) atributo[3], (Integer) atributo[4], (Integer) atributo[5], (Integer) atributo[6],
                        (Integer) atributo[7]);
            } else if (subcategoria == 2) {
                return new DroneVigilancia((String) atributo[0], (String) atributo[1], (Integer) atributo[2],
                        (Integer) atributo[3], (Integer) atributo[4], (Integer) atributo[5], (Float) atributo[6],
                        (Float) atributo[7]);
            }
        }
        return null;
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
        return null;
    }
}