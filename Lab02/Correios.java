import java.util.ArrayList;

public class Correios extends RoboTerrestre {
    private int capacidadeMax;
    private int cargaAtual;
    private float pesoMax;
    private float pesoAtual;
    private int[] entregas;
    private float[] pesos;

    public Correios(String nome, String direcao, int posicaoX, int posicaoY, int velocidadeMaxima, int capacidadeMax, float pesoMax) {
        super(nome, direcao, posicaoX, posicaoY, velocidadeMaxima);
        this.capacidadeMax=capacidadeMax;
        cargaAtual=0;
        this.pesoMax=pesoMax;
        pesoAtual=0;
        entregas = new int[capacidadeMax];
        pesos = new float[capacidadeMax];
    }
    
    public String carregarPacote(int id, float peso){
        if(cargaAtual >= capacidadeMax){
            return "Não há espaço para mais pacotes";
        }
        if(pesoAtual+peso>pesoMax){
            return "Peso excede a capacidade máxima";
        }
        entregas[cargaAtual]=id;
        pesos[cargaAtual]=peso;
        pesoAtual+=peso;
        cargaAtual++;

        return "Pacote carregado com sucesso";
    }

    public void entregarPacote(int id, int destinoX, int destinoY){
        mover(destinoX - getPosicaoX(), destinoY - getPosicaoY(), 20);
        for(int i=0;i<cargaAtual;i++){
            if (entregas[i] == id){
                pesoAtual -= pesos[i];
                for (int j = i; j < cargaAtual - 1; j++) {
                    entregas[j] = entregas[j + 1];
                    pesos[j] = pesos[j + 1];
                }
                cargaAtual--;
            }
        }
    }

    public String listarEntregas() {
        if (cargaAtual == 0) {
            return "Entregas pendentes: nenhuma";
        }
    
        String resultado = "Entregas pendentes: ";
        for (int i = 0; i < cargaAtual; i++) {
            resultado += entregas[i];
            if (i < cargaAtual - 1) {
                resultado += ", ";
            }
        }
        return resultado;
    }
}
