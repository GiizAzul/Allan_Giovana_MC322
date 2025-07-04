package robos.aereos;

import java.util.ArrayList;

import robos.geral.*;
import robos.subsistemas.ModuloComunicacao;
import robos.subsistemas.movimento.ControleMovimentoAereo;
import robos.terrestres.RoboTerrestre;
import interfaces.*;
import excecoes.ambiente.ErroComunicacaoException;
import excecoes.ambiente.ForaDosLimitesException;
import excecoes.robos.especificos.VarreduraInvalidaException;
import excecoes.robos.gerais.ColisaoException;
import excecoes.robos.gerais.MovimentoInvalidoException;
import excecoes.robos.gerais.RoboDesligadoException;
import excecoes.robos.gerais.RoboDestruidoPorBuracoException;
import excecoes.sensor.SensorException;
import excecoes.logger.LoggerException;

import ambiente.*;

/**
 * Classe que representa um drone de vigilância aéreo com capacidades de
 * reconhecimento.
 * Estende RoboAereo e implementa a interface Comunicavel para troca de
 * mensagens.
 */
public class DroneVigilancia extends RoboAereo implements Comunicavel {

    private boolean camuflado; // Estado de camuflagem do drone
    private float alcance_radar; // Alcance do radar de detecção
    private float angulo_camera; // Ângulo de abertura da câmera para varredura

    private ModuloComunicacao moduloComunicacao;

    /**
     * Construtor do drone de vigilância com especificações completas
     * 
     * @param nome      Nome identificador do drone
     * @param dir       Direção inicial do drone (Norte/Sul/Leste/Oeste)
     * @param m         Material de construção do drone
     * @param x         Coordenada X inicial
     * @param y         Coordenada Y inicial
     * @param vel       Velocidade de movimento do drone
     * @param h         Altura inicial do drone
     * @param hmax      Altura máxima que o drone pode atingir
     * @param amb       Ambiente onde o drone opera
     * @param alc_rad   Alcance do radar de detecção
     * @param ang_radar Ângulo do radar de detecção
     * @param ang_cam   Ângulo de abertura da câmera
     */
    public DroneVigilancia(String nome, String dir, MateriaisRobo m, int x, int y, int vel, int h, int hmax,
            Ambiente amb, float alc_rad, float ang_radar, float ang_cam) {
        super(nome, dir, m, x, y, vel, h, hmax, amb, alc_rad, ang_radar);
        this.angulo_camera = ang_cam;
        this.camuflado = false;
        this.moduloComunicacao = new ModuloComunicacao(this);
    }

    /**
     * Executa tarefas específicas do drone de vigilância baseadas nos argumentos
     * fornecidos
     * Suporta varredura de área e controle de camuflagem
     * 
     * @param argumentos Array de argumentos variados dependendo da tarefa
     * @return String com o resultado da execução da tarefa
     * @throws MovimentoInvalidoException
     * @throws RoboDestruidoPorBuracoException
     */
    public String executarTarefa(Object... argumentos)
            throws RoboDestruidoPorBuracoException, MovimentoInvalidoException {
        String result = super.executarTarefa(argumentos);
        if (result != "") {
            return result;
        }
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "varrer":
                Ambiente ambiente = (Ambiente) argumentos[1];
                int centroX = (Integer) argumentos[2];
                int centroY = (Integer) argumentos[3];
                int raio = (Integer) argumentos[4];
                ArrayList<Entidade> objetos_encontrados;
                try {
                    objetos_encontrados = varrerArea(ambiente, centroX, centroY, raio);
                } catch (VarreduraInvalidaException | ForaDosLimitesException | RoboDestruidoPorBuracoException
                        | ColisaoException | SensorException | LoggerException e) {
                    return "Erro ao varrer a área: " + e.getMessage();
                }

                if (objetos_encontrados.isEmpty()) {
                    result += "Nenhum objeto foi encontrado!";
                } else {
                    result += "Objetos encontrados:\n";
                    for (Entidade obj : objetos_encontrados) {
                        if (obj instanceof Robo) {
                            Robo r = (Robo) obj;
                            result += String.format("Robô: %s, X: %d, Y: %d, Altura: %s\n",
                                    r.getNome(), r.getXInterno(), r.getYInterno(), r.getZInterno());
                        } else {
                            Obstaculo o = (Obstaculo) obj;
                            result += String.format("Obstáculo: %s, X1: %d, X2: %d, Y1: %d, Y2: %d, Altura: %d\n",
                                    o.getTipoObstaculo(), o.getX1(), o.getX2(), o.getY1(), o.getY2(),
                                    o.getAltura());
                        }
                    }
                }
                return result;

            case "camuflagem":
                if (isCamuflado())
                    desabilitarCamuflagem();
                else
                    acionarCamuflagem();
                return "";

            default:
                return "";
        }
    }

    /**
     * Executa varredura de uma área circular no ambiente
     * Move o drone para o centro da área e utiliza sua câmera para detectar
     * entidades
     * A eficácia depende da altura do drone e do ângulo da câmera
     * 
     * @param ambiente Ambiente onde realizar a varredura
     * @param centroX  Coordenada X do centro da área de varredura
     * @param centroY  Coordenada Y do centro da área de varredura
     * @param raio     Raio da área a ser varrida
     * @return Lista de entidades encontradas na área
     * @throws ForaDosLimitesException         Se a posição estiver fora dos limites
     * @throws RoboDestruidoPorBuracoException Se o drone cair em um buraco
     * @throws ColisaoException                Se houver colisão durante o movimento
     * @throws SensorException                 Se houver problemas com os sensores
     * @throws VarreduraInvalidaException      Se o raio for muito grande para o
     *                                         ângulo da câmera
     */
    private ArrayList<Entidade> varrerArea(Ambiente ambiente, int centroX, int centroY, int raio)
            throws ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException, SensorException, LoggerException,
            VarreduraInvalidaException {
        // Sistema de varredura, melhor quanto mais alto está o drone
        // Reposiciona o drone para o centro da varredura
        // Baseado na capacidade da câmera do drone
        // Sistema de varredura não encontra drones camuflados

        // Move o drone para ficar sobre a região central
        verificarGPSAtivo();

        ((ControleMovimentoAereo) this.controleMovimento).mover(this, centroX - this.getX(), centroY - this.getY(), ambiente);

        // Verifica se a câmera possui abertura para fazer a varredura
        double ang_rad = Math.toRadians(this.angulo_camera);
        if (raio > this.getZ() * Math.tan(ang_rad / 2)) {
            throw new VarreduraInvalidaException("Não é possível varrer tal raio com a câmera atual!");
        }

        ArrayList<Entidade> lista_entidades = ambiente.getEntidades();
        ArrayList<Entidade> objetos_encontrados = new ArrayList<>();
        for (Entidade entidade : lista_entidades) {
            if (entidade.getTipo() == TipoEntidade.ROBO) {
                Robo robo = (Robo) entidade;
                double distancia = robo.distanciaRobo(this);
                if (distancia > Math.sqrt(Math.pow(raio, 2) + Math.pow(this.getZ(), 2))) {
                    continue;
                }

                double hMax = this.getZ() - distancia * Math.cos(ang_rad);
                if (robo instanceof RoboAereo) {
                    RoboAereo roboAereo = (RoboAereo) robo;
                    if (roboAereo.getZ() > hMax) {
                        // Fora da região de visualização da câmera
                        continue;
                    }

                } else if (!(robo instanceof RoboTerrestre)) {
                    // RoboTerrestre automaticamente já está na região de varredura
                    System.out.printf("Robo %s não avaliado!\n", robo.getNome());
                }

                if (robo.getVisivel()) {
                    objetos_encontrados.add(robo);
                }
            } else if (entidade.getTipo() == TipoEntidade.ROBO) {
                Obstaculo obstaculo = (Obstaculo) entidade;
                double distancia = distanciaObstaculo(obstaculo);
                if (distancia > Math.sqrt(Math.pow(raio, 2) + Math.pow(this.getZ(), 2))) {
                    continue;
                }

                objetos_encontrados.add(obstaculo);

            }
        }

        return objetos_encontrados;

    }

    /**
     * Verifica se o drone está atualmente camuflado
     * 
     * @return true se estiver camuflado, false caso contrário
     */
    public boolean isCamuflado() {
        return this.camuflado;
    }

    /**
     * Ativa o sistema de camuflagem do drone, tornando-o invisível
     * Define o drone como não visível para outros sistemas de detecção
     */
    private void acionarCamuflagem() {
        this.camuflado = true;
        this.visivel = false;
    }

    /**
     * Desativa o sistema de camuflagem do drone, tornando-o visível novamente
     * Restaura a visibilidade do drone para outros sistemas de detecção
     */
    private void desabilitarCamuflagem() {
        this.visivel = true;
        this.camuflado = false;
    }

    /**
     * Retorna o alcance atual do radar de detecção
     * 
     * @return Alcance do radar em unidades de distância
     */
    public float getAlcanceRadar() {
        return alcance_radar;
    }

    /**
     * Define o alcance do radar de detecção
     * 
     * @param alcance_radar Novo alcance do radar
     */
    public void setAlcanceRadar(float alcance_radar) {
        this.alcance_radar = alcance_radar;
    }

    /**
     * Retorna o ângulo de abertura atual da câmera
     * 
     * @return Ângulo da câmera em graus
     */
    public float getAnguloCamera() {
        return angulo_camera;
    }

    /**
     * Define o ângulo de abertura da câmera
     * 
     * @param angulo_camera Novo ângulo da câmera em graus
     */
    public void setAnguloCamera(float angulo_camera) {
        this.angulo_camera = angulo_camera;
    }

    /**
     * Envia uma mensagem para outro robô comunicável através da central de
     * comunicação
     * Registra a mensagem na central e notifica o destinatário
     * 
     * @param destinatario Robô que receberá a mensagem
     * @param mensagem     Conteúdo da mensagem a ser enviada
     * @param central      Central de comunicação para registro da mensagem
     * @return String confirmando o envio e resposta do destinatário
     * @throws ErroComunicacaoException Se houver problemas na comunicação
     * @throws RoboDesligadoException   Se o robô estiver desligado
     */
    public String enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central)
            throws ErroComunicacaoException, RoboDesligadoException {
        return this.moduloComunicacao.enviarMensagem(destinatario, mensagem, central);
    }

    /**
     * Recebe uma mensagem de outro robô comunicável
     * Verifica se o robô está ligado antes de processar a mensagem
     * 
     * @param remetente Robô que enviou a mensagem
     * @param mensagem  Conteúdo da mensagem recebida
     * @return String confirmando o recebimento da mensagem
     * @throws ErroComunicacaoException Se houver problemas na comunicação
     * @throws RoboDesligadoException   Se o robô estiver desligado
     */
    public String receberMensagem(Comunicavel remetente, String mensagem)
            throws ErroComunicacaoException, RoboDesligadoException {
        return this.moduloComunicacao.receberMensagem(remetente, mensagem);
    }

    public String executarMissao(Ambiente a) {
        if (temMissao()) {
            String resultado = "Drone de Vigiância " + getNome() + " iniciando execução da missão...\n";
            resultado+=missao.executar(this, a, this.getLogger());
            resultado+="\nDrone de Vigiância " + getNome() + " finalizou a missão.";
            return resultado;
        } else {
            return "Drone de Vigiância " + getNome() + " não possui uma missão para executar.";
        }
    }
}