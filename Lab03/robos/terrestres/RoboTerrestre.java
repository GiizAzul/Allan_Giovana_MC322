package robos.terrestres;
import ambiente.Ambiente;
import ambiente.TipoObstaculo;
import robos.aereos.RoboAereo;
import robos.equipamentos.sensores.Colisao;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;

/**
 * Classe que representa um robô terrestre, especialização de Robo
 * com capacidade de velocidade limitada
 */
public class RoboTerrestre extends Robo {
    private int velocidadeMaxima;  // Velocidade máxima do robô terrestre
    private Colisao sensorColisao; // Sensor de colisão

    /**
     * Construtor de RoboTerrestre
     * @param nome Nome do robô
     * @param direcao Direção inicial
     * @param posicaoX Posição X inicial
     * @param posicaoY Posição Y inicial
     * @param velocidadeMaxima Velocidade máxima do robô
     */
    public RoboTerrestre(String nome, String direcao, Ambiente ambiente, MateriaisRobo material, int posicaoX, int posicaoY, int velocidade, int velocidadeMaxima) {
        super(nome, direcao, material, posicaoX, posicaoY, velocidade);
        this.velocidadeMaxima = velocidadeMaxima;
        this.sensorColisao = new Colisao(this, ambiente); // Inicializa o sensor de colisão
    }

    /**
     * Move o robô de acordo com o delta especificado
     * 
     * @param deltaX Deslocamento na direção X
     * @param deltaY Deslocamento na direção Y
     * @param ambiente Ambiente onde o robô está
     */
    public void mover(int deltaX, int deltaY, int velocidade, Ambiente ambiente) {
        if (velocidade> this.velocidadeMaxima) {
            System.out.println("A velocidade do robô " + this.getNome() + " ultrapassa a velocidade máxima permitida.");
            return;
        }

        // Robo Terrestre não precisa do GPS para se movimentar (v*t)
        int posicaoX = this.getPosicaoXInterna();
        int posicaoY = this.getPosicaoYInterna();

        // Verifica se o robô está dentro dos limites do ambiente
        int destinoX = posicaoX + deltaX > ambiente.getTamX() ? ambiente.getTamX() : posicaoX + deltaX;
        int destinoY = posicaoY + deltaY > ambiente.getTamY() ? ambiente.getTamY() : posicaoY + deltaY;

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            for (int x = posicaoY + passoX; x != destinoX + passoX; x += passoX) {
                int detectado = sensorColisao.acionar();
                if (detectado == 1) {
                    System.out.println("O robô " + this.getNome() + " colidiu com outro robô na posição X:" + x + " Y:" + posicaoY);
                    break; // Para uma casa antes do obstáculo
                } else if (detectado == 2) {
                    if (sensorColisao.getUltimoObstaculoColidido().getTipo() == TipoObstaculo.BURACO) {
                        System.out.println("O robô " + this.getNome() + " caiu no buraco e foi destruido");
                        ambiente.removerRobo(this);
                    } else {
                        System.out.println("O robô " + this.getNome() + " colidiu com o obstáculo: " + sensorColisao.getUltimoObstaculoColidido().getTipo() + " na posição X:" + x + " Y:" + posicaoY);
                    }
                    break; // Para uma casa antes do obstáculo
                }
                this.setPosicaoX(x);
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = posicaoY + passoY; y != destinoY + passoY; y += passoY) {
                int detectado = sensorColisao.acionar();
                if (detectado == 1) {
                    System.out.println("O robô " + this.getNome() + " colidiu com outro robô na posição X:"+ posicaoX + " Y:" + y);
                    break; // Para uma casa antes do obstáculo
                } else if (detectado == 2) {
                    if (sensorColisao.getUltimoObstaculoColidido().getTipo() == TipoObstaculo.BURACO) {
                        System.out.println("O robô " + this.getNome() + " caiu no buraco e foi destruido");
                        ambiente.removerRobo(this);
                    } else {
                        System.out.println("O robô " + this.getNome() + " colidiu com o obstáculo: " + sensorColisao.getUltimoObstaculoColidido().getTipo() + " na posição X:" + posicaoX + " Y:" + y);
                    }
                    break; // Para uma casa antes do obstáculo
                }
                this.setPosicaoY(y);
            }
        }
    }

    /**
     * Obtém a velocidade máxima do robô
     * @return Velocidade máxima
     */
    public int getVelocidadeMaxima() {
        return this.velocidadeMaxima;
    }

    /**
     * Define uma nova velocidade máxima
     * @param vmax Nova velocidade máxima
     */
    public void setVelocidadeMaxima(int vmax) {
        this.velocidadeMaxima = vmax;
    }

    /**
     * Calcula a distância entre este robô terrestre e outro
     * @param robo Robô terrestre alvo
     * @return Distância euclidiana 2D
     */
    public double distanciaRobo(RoboTerrestre robo) {
        if (!this.getGPS().isAtivo()) {
            return -1;
        }

        return Math.sqrt(Math.pow(robo.getPosicaoXInterna() - this.getPosicaoX(), 2)
                + Math.pow(robo.getPosicaoYInterna() - this.getPosicaoY(), 2));
    }

    /**
     * Calcula a distância entre este robô terrestre e um robô aéreo
     * @param alvo Robô aéreo alvo
     * @return Distância euclidiana 3D (considerando altitude do robô aéreo)
     */
    public double distanciaRobo(RoboAereo alvo) {
        if (!this.getGPS().isAtivo()) {
            return -1;
        }

        return Math.sqrt(Math.pow(alvo.getPosicaoXInterna() - this.getPosicaoX(), 2)
                + Math.pow(alvo.getPosicaoYInterna() - this.getPosicaoY(), 2) + Math.pow(alvo.getAltitude() - 0, 2));
    }

    public Colisao getSensorColisao() {
        return sensorColisao;
    }
}
