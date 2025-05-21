package robos.terrestres;

import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import interfaces.Entidade;
import interfaces.TipoEntidade;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;

public class Correios extends RoboTerrestre {
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

    public String carregarPacote(String id, float peso) {
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

    public boolean moverEntrega(int deltaX, int deltaY, Ambiente ambiente) {
        // Verifica se o robô está dentro dos limites do ambiente
        int destinoX = getX() + deltaX > ambiente.getTamX() ? ambiente.getTamX() : getX() + deltaX;
        int destinoY = getY() + deltaY > ambiente.getTamY() ? ambiente.getTamY() : getY() + deltaY;

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            for (int x = getX() + passoX; x != destinoX + passoX; x += passoX) {
                Entidade obj = ambiente.identificarObjetoPosicao(x, getY(), 0);
                if (obj != null) {
                    if (obj.getTipo() == TipoEntidade.ROBO) {
                        System.out.print("O robô " + getNome() + " colidiu com o objeto: ");
                        System.out.println(((Robo) obj).getNome() + " na posição X:" + x + " Y:" + getY());
                        return false;
                    } else if (((Obstaculo) obj).getTipo() == TipoObstaculo.BURACO) {
                        return true;
                    } else {
                        System.out.print("O robô " + getNome() + " colidiu com o objeto: ");
                        System.out.println(((Obstaculo) obj).getTipo() + " na posição X:" + x + " Y:" + getY());
                        return false;
                    }
                }
                setPosicaoX(x);
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = getY() + passoY; y != destinoY + passoY; y += passoY) {
                Entidade obj = ambiente.identificarObjetoPosicao(getX(), y, 0);
                if (obj != null) {
                    if (obj.getTipo() == TipoEntidade.ROBO) {
                        System.out.print("O robô " + getNome() + " colidiu com o objeto: ");
                        System.out.println(((Robo) obj).getNome() + " na posição X:" + getX() + " Y:" + y);
                        return false;
                    } else if (((Obstaculo) obj).getTipo() == TipoObstaculo.BURACO) {
                        return true;
                    } else {
                        System.out.print("O robô " + getNome() + " colidiu com o objeto: ");
                        System.out.println(((Obstaculo) obj).getTipo() + " na posição X:" + getX() + " Y:" + y);
                        return false;
                    }
                }
                setPosicaoY(y);
            }
        }
        return true;
    }

    public String entregarPacote(String id, int destinoX, int destinoY, Ambiente ambiente) {
        if (!entregas.contains(id)) {
            return ("Pacote " + id + " não encontrado na carga.");
        }
        int i = entregas.indexOf(id);
        Entidade objeto_posicao = ambiente.identificarObjetoPosicao(destinoX, destinoY, 0);
        if (moverEntrega(destinoX - getX(), destinoY - getY(), ambiente)) {
            if (objeto_posicao.getTipo() == TipoEntidade.OBSTACULO && ((Obstaculo) objeto_posicao).getTipo() == TipoObstaculo.BURACO) {
                pesoAtual -= pesos.get(i);
                entregas.remove(i);
                pesos.remove(i);
                String texto = "Pacote " + id + " entregue na posição (" + destinoX + ", " + destinoY
                        + ").\nBuraco coberto na posição X1:" + ((Obstaculo) objeto_posicao).getX1() + " X2:"
                        + ((Obstaculo) objeto_posicao).getX2() + " Y1:" + ((Obstaculo) objeto_posicao).getY1()
                        + " Y2:" + ((Obstaculo) objeto_posicao).getY2();
                ambiente.removerObstaculo((Obstaculo) objeto_posicao);

                return texto;
            }

            pesoAtual -= pesos.get(i);
            entregas.remove(i);
            pesos.remove(i);
    
            return ("Pacote " + id + " entregue na posição (" + destinoX + ", " + destinoY + ").");
        }
        return "Entrega não concluída";
    }

    public String listarEntregas() {
        if (entregas.isEmpty()) {
            return "Não há entregas pendentes.";
        } else {
            return "Entregas pendentes: " + String.join(", ", entregas);
        }
    }
}
