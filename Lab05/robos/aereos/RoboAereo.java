package robos.aereos;
import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import excecoes.robos.gerais.ColisaoException;
import excecoes.robos.gerais.MovimentoInvalidoException;
import excecoes.robos.gerais.RoboDestruidoPorBuracoException;
import excecoes.sensor.*;
import interfaces.*;
import robos.equipamentos.sensores.*;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.subsistemas.movimento.ControleMovimentoAereo;

/**
 * Classe que representa um robô aéreo, especialização de Robo
 * com capacidade de voar em diferentes altitudes
 */
public class RoboAereo extends Robo implements Identificantes {

    private int altitudeMaxima = -1;  // Altitude máxima que o robô pode atingir

    /**
     * Construtor de RoboAereo com parâmetros básicos
     * Inicializa sensores de barômetro e radar com configurações padrão
     * @param n Nome do Robô
     * @param d Direção inicial
     * @param m Material de construção do robô
     * @param x Posição X inicial
     * @param y Posição Y inicial
     * @param vel Velocidade de movimento
     * @param h Altura inicial
     * @param hmax Altura máxima permitida
     * @param ambiente Ambiente onde o robô opera
     */
    public RoboAereo(String n, String d, MateriaisRobo m, int x, int y, int vel, int h, int hmax, Ambiente ambiente) {
        super(n, d, m, x, y, h, vel, new ControleMovimentoAereo());
        this.altitudeMaxima = hmax;
        super.gerenciadorSensores.adicionarSensor(new Barometro(this));
        super.gerenciadorSensores.adicionarSensor(new Radar(this, ambiente, 100, 30));
  
    }

    /**
     * Construtor de RoboAereo com configuração personalizada de radar
     * Permite especificar alcance e ângulo do radar
     * @param n Nome do Robô
     * @param d Direção inicial
     * @param m Material de construção do robô
     * @param x Posição X inicial
     * @param y Posição Y inicial
     * @param vel Velocidade de movimento
     * @param h Altura inicial
     * @param hmax Altura máxima permitida
     * @param ambiente Ambiente onde o robô opera
     * @param alc_radar Alcance do radar em unidades de distância
     * @param ang_radar Ângulo de abertura do radar em graus
     */
    public RoboAereo(String n, String d, MateriaisRobo m, int x, int y, int vel, int h, int hmax, Ambiente ambiente, float alc_radar, float ang_radar) {
        super(n, d, m, x, y, h, vel, new ControleMovimentoAereo());
        this.altitudeMaxima = hmax;
        super.gerenciadorSensores.adicionarSensor(new Barometro(this));
        super.gerenciadorSensores.adicionarSensor(new Radar(this, ambiente, alc_radar, ang_radar));
    }
    
    /**
     * Realiza o movimento vertical (eixo Z) do robô aéreo passo a passo
     * Verifica colisões e limites de altitude durante o movimento
     * @param passo O incremento (positivo) ou decremento (negativo) a ser aplicado em cada passo
     * @param metros A distância total a ser percorrida
     * @param ambiente O ambiente em que o robô está se movendo, usado para verificar colisões
     * @throws SensorException Se houver problemas com os sensores durante o movimento
     * @throws MovimentoInvalidoException Se o movimento exceder limites de altitude
     * @throws ColisaoException Se houver colisão com outros objetos
     */
    private void movimentoZ(int passo, int metros, Ambiente ambiente) throws SensorException, MovimentoInvalidoException, ColisaoException {
        // Calcula a altitude alvo baseada na direção do movimento
        int altitudeAlvo = passo > 0 ? getZ() + metros : getZ() - metros;
        
        // Impede que a altitude alvo seja negativa (abaixo do solo)
        if (altitudeAlvo < 0) {
            throw new MovimentoInvalidoException("Não é possível descer abaixo do nível do solo.");
        }
        
        // Impede que a altitude alvo exceda a altitude máxima
        if (altitudeAlvo > this.altitudeMaxima) {
            throw new MovimentoInvalidoException("Altura máxima permitida é " + this.altitudeMaxima + " metros.");
        }
        
        // Realiza o movimento passo a passo
        for (int z = getZ() + passo; z != altitudeAlvo + passo; z += passo) {
            Object obj = ambiente.identificarEntidadePosicao(getX(), getY(), z);
            if (obj != null) {
                if (obj instanceof Robo) {
                    throw new ColisaoException(((Robo) obj).getNome() + " na posição X:" + getX() +
                            " Y:" + getY() + " Z:" + z);
                } else {
                    throw new ColisaoException(((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + getX() +
                            " Y:" + getY() + " Z:" + z);
                }
            }
            ambiente.moverEntidade(this, getX(), getY(), z);

            setPosicaoZ(z);
        }
    }

    /**
     * Aumenta a altitude do robô aéreo de forma controlada
     * @param metros Quantidade de metros a subir
     * @param ambiente Ambiente onde o robô se encontra
     * @throws SensorException Se houver problemas com os sensores
     * @throws ColisaoException Se houver colisão durante a subida
     * @throws MovimentoInvalidoException Se exceder a altitude máxima
     */
    public void subir(int metros, Ambiente ambiente) throws SensorException, ColisaoException, MovimentoInvalidoException {
        this.movimentoZ(1, metros, ambiente);
    }

    /**
     * Diminui a altitude do robô aéreo de forma controlada
     * @param metros Quantidade de metros a descer
     * @param ambiente Ambiente onde o robô se encontra
     * @throws SensorException Se houver problemas com os sensores
     * @throws ColisaoException Se houver colisão durante a descida
     * @throws MovimentoInvalidoException Se tentar descer abaixo do solo
     */
    public void descer(int metros, Ambiente ambiente) throws SensorException, ColisaoException, MovimentoInvalidoException {
        this.movimentoZ(-1, metros, ambiente);
    }

    /**
     * Obtém a altitude máxima configurada para o robô
     * @return Altitude máxima em metros
     */
    public int getAltitudeMaxima() {
        return this.altitudeMaxima;
    }

    /**
     * Obtém a pressão atmosférica atual através do barômetro
     * @return Pressão atmosférica medida pelo sensor
     * @throws SensorException Se houver problemas com o sensor barômetro
     */
    public double getPressao() throws SensorException {
        return super.gerenciadorSensores.getPressaoAtmosferica();
    }

    /**
     * Define uma nova altitude máxima para o robô
     * @param hMax Nova altitude máxima em metros
     */
    public void setAltitudeMaxima(int hMax) {
        this.altitudeMaxima = hMax;
    }

    /**
     * Calcula a distância euclidiana 3D entre este robô e outro robô
     * Considera as coordenadas X, Y e Z (altitude) para o cálculo
     * @param alvo Robô alvo para calcular a distância
     * @return Distância euclidiana 3D em unidades de ambiente
     * @throws SensorException Se houver problemas com o GPS
     */
    public double distanciaRobo(Robo alvo) throws SensorException {
        verificarGPSAtivo();

        return Math.sqrt(Math.pow(alvo.getXInterno() - this.getX(), 2)
                + Math.pow(alvo.getYInterno() - this.getY(), 2)
                + Math.pow(alvo.getZ() - this.getZ(), 2));
    }

    /**
     * Calcula a distância euclidiana entre este robô e um obstáculo
     * Usa o ponto central do obstáculo para o cálculo
     * @param obstaculo Obstáculo alvo para calcular a distância
     * @return Distância euclidiana até o obstáculo
     */
    public double distanciaObstaculo(Obstaculo obstaculo) {
        // Calcula o centro do obstáculo
        double centroX = (obstaculo.getX1() + obstaculo.getX2()) / 2.0;
        double centroY = (obstaculo.getY1() + obstaculo.getY2()) / 2.0;
        double centroZ = obstaculo.getAltura() / 2.0;

        return Math.sqrt(Math.pow(centroX - this.getXInterno(), 2)
                + Math.pow(centroY - this.getYInterno(), 2)
                + Math.pow(centroZ - this.getZInterno(), 2));
    }

    /**
     * Move o robô aéreo para uma nova posição 3D no ambiente
     * Realiza movimento sequencial nos eixos X, Y e depois Z
     * Verifica colisões a cada passo do movimento
     * @param X Nova coordenada X
     * @param Y Nova coordenada Y
     * @param Z Nova altitude
     * @param ambiente Ambiente onde o robô se encontra
     * @throws SensorException Se houver problemas com sensores
     * @throws ColisaoException Se houver colisão durante o movimento
     * @throws MovimentoInvalidoException Se o movimento for inválido
     */
    public void mover(int X, int Y, int Z, Ambiente ambiente) throws SensorException, ColisaoException, MovimentoInvalidoException {
        ((ControleMovimentoAereo) this.controleMovimento).mover(this, X, Y, Z, ambiente);
    }

    /**
     * Identifica robôs no alcance do radar
     * Filtra entidades detectadas pelo radar para retornar apenas robôs
     * @return Lista de robôs detectados pelo sensor radar
     * @throws SensorException Se houver problemas com o sensor radar
     */
    @Override
    public ArrayList<Robo> identificarRobo() throws SensorException {
        ArrayList<Entidade> objetosEncontrados = super.gerenciadorSensores.identificarComRadar();
        ArrayList<Robo> robosEncontrados = new ArrayList<Robo>();
        for (Entidade objectEnc : objetosEncontrados) {
            if (objectEnc.getTipo() == TipoEntidade.ROBO) {
                robosEncontrados.add((Robo) objectEnc);
            }
        }
        return robosEncontrados;
    }

    /**
     * Identifica obstáculos no alcance do radar
     * Filtra entidades detectadas pelo radar para retornar apenas obstáculos
     * @return Lista de obstáculos detectados pelo sensor radar
     * @throws SensorException Se houver problemas com o sensor radar
     */
    public ArrayList<Obstaculo> identificarObstaculo() throws SensorException {
        ArrayList<Entidade> objetosEncontrados = super.gerenciadorSensores.identificarComRadar();
        ArrayList<Obstaculo> obstaculosEncontrados = new ArrayList<Obstaculo>();
        for (Entidade objectEnc : objetosEncontrados) {
            if (objectEnc.getTipo() == TipoEntidade.OBSTACULO) {
                obstaculosEncontrados.add((Obstaculo) objectEnc);
            }
        }
        return obstaculosEncontrados;
    }

    /**
     * Retorna o sensor radar do robô aéreo
     * @return Instância do sensor radar
     */
    public Radar getRadar() {
        return super.gerenciadorSensores.getRadar();
    }

    /**
     * Retorna o sensor barômetro do robô aéreo
     * @return Instância do sensor barômetro
     */
    public Barometro getBarometro() {
        return super.gerenciadorSensores.getBarometro();
    }

    /**
     * Executa tarefas específicas de robôs aéreos baseadas nos argumentos fornecidos
     * Suporta movimento 3D e identificação de objetos via radar
     * @param argumentos Array de argumentos variados dependendo da tarefa
     * @return String com o resultado da execução da tarefa
     * @throws MovimentoInvalidoException 
     * @throws RoboDestruidoPorBuracoException 
     */
    public String executarTarefa(Object... argumentos) throws RoboDestruidoPorBuracoException, MovimentoInvalidoException {
        String result = super.executarTarefa(argumentos);
        if (result != ""){
            return result;
        }
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "mover":
                int x = (Integer) argumentos[1];
                int y = (Integer) argumentos[2];
                int z = (Integer) argumentos[3];
                Ambiente ambiente = (Ambiente) argumentos[4];
                try {
                    mover(x, y, z, ambiente);
                } catch (SensorException | ColisaoException | MovimentoInvalidoException e) {
                    return "Erro ao mover o robô: " + e.getMessage();
                }
                return "";
                
            case "identificar":
                ArrayList<Obstaculo> listaoObstaculos;
                ArrayList<Robo> listaoRobos;
                try {
                    listaoObstaculos = identificarObstaculo();
                    listaoRobos = identificarRobo();
                } catch (SensorException e) {
                    return "Erro ao identificar objetos: " + e.getMessage();
                }

                if (listaoObstaculos.isEmpty() && listaoRobos.isEmpty()) {
                    return "Nenhum objeto encontrado!";
                } else {
                    for (Obstaculo o : listaoObstaculos) {
                        result += String.format(
                                "Obstáculo encontrado: %s, X1: %d, X2: %d, Y1: %d, Y2: %d, Altura: %d\n",
                                o.getTipoObstaculo(), o.getX1(), o.getX2(), o.getY1(), o.getY2(),
                                o.getAltura());
                    }
                    for (Robo r : listaoRobos) {
                        result += String.format("Robô encontrado: %s, X: %d, Y: %d, Z: %d\n",
                                r.getNome(), r.getXInterno(), r.getYInterno(), r.getZInterno());
                    }
                }
                return result;

            default:
                return "";
        }
    }

}
