public class Main{
    public static void main(String[] args) { 
        Ambiente ambiente = new Ambiente(50,50);

        TanqueGuerra geraldo = new TanqueGuerra("Geraldo", "Norte", 20, 20, 50, "Sei la", 50, 50);
        geraldo.mover(40, 10, 30);
        boolean resultado = ambiente.dentroDosLimites(geraldo.getPosicaoX(), geraldo.getPosicaoY());
        System.out.print("Dentro do limite? ");
        if (resultado) {
            System.out.println("Sim");
        } else {
            System.out.println("Não");
        }
        geraldo.exibirPosicao();
        System.out.println(geraldo.atirar(50, 20, 50));
        System.out.println(geraldo.defender(50));
        System.out.println(geraldo.recarregar(20));
        System.out.println(geraldo.defender(60));

        Correios bob = new Correios("Bob", "Sul", 10, 10, 30, 2, 20);
        System.out.println(bob.carregarPacote("batata",10));
        System.out.println(bob.carregarPacote("cenoura",15));
        System.out.println(bob.carregarPacote("brocolis",5));
        System.out.println(bob.carregarPacote("abacate",2));
        System.out.println(bob.entregarPacote("batata", 6, 7));
        System.out.println(bob.listarEntregas());
        bob.exibirPosicao();
    }
}