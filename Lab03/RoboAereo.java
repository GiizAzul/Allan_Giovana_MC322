import java.util.ArrayList;
import java.util.Comparator;

/**
 * Classe que representa um robô aéreo, especialização de Robo
 * com capacidade de voar em diferentes altitudes
 */
public class RoboAereo extends Robo {

    private int altitude = -1;        // Altitude atual do robô aéreo
    private int altitudeMaxima = -1;  // Altitude máxima que o robô pode atingir

    /**
     * Construtor de RoboAereo
     * @param n Nome do Robô
     * @param d Direção inicial
     * @param x Posição X inicial
     * @param y Posição Y inicial
     * @param h Altura inicial
     * @param hmax Altura máxima permitida
     */
    public RoboAereo(String n, String d, int x, int y, int h, int hmax) {
        super(n, d, x, y);
        this.altitude = h;
        this.altitudeMaxima = hmax;
    }

    /**
     * Aumenta a altitude do robô aéreo
     * @param metros Quantidade de metros a subir
     */
    public void subir(int metros) {
        this.altitude += metros;
        if (this.altitude > this.altitudeMaxima) {
            System.out.println("Altura máxima foi atingida!");
            this.altitude = this.altitudeMaxima;
        }
    }

    /**
     * Diminui a altitude do robô aéreo
     * @param metros Quantidade de metros a descer
     */
    public void descer(int metros, Ambiente ambiente) {
            // Movimentação em linha reta no eixo Z
            int Z = this.altitude-metros;
            if (metros != 0) {
                int passoZ = -1;
                for (int z = altitude + passoZ; z != Z + passoZ; z += passoZ) {
                    if (this.altitude > this.altitudeMaxima) {
                        System.out.println("Altura máxima foi atingida!");
                        this.altitude = this.altitudeMaxima;
                        return;
                    }else{
                        Object obj = ambiente.identificarObjetoPosicao(getPosicaoX(), getPosicaoY(),z);
                        if (obj != null) {
                            System.out.print("O robô "+getNome()+" colidiu com o objeto: ");
                            if (obj instanceof Robo){
                                System.out.println(((Robo)obj).getNome()+" na posição X:"+getPosicaoX()+" Y:"+getPosicaoY()+" Z:"+z);
                            }
                            else{
                                System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+getPosicaoX()+" Y:"+getPosicaoY()+" Z:"+altitude);
                            }
                            return; // Para uma casa antes do obstáculo
                        }
                        this.altitude=z;
                    }
                }
            }
    }

    /**
     * Obtém a altitude atual do robô
     * @return Altitude em metros
     */
    public int getAltitude() {
        return this.altitude;
    }

    /**
     * Obtém a altitude máxima do robô
     * @return Altitude máxima em metros
     */
    public int getAltitudeMaxima() {
        return this.altitudeMaxima;
    }

    /**
     * Define uma nova altitude máxima
     * @param hMax Nova altitude máxima
     */
    public void setAltitudeMaxima(int hMax) {
        this.altitudeMaxima = hMax;
    }

    /**
     * Calcula a distância entre este robô aéreo e um robô terrestre
     * @param robo Robô terrestre alvo
     * @return Distância euclidiana 3D
     */
    public double distanciaRobo(RoboTerrestre robo) {
        return Math.sqrt(Math.pow(robo.getPosicaoX() - this.getPosicaoX(), 2)
                + Math.pow(robo.getPosicaoY() - this.getPosicaoY(), 2) + Math.pow(0 - this.getAltitude(), 2));
    }

    /**
     * Calcula a distância entre este robô aéreo e outro robô aéreo
     * @param alvo Robô aéreo alvo
     * @return Distância euclidiana 3D (considerando altitudes)
     */
    public double distanciaRobo(RoboAereo alvo) {
        return Math.sqrt(Math.pow(alvo.getPosicaoX() - this.getPosicaoX(), 2)
                + Math.pow(alvo.getPosicaoY() - this.getPosicaoY(), 2)
                + Math.pow(alvo.getAltitude() - this.getAltitude(), 2));
    }

    /**
     * Move o robô aéreo para uma nova posição 3D
     * @param X Nova coordenada X
     * @param Y Nova coordenada Y
     * @param Z Nova altitude
     */
    public void mover(int X, int Y, int Z, Ambiente ambiente) {
        int deltaX = X - getPosicaoX();
        int deltaY = Y - getPosicaoY();

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            for (int x = getPosicaoX() + passoX; x != X + passoX; x += passoX) {
                Object obj = ambiente.identificarObjetoPosicao(x, getPosicaoY(),altitude);
                if (obj != null) {
                    System.out.print("O robô "+getNome()+" colidiu com o objeto: ");
                    if (obj instanceof RoboAereo){
                        System.out.println(((RoboAereo)obj).getNome()+" na posição X:"+x+" Y:"+getPosicaoY()+" Z:"+altitude);
                    }else if(((Obstaculo)obj).getTipo()==TipoObstaculo.BURACO){
                        System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+x+" Y:"+getPosicaoY());
                        System.out.println("O robô "+getNome()+"caiu no buraco e foi destruido");
                        ambiente.removerRobo(this);
                    } else{
                        System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+x+" Y:"+getPosicaoY()+" Z:"+altitude);
                    }
                    return; // Para uma casa antes do obstáculo
                }
                setPosicaoX(x);
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = getPosicaoY() + passoY; y != Y + passoY; y += passoY) {
                Object obj = ambiente.identificarObjetoPosicao(getPosicaoX(), y, altitude);
                if (obj != null) {
                    System.out.print("O robô "+getNome()+" colidiu com o objeto: ");
                    if (obj instanceof Robo){
                        System.out.println(((Robo)obj).getNome()+" na posição X:"+getPosicaoX()+" Y:"+y+" Z:"+altitude);
                    }
                    else{
                        System.out.println(((Obstaculo)obj).getTipo()+" na posição X:"+getPosicaoX()+" Y:"+y+" Z:"+altitude);
                    }
                    return; // Para uma casa antes do obstáculo
                }
                setPosicaoY(y);
            }
        }

        
        int h = this.getAltitude();
        if (Z > h) {
            this.subir(Z - h);
        } else {
            this.descer(h - Z, ambiente);
        }
    }

    /**
     * Sobrescreve o método da classe pai para identificar obstáculos
     * considerando apenas robôs aéreos na mesma altitude
     * @param ambiente Ambiente onde os robôs estão
     * @param direcao Direção a verificar
     * @return Lista de robôs que são obstáculos na direção indicada
     */
    @Override
    public ArrayList<Robo> identificarRobo(Ambiente ambiente, String direcao) {

        ArrayList<Robo> listaRobo = ambiente.getListaRobos();
        ArrayList<Robo> robos = new ArrayList<>();
        ArrayList<RoboAereo> robosAereos = new ArrayList<>();

        // Filtra apenas robôs aéreos na mesma altitude
        for (Robo robo : listaRobo) {
            if (robo instanceof RoboAereo && robo.getVisivel()) {
                RoboAereo roboAir = (RoboAereo) robo;
                if (roboAir.getAltitude() == this.getAltitude()) {
                    robosAereos.add(roboAir);
                }
            }
        }

        if (direcao.equals("Norte")) {
            // Identifica robôs ao norte (mesmo X, Y maior)
            for (RoboAereo robo : robosAereos) {
                if (robo.getPosicaoX() == this.getPosicaoX() && robo.getPosicaoY() > this.getPosicaoY()) {
                    robos.add(robo);
                }
            }
            // Ordena por Y crescente
            robos.sort(Comparator.comparingInt(Robo::getPosicaoY));
        } else if (direcao.equals("Sul")) {
            // Identifica robôs ao sul (mesmo X, Y menor)
            for (RoboAereo robo : robosAereos) {
                if (robo.getPosicaoX() == this.getPosicaoX() && robo.getPosicaoY() < this.getPosicaoY()) {
                    robos.add(robo);
                }
                // Ordena por Y decrescente
                robos.sort(Comparator.comparingInt(Robo::getPosicaoY).reversed());
            }
        } else if (direcao.equals("Leste")) {
            // Identifica robôs ao leste (mesmo Y, X maior)
            for (RoboAereo robo : robosAereos) {
                if (robo.getPosicaoY() == this.getPosicaoY() && robo.getPosicaoX() > this.getPosicaoX()) {
                    robos.add(robo);
                }
                // Ordena por X crescente
                robos.sort(Comparator.comparingInt(Robo::getPosicaoX));
            }
        } else if (direcao.equals("Oeste")) {
            // Identifica robôs ao oeste (mesmo Y, X menor)
            for (RoboAereo robo : robosAereos) {
                if (robo.getPosicaoY() == this.getPosicaoY() && robo.getPosicaoX() < this.getPosicaoX()) {
                    robos.add(robo);
                }
                // Ordena por X decrescente
                robos.sort(Comparator.comparingInt(Robo::getPosicaoX).reversed());
            }
        }
        return robos;
    }

    public ArrayList<Obstaculo> identificarobstaculo(Ambiente ambiente, String direcao) {

        ArrayList<Obstaculo> listaObstaculo = ambiente.getListaObstaculos();
        ArrayList<Obstaculo> obstaculosVistos = new ArrayList<>();

        if (direcao.equals("Norte")) {
            for (Obstaculo obstaculo : listaObstaculo) {
                if (obstaculo.getX1() <= this.getPosicaoX() && obstaculo.getX2()>=this.getPosicaoX() && obstaculo.getY1() > this.getPosicaoY() && obstaculo.getAltura() >= this.altitude) {
                    obstaculosVistos.add(obstaculo);
                }
            }
            obstaculosVistos.sort(Comparator.comparingInt((Obstaculo o) -> o.getY1()));
        } else if (direcao.equals("Sul")) {
            for (Obstaculo obstaculo : listaObstaculo) {
                if (obstaculo.getX1() <= this.getPosicaoX() && obstaculo.getX2()>=this.getPosicaoX() && obstaculo.getY2() < this.getPosicaoY() && obstaculo.getAltura() >= this.altitude) {
                    obstaculosVistos.add(obstaculo);
                }
            }
            obstaculosVistos.sort(Comparator.comparingInt((Obstaculo o)-> o.getY1()).reversed());
        } else if (direcao.equals("Leste")) {
            for (Obstaculo obstaculo : listaObstaculo) {
                if (obstaculo.getY1() <= this.getPosicaoY() && obstaculo.getY2()>=this.getPosicaoY() && obstaculo.getX1() > this.getPosicaoX() && obstaculo.getAltura() >= this.altitude) {
                    obstaculosVistos.add(obstaculo);
                }
            }
            obstaculosVistos.sort(Comparator.comparingInt((Obstaculo o)-> o.getX1()));
        } else if (direcao.equals("Oeste")) {
            for (Obstaculo obstaculo : listaObstaculo) {
                if (obstaculo.getY1() <= this.getPosicaoY() && obstaculo.getY2()>=this.getPosicaoY() && obstaculo.getX1() < this.getPosicaoX() && obstaculo.getAltura() >= this.altitude) {
                    obstaculosVistos.add(obstaculo);
                }
            }
            obstaculosVistos.sort(Comparator.comparingInt((Obstaculo o)-> o.getX1()).reversed());
        }
        return obstaculosVistos;
    }

    /**
     * Sobrescreve o método da classe pai para exibir posição incluindo altitude
     * @return String formatada com nome e posição 3D do robô
     */
    @Override
    public String exibirPosicao() {
        return String.format("%s está em (%d, %d, %d)", super.getNome(), super.getPosicaoX(), super.getPosicaoY(),
                this.getAltitude());
    }
}
