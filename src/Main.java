public class Main{
    public static void main(String[] args) { 
        System.out.println("Criando ambiente de tamanho Largura=50 e Altura=50");
        Ambiente ambiente = new Ambiente(50,50);
        System.out.println("Criando Robô Geraldo na posição X=20 Y=20");
        Robo geraldo = new Robo("Geraldo", 20,20);

        System.out.println("Movendo Geraldo em deltaX=40 deltaY=10");
        geraldo.mover(40, 10);
        boolean resultado = ambiente.dentroDosLimites(geraldo.getPosicaoX(), geraldo.getPosicaoY());
        System.out.print("Dentro do limite? ");
        if (resultado) {
            System.out.println("Sim");
        } else {
            System.out.println("Não");
        }
        geraldo.exibirPosicao();

    }
}