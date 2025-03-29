import java.util.ArrayList;

public class Correios extends RoboTerrestre {
    private int capacidadeMax;
    private float pesoMax;
    private float pesoAtual;
    private ArrayList<String> entregas;
    private ArrayList<Float> pesos;

    public Correios(String nome, String direcao, int posicaoX, int posicaoY, int velocidadeMaxima, int capacidadeMax, float pesoMax) {
        super(nome, direcao, posicaoX, posicaoY, velocidadeMaxima);
        this.capacidadeMax=capacidadeMax;
        this.pesoMax=pesoMax;
        pesoAtual=0;
        entregas = new ArrayList<String>();
        pesos = new ArrayList<Float>();
        setIntegridade(50);
    }
    
    public String carregarPacote(String id, float peso){
        if(entregas.size() >= capacidadeMax){
            return "Não há espaço para mais pacotes";
        }
        if(pesoAtual+peso>pesoMax){
            return "Peso excede a capacidade máxima";
        }
        entregas.add(id);
        pesos.add(peso);
        pesoAtual+=peso;

        return "Pacote carregado com sucesso";
    }

    public String entregarPacote(String id, int destinoX, int destinoY){
        if (!entregas.contains(id)) {
            return("Pacote " + id + " não encontrado na carga.");
        }
        int i = entregas.indexOf(id);
        mover(destinoX - getPosicaoX(), destinoY - getPosicaoY(), 20);
        pesoAtual -= pesos.get(i);
        entregas.remove(i);
        pesos.remove(i);

        return("Pacote " + id + " entregue na posição (" + destinoX + ", " + destinoY + ").");
    }

    public String listarEntregas() {
        if (entregas.isEmpty()) {
            return "Não há entregas pendentes.";
        } else {
            return "Entregas pendentes: " + String.join(",", entregas);
        }
    }
}
