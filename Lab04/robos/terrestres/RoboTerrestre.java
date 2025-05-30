package robos.terrestres;
import ambiente.Ambiente;
import ambiente.TipoObstaculo;
import robos.equipamentos.sensores.Colisao;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import excecoes.*;
import excecoes.sensor.*;;

/**
 * Classe que representa um robô terrestre, especialização de Robo
 * com capacidade de velocidade limitada
 */
public class RoboTerrestre extends Robo {
    private int velocidadeMaxima;  // Velocidade máxima do robô terrestre
    private Colisao sensorColisao; // Sensor de colisão

    /**
     * Construtor de RoboTerrestre
     * 
     * @param nome             Nome do robô
     * @param direcao          Direção inicial
     * @param posicaoX         Posição X inicial
     * @param posicaoY         Posição Y inicial
     * @param velocidadeMaxima Velocidade máxima do robô
     */
    public RoboTerrestre(String nome, String direcao, Ambiente ambiente, MateriaisRobo material, int posicaoX, int posicaoY, int velocidade, int velocidadeMaxima) {
        super(nome, direcao, material, posicaoX, posicaoY, 0, velocidade);
        this.velocidadeMaxima = velocidadeMaxima;
        this.sensorColisao = new Colisao(this, ambiente); // Inicializa o sensor de colisão
    }

    /**
     * Move o robô de acordo com o delta especificado
     * 
     * @param deltaX   Deslocamento na direção X
     * @param deltaY   Deslocamento na direção Y
     * @param ambiente Ambiente onde o robô está
     */
    public void mover(int deltaX, int deltaY, int velocidade, Ambiente ambiente) {
        if (velocidade > this.velocidadeMaxima) {
            System.out.println("A velocidade do robô " + this.getNome() + " ultrapassa a velocidade máxima permitida.");
            return;
        }

        // Robo Terrestre não precisa do GPS para se movimentar (v*t)
        int posicaoX = this.getXInterno();
        int posicaoY = this.getYInterno();

        // Verifica se o robô está dentro dos limites do ambiente
        int destinoX = posicaoX + deltaX >= ambiente.getTamX() ? ambiente.getTamX() - 1 : posicaoX + deltaX;
        int destinoY = posicaoY + deltaY >= ambiente.getTamY() ? ambiente.getTamY() - 1 : posicaoY + deltaY;

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            int x = 0;
            int detectado = 0;
            for (x = posicaoX + passoX; x != destinoX + passoX; x += passoX) {
                ambiente.moverEntidade(this, x, getYInterno(), getZInterno());
                this.setPosicaoX(x);
                detectado = sensorColisao.acionar();
                if (detectado == 1) {
                    System.out.println("O robô " + this.getNome() + " colidiu com outro robô na posição X:" + x + " Y:" + posicaoY);
                    break; // Para uma casa antes do obstáculo
                } else if (detectado == 2) {
                    if (sensorColisao.getUltimoObstaculoColidido().getTipoObstaculo() == TipoObstaculo.BURACO) {
                        System.out.println("O robô " + this.getNome() + " caiu no buraco e foi destruido");
                    } else {
                        System.out.println("O robô " + this.getNome() + " colidiu com o obstáculo: "
                                + sensorColisao.getUltimoObstaculoColidido().getTipoObstaculo() + " na posição X:" + x
                                + " Y:"
                                + posicaoY);
                    }
                    break; // Para uma casa antes do obstáculo
                }
            }
            if (detectado == 2 && sensorColisao.getUltimoObstaculoColidido().getTipoObstaculo() == TipoObstaculo.BURACO){
                ambiente.moverEntidade(this, x - passoX, getYInterno(), getZInterno());
                this.setPosicaoX(x - passoX); // Corrige a posição do robô
                ambiente.removerEntidade(this);
            }
            else if (detectado != 0) {
                ambiente.moverEntidade(this, x - passoX, getYInterno(), getZInterno());
                this.setPosicaoX(x - passoX); // Corrige a posição do robô
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            int y = 0, detectado = 0;
            for (y = posicaoY + passoY; y != destinoY + passoY; y += passoY) {
                ambiente.moverEntidade(this, getXInterno(), y, getZInterno());
                this.setPosicaoY(y);
                detectado = sensorColisao.acionar();
                if (detectado == 1) {
                    System.out.println("O robô " + this.getNome() + " colidiu com outro robô na posição X:"+ posicaoX + " Y:" + y);
                    break; // Para uma casa antes do obstáculo
                } else if (detectado == 2) {
                    if (sensorColisao.getUltimoObstaculoColidido().getTipoObstaculo() == TipoObstaculo.BURACO) {
                        System.out.println("O robô " + this.getNome() + " caiu no buraco e foi destruido");
                    } else {
                        System.out.println("O robô " + this.getNome() + " colidiu com o obstáculo: "
                                + sensorColisao.getUltimoObstaculoColidido().getTipoObstaculo() + " na posição X:"
                                + posicaoX
                                + " Y:" + y);
                    }
                    break; // Para uma casa antes do obstáculo
                }
            }
            if (detectado == 2 && sensorColisao.getUltimoObstaculoColidido().getTipoObstaculo() == TipoObstaculo.BURACO){
                ambiente.moverEntidade(this, getXInterno(), y - passoY, getZInterno());
                this.setPosicaoY(y - passoY); // Corrige a posição do robô
                ambiente.removerEntidade(this);
            }
            else if (detectado != 0) {
                ambiente.moverEntidade(this, getXInterno(), y - passoY, getZInterno());
                this.setPosicaoY(y - passoY); // Corrige a posição do robô
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
     * Calcula a distância entre este robô terrestre e um robô
     * @param alvo Robô aéreo alvo
     * @return Distância euclidiana 3D (considerando altitude do robô)
     */
    public double distanciaRobo(Robo alvo) throws SensorInativoException {
        super.verificarGPSAtivo();

        return Math.sqrt(Math.pow(alvo.getXInterno() - this.getX(), 2)
                + Math.pow(alvo.getYInterno() - this.getY(), 2) + Math.pow(alvo.getZ() - 0, 2));
    }

    public Colisao getSensorColisao() {
        return sensorColisao;
    }

    public String executarTarefa(Object... argumentos) throws AlvoInvalidoException, MunicaoInsuficienteException, SensorInativoException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        String result = super.executarTarefa(argumentos);
        if (result != ""){
            return result;
        }
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "mover":
                int deltaX = (Integer) argumentos[1];
                int deltaY = (Integer) argumentos[2];
                int velocidade = (Integer) argumentos[3];
                Ambiente ambiente = (Ambiente) argumentos[4];
                mover(deltaX, deltaY, velocidade, ambiente);
                return "";

            case "velocidade":
                int vel = (Integer) argumentos[1];
                setVelocidadeMaxima(vel);
                return "";

            default:
                return "";
        }
    }

}
