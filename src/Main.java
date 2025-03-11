public class Main{
    public static void main(String[] args) {
        Ambiente ambiente = new Ambiente(50,50);
        Robo geraldo = new Robo("Geraldo", 20,20);

        geraldo.mover(40, 10);
        boolean resultado = ambiente.dentroDosLimites(geraldo.getPosicaoX(), geraldo.getPosicaoY());
        System.out.println(resultado);
        geraldo.exibirPosicao();

    }
}