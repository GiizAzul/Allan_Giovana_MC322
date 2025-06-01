package robos.terrestres;

import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.CentralComunicacao;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import excecoes.*;
import interfaces.*;

public class Correios extends RoboTerrestre implements Comunicavel {
    private int capacidadeMax;
    private float pesoMax;
    private float pesoAtual;
    private ArrayList<String> entregas;
    private ArrayList<Float> pesos;

    public Correios(String nome, String direcao, Ambiente ambiente, MateriaisRobo material, int posicaoX, int posicaoY, int velocidade, int velocidadeMaxima, int capacidadeMax,
            float pesoMax) {
        super(nome, direcao, ambiente, material, posicaoX, posicaoY, velocidade, velocidadeMaxima);
        this.capacidadeMax = capacidadeMax;
        this.pesoMax = pesoMax;
        pesoAtual = 0;
        entregas = new ArrayList<String>();
        pesos = new ArrayList<Float>();
        setIntegridade(50);
    }

    public String executarTarefa(Object... argumentos) throws AlvoInvalidoException, MunicaoInsuficienteException, SensorInativoException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        String result = super.executarTarefa(argumentos);
        if (result != ""){
            return result;
        }
        String tarefa = (String) argumentos[0];
        switch (tarefa) {
            case "carregar":
                String id = (String) argumentos[1];
                float peso = (Float) argumentos[2];
                return carregarPacote(id, peso);  

            case "entregar":
                id = (String) argumentos[1];
                int destinoX = (Integer) argumentos[2];    
                int destinoY = (Integer) argumentos[3];
                Ambiente ambiente = (Ambiente) argumentos[4];
                
                try {
                    return entregarPacote(id, destinoX, destinoY, ambiente);
                } catch (SensorInativoException e) {
                    return "Erro ao entregar pacote: " + e.getMessage();
                }
            
            case "listar":
                return listarEntregas();
            default:
                return "";
        }
    }

    private String carregarPacote(String id, float peso) {
        if (entregas.size() >= capacidadeMax) {
            return "Não há espaço para mais pacotes";
        }
        if (pesoAtual + peso > pesoMax) {
            return "Peso excede a capacidade máxima";
        }
        entregas.add(id);
        pesos.add(peso);
        pesoAtual += peso;

        return "Pacote carregado com sucesso";
    }

    private boolean moverEntrega(int deltaX, int deltaY, Ambiente ambiente) throws SensorInativoException {
        // Verifica se o robô está dentro dos limites do ambiente
        int destinoX = getX() + deltaX > ambiente.getTamX() ? ambiente.getTamX() : getX() + deltaX;
        int destinoY = getY() + deltaY > ambiente.getTamY() ? ambiente.getTamY() : getY() + deltaY;

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            for (int x = getX() + passoX; x != destinoX + passoX; x += passoX) {
                Entidade obj = ambiente.identificarEntidadePosicao(x, getY(), 0);
                if (obj != null) {
                    if (obj.getTipo() == TipoEntidade.ROBO) {
                        System.out.print("O robô " + getNome() + " colidiu com o objeto: ");
                        System.out.println(((Robo) obj).getNome() + " na posição X:" + x + " Y:" + getY());
                        return false;
                    } else if (((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        return true;
                    } else {
                        System.out.print("O robô " + getNome() + " colidiu com o objeto: ");
                        System.out.println(((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + x + " Y:" + getY());
                        return false;
                    }
                }
                ambiente.moverEntidade(this,x,getY(),getZ());
                setPosicaoX(x);
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = getY() + passoY; y != destinoY + passoY; y += passoY) {
                Entidade obj = ambiente.identificarEntidadePosicao(getX(), y, 0);
                if (obj != null) {
                    if (obj.getTipo() == TipoEntidade.ROBO) {
                        System.out.print("O robô " + getNome() + " colidiu com o objeto: ");
                        System.out.println(((Robo) obj).getNome() + " na posição X:" + getX() + " Y:" + y);
                        return false;
                    } else if (((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        return true;
                    } else {
                        System.out.print("O robô " + getNome() + " colidiu com o objeto: ");
                        System.out.println(((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + getX() + " Y:" + y);
                        return false;
                    }
                }
                ambiente.moverEntidade(this,getX(),y,getZ());
                setPosicaoY(y);
            }
        }
        return true;
    }

    private String entregarPacote(String id, int destinoX, int destinoY, Ambiente ambiente) throws SensorInativoException {
        if (!entregas.contains(id)) {
            return ("Pacote " + id + " não encontrado na carga.");
        }
        int i = entregas.indexOf(id);
        Entidade objeto_posicao = ambiente.identificarEntidadePosicao(destinoX, destinoY, 0);
        if (moverEntrega(destinoX - getX(), destinoY - getY(), ambiente)) {
            if (objeto_posicao != null && objeto_posicao.getTipo() == TipoEntidade.OBSTACULO && ((Obstaculo) objeto_posicao).getTipoObstaculo() == TipoObstaculo.BURACO) {
                pesoAtual -= pesos.get(i);
                entregas.remove(i);
                pesos.remove(i);
                String texto = "Pacote " + id + " entregue na posição (" + destinoX + ", " + destinoY
                        + ").\nBuraco coberto na posição X1:" + ((Obstaculo) objeto_posicao).getX1() + " X2:"
                        + ((Obstaculo) objeto_posicao).getX2() + " Y1:" + ((Obstaculo) objeto_posicao).getY1()
                        + " Y2:" + ((Obstaculo) objeto_posicao).getY2();
                ambiente.removerEntidade(objeto_posicao);

                return texto;
            }

            pesoAtual -= pesos.get(i);
            entregas.remove(i);
            pesos.remove(i);
    
            return ("Pacote " + id + " entregue na posição (" + destinoX + ", " + destinoY + ").");
        }
        return "Entrega não concluída";
    }

    private String listarEntregas() {
        if (entregas.isEmpty()) {
            return "Não há entregas pendentes.";
        } else {
            return "Entregas pendentes: " + String.join(", ", entregas);
        }
    }

    public String enviarMensagem(Comunicavel destinatario, String mensagem, CentralComunicacao central) throws  ErroComunicacaoException, RoboDesligadoException{
        if (destinatario == null){
            throw new ErroComunicacaoException("Destinatário não pode ser nulo");
        }
        if (!getEstado()){
            throw new RoboDesligadoException("Robô " + getNome() + " está desligado e não pode enviar mensagens.");
        }

        try {
            central.registrarMensagem(this, destinatario, mensagem);
            return "Mensagem enviada com sucesso por " + this.getNome() + "\n" + destinatario.receberMensagem(this, mensagem);
        } catch (RoboDesligadoException | ErroComunicacaoException e) {
            throw new ErroComunicacaoException("Falha ao enviar mensagem: " + e.getMessage());
        }
    }

    public String receberMensagem(Comunicavel remetente, String mensagem) 
            throws ErroComunicacaoException, RoboDesligadoException {
        
        if (!getEstado()) {
            throw new RoboDesligadoException("Robô " + getNome() + " está desligado e não pode receber mensagens.");
        }

        if (remetente == null) {
            throw new ErroComunicacaoException("Remetente não pode ser nulo.");
        }

        // A mensagem já foi registrada pela central no método enviarMensagem
        // Aqui podemos adicionar lógica adicional de processamento da mensagem
        return getNome() + " recebeu mensagem de " + ((Robo)remetente).getNome() + ": " + mensagem;
    }
}
