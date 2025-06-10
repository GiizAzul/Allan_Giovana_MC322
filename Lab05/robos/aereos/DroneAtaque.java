package robos.aereos;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import interfaces.Atacante;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import excecoes.robos.especificos.AlvoInvalidoException;
import excecoes.robos.especificos.MunicaoInsuficienteException;
import excecoes.robos.gerais.MovimentoInvalidoException;
import excecoes.robos.gerais.RoboDestruidoPorBuracoException;
import excecoes.sensor.*;

/**
 * Classe que representa um drone de ataque aéreo com capacidades ofensivas.
 * Estende RoboAereo e implementa a interface Atacante para realizar ataques.
 */
public class DroneAtaque extends RoboAereo implements Atacante {

    private int municao;    // Quantidade atual de munição disponível
    private int alcance;    // Alcance máximo de ataque do drone
    private int escudo;     // Pontos de escudo para proteção adicional

    /**
     * Construtor do drone de ataque com especificações completas
     * @param nome Nome identificador do drone
     * @param dir Direção inicial do drone (Norte/Sul/Leste/Oeste)
     * @param m Material de construção do drone
     * @param x Coordenada X inicial
     * @param y Coordenada Y inicial
     * @param vel Velocidade de movimento do drone
     * @param alt Altitude inicial do drone
     * @param altMax Altitude máxima que o drone pode atingir
     * @param amb Ambiente onde o drone opera
     * @param muni Quantidade inicial de munição
     * @param alc Alcance máximo de ataque
     */
    public DroneAtaque(String nome, String dir, MateriaisRobo m, int x, int y, int vel, int alt, int altMax,
            Ambiente amb,
            int muni, int alc) {

        super(nome, dir, m, x, y, vel, alt, altMax, amb);
        this.municao = muni;
        this.alcance = alc;
        this.escudo = 100;
    }

    /**
     * Processa o dano recebido, considerando o sistema de escudo do drone
     * O escudo absorve dano primeiro, depois a integridade do robô
     * @param dano Quantidade de dano a ser aplicada
     * @param ambiente Referência ao ambiente para remoção se destruído
     * @return String descrevendo o resultado da defesa
     */
    @Override
    public String defender(int dano, Ambiente ambiente) {
        if (this.escudo > 0) {
            if (this.escudo > dano) {
                this.escudo -= dano;
                return String.format("O robô %s defendeu o dano em seu escudo!\n", this.getNome());
            } else {
                dano -= this.escudo;
                this.setIntegridade(this.getIntegridade() - dano);
                return String.format("O robô %s defendeu parcialmente com o escudo mas sofreu danos!\n",
                        this.getNome());
            }
        } else {
            this.setIntegridade(this.getIntegridade() - dano);
            if (this.getEstado()) {
                return String.format("O robô %s está desligado devido ao dano tomado", this.getNome());
            } else {
                return String.format("O robô %s ainda está ligado", this.getNome());
            }
        }
    }

    /**
     * Executa tarefas específicas do drone de ataque baseadas nos argumentos fornecidos
     * Suporta ataques por coordenadas e ataques diretos em robôs
     * @param argumentos Array de argumentos variados dependendo da tarefa
     * @return String com o resultado da execução da tarefa
     * @throws MovimentoInvalidoException 
     * @throws RoboDestruidoPorBuracoException 
     */
    public String executarTarefa(Object... argumentos) throws RoboDestruidoPorBuracoException, MovimentoInvalidoException {
        String result = super.executarTarefa(argumentos);
        if (result != "") {
            return result;
        }
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "atirar coord":
                int alvoX = (Integer) argumentos[1];
                int alvoY = (Integer) argumentos[2];
                int alvoZ = (Integer) argumentos[3];
                int nTiros = (Integer) argumentos[4];
                Ambiente ambiente = (Ambiente) argumentos[5];
                try {
                    return atirar(alvoX, alvoY, alvoZ, nTiros, ambiente);
                } catch (SensorException | MunicaoInsuficienteException | AlvoInvalidoException e) {
                    return e.getMessage();
                }

            case "atirar robo":
                Robo robo = (Robo) argumentos[1];
                nTiros = (Integer) argumentos[2];
                ambiente = (Ambiente) argumentos[3];
                try {
                    return atirar(robo, nTiros, ambiente);
                } catch (SensorException | MunicaoInsuficienteException | AlvoInvalidoException e) {
                    return e.getMessage();
                }

            default:
                return "";
        }

    }

    /**
     * Executa um ataque contra coordenadas específicas no ambiente
     * Verifica munição, alcance e validade do alvo antes de executar
     * @param alvoX Coordenada X do alvo
     * @param alvoY Coordenada Y do alvo
     * @param alvoZ Coordenada Z (altura) do alvo
     * @param nTiros Número de tiros a serem disparados
     * @param ambiente Referência ao ambiente onde ocorre o ataque
     * @return String descrevendo o resultado do ataque
     * @throws SensorException Se houver problemas com GPS/sensores
     * @throws MunicaoInsuficienteException Se não houver munição suficiente
     * @throws AlvoInvalidoException Se o alvo for inválido (próprio robô)
     */
    public String atirar(int alvoX, int alvoY, int alvoZ, int nTiros, Ambiente ambiente) throws SensorException, MunicaoInsuficienteException, AlvoInvalidoException {

        verificarGPSAtivo();

        if (this.municao < nTiros) {
            throw new MunicaoInsuficienteException();
        } 
        if (alvoX == getXInterno() && alvoY == getYInterno() && alvoZ == getZInterno()) {
            throw new AlvoInvalidoException("Não é possível atirar no próprio robô");
        } 

        int dX = this.getX() - alvoX;
        int dY = this.getY() - alvoY;
        int dZ = this.getZ() - alvoZ;

        return executarTiro(dX, dY, dZ, alvoX, alvoY, alvoZ, nTiros, ambiente);

    }

    /**
     * Executa um ataque contra outro robô específico
     * Obtém as coordenadas do robô alvo e delega para o método de ataque por coordenadas
     * @param robo Robô alvo a ser atacado
     * @param nTiros Número de tiros a serem disparados
     * @param ambiente Referência ao ambiente onde ocorre o ataque
     * @return String descrevendo o resultado do ataque
     * @throws SensorException Se houver problemas com GPS/sensores
     * @throws MunicaoInsuficienteException Se não houver munição suficiente
     * @throws AlvoInvalidoException Se o alvo for inválido
     */
    private String atirar(Robo robo, int nTiros, Ambiente ambiente)
    throws SensorException, MunicaoInsuficienteException, AlvoInvalidoException {
        verificarGPSAtivo();
        if (this.municao < nTiros) {
            throw new MunicaoInsuficienteException();
        }

        return this.atirar(robo.getX(), robo.getY(), robo.getZ(), nTiros, ambiente);
    }

    /**
     * Executa o disparo propriamente dito após validações
     * Calcula distância, verifica alcance, consome munição e aplica dano ao alvo
     * @param dX Diferença na coordenada X entre drone e alvo
     * @param dY Diferença na coordenada Y entre drone e alvo
     * @param dZ Diferença na coordenada Z entre drone e alvo
     * @param aX Coordenada X absoluta do alvo
     * @param aY Coordenada Y absoluta do alvo
     * @param aZ Coordenada Z absoluta do alvo
     * @param nTiros Número de tiros disparados
     * @param ambiente Ambiente onde ocorre o ataque
     * @return String descrevendo o resultado detalhado do ataque
     */
    private String executarTiro(int dX, int dY, int dZ, int aX, int aY, int aZ, int nTiros, Ambiente ambiente) {
        if (Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2) <= Math.pow(this.alcance, 2)) {
            this.municao -= nTiros;
            Object alvo = ambiente.identificarEntidadePosicao(aX, aY, aZ);
            if (alvo == null) {
                return String.format("Disparado realizado nas coordenadas (%d, %d, %d)\nNenhum alvo foi atingido!\n",
                        aX, aY, aZ);
            } else {
                if (alvo instanceof Robo) {
                    Robo alvodef = (Robo) alvo;
                    String defesa = alvodef.defender(nTiros, ambiente);
                    String result = String.format(
                            "Disparo realizado no alvo (%d, %d, %d)\n" +
                                    "Robô %s foi atingido!\n" +
                                    defesa,
                            aX, aY, aZ, alvodef.getNome());
                    return result;
                } else {
                    Obstaculo alvodef = (Obstaculo) alvo;
                    String defesa = alvodef.defender(nTiros, ambiente);
                    String result = String.format(
                            "Disparo realizado no alvo (%d, %d, %d)\n" +
                                    "Obstáculo %s foi atingido!\n" +
                                    defesa,
                            aX, aY, aZ, alvodef.getTipoObstaculo());
                    return result;
                }
            }

        } else {
            return "Alvo fora do alcance";
        }
    }

    /**
     * Recarrega a munição do drone adicionando o número especificado de projéteis
     * @param nBalas Número de projéteis a serem adicionados ao arsenal
     * @return String confirmando a conclusão do recarregamento
     */
    public String recarregar(int nBalas) {
        municao += nBalas;
        return "Recarregamento concluido";
    }

    public String executarMissao(Ambiente a) {
        if (temMissao()) {
            String resultado = "Drone de Ataque " + getNome() + " iniciando execução da missão...\n";
            resultado+=missao.executar(this, a);
            resultado+="\nDrone de Ataque " + getNome() + " finalizou a missão.";
            return resultado;
        } else {
            return "Drone de Ataque " + getNome() + " não possui uma missão para executar.";
        }

    }

}