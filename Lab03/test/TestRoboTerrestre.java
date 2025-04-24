public class TestRoboTerrestre {
    
    public static void main(String[] args) {
        executarTestes();
    }
    
    public static void executarTestes() {
        System.out.println("Iniciando testes da classe RoboTerrestre...");
        
        testarConstrutor();
        testarMovimentoComVelocidade();
        testarAlteracaoVelocidadeMaxima();
        testarDistanciaEntreRobosTerrestre();
        testarDistanciaComRoboAereo();
        
        System.out.println("\nTodos os testes para RoboTerrestre foram concluídos!");
    }
    
    private static void testarConstrutor() {
        System.out.println("\n== Teste do Construtor ==");
        
        // Teste 1: Criação básica de um robô terrestre
        RoboTerrestre robo = new RoboTerrestre("Tank01", "Norte", 5, 5, 10);
        verificar("Nome do robô deve ser Tank01", 
                 robo.getNome().equals("Tank01"));
        verificar("Posição X inicial deve ser 5", 
                 robo.getPosicaoX() == 5);
        verificar("Posição Y inicial deve ser 5", 
                 robo.getPosicaoY() == 5);
        verificar("Direção inicial deve ser Norte", 
                 robo.getDirecao().equals("Norte"));
        verificar("Velocidade máxima deve ser 10", 
                 robo.getVelocidadeMaxima() == 10);
        verificar("Robô deve estar operando", 
                 robo.getOperando());
        verificar("Integridade inicial deve ser 100", 
                 robo.getIntegridade() == 100);
    }
    
    private static void testarMovimentoComVelocidade() {
        System.out.println("\n== Teste de Movimento com Velocidade ==");
        
        // Teste 1: Movimento com velocidade dentro do limite
        RoboTerrestre robo = new RoboTerrestre("Trator", "Leste", 0, 0, 10);
        robo.mover(3, 4, 8);
        verificar("Posição X após movimento deve ser 3", 
                 robo.getPosicaoX() == 3);
        verificar("Posição Y após movimento deve ser 4", 
                 robo.getPosicaoY() == 4);
        
        // Teste 2: Movimento com velocidade acima do limite
        robo.mover(2, 3, 15);
        verificar("Posição não deve mudar quando velocidade é maior que máxima", 
                 robo.getPosicaoX() == 3 && robo.getPosicaoY() == 4);
        
        // Teste 3: Movimento com velocidade igual ao limite
        robo.mover(2, 3, 10);
        verificar("Posição X após movimento com velocidade máxima deve ser 5", 
                 robo.getPosicaoX() == 5);
        verificar("Posição Y após movimento com velocidade máxima deve ser 7", 
                 robo.getPosicaoY() == 7);
    }
    
    private static void testarAlteracaoVelocidadeMaxima() {
        System.out.println("\n== Teste de Alteração da Velocidade Máxima ==");
        
        // Teste 1: Alteração da velocidade máxima
        RoboTerrestre robo = new RoboTerrestre("Escavadora", "Sul", 10, 10, 5);
        verificar("Velocidade máxima inicial deve ser 5", 
                 robo.getVelocidadeMaxima() == 5);
        
        // Teste 2: Definindo nova velocidade máxima
        robo.setVelocidadeMaxima(8);
        verificar("Velocidade máxima deve ser atualizada para 8", 
                 robo.getVelocidadeMaxima() == 8);
        
        // Teste 3: Movimento com nova velocidade máxima
        robo.mover(2, 2, 6);
        verificar("Deve permitir movimento com velocidade dentro do novo limite", 
                 robo.getPosicaoX() == 12 && robo.getPosicaoY() == 12);
    }
    
    private static void testarDistanciaEntreRobosTerrestre() {
        System.out.println("\n== Teste de Distância Entre Robôs Terrestres ==");
        
        // Teste 1: Distância entre dois robôs terrestres
        RoboTerrestre robo1 = new RoboTerrestre("Trator1", "Norte", 0, 0, 5);
        RoboTerrestre robo2 = new RoboTerrestre("Trator2", "Sul", 3, 4, 8);
        
        double distancia = robo1.distanciaRobo(robo2);
        verificar("Distância entre dois robôs terrestres deve ser 5.0", 
                 Math.abs(distancia - 5.0) < 0.001);
        
        // Teste 2: Distância entre robôs na mesma posição
        RoboTerrestre robo3 = new RoboTerrestre("Trator3", "Oeste", 0, 0, 10);
        verificar("Distância entre robôs terrestres na mesma posição deve ser 0", 
                 robo1.distanciaRobo(robo3) == 0.0);
    }
    
    private static void testarDistanciaComRoboAereo() {
        System.out.println("\n== Teste de Distância com Robô Aéreo ==");
        
        // Simulação de um Robo Aéreo para teste
        RoboAereo drone = new RoboAereo("Drone1", "Norte", 3, 4, 5, 10);
        RoboTerrestre trator = new RoboTerrestre("Trator4", "Leste", 0, 0, 7);
        
        double distancia = trator.distanciaRobo(drone);
        verificar("Distância 3D entre terrestre e aéreo deve ser 7.07", 
                 Math.abs(distancia - 7.07) < 0.1);
        
        // Teste com drone diretamente acima
        RoboAereo droneCima = new RoboAereo("DroneCima", "Norte", 0, 0, 10, 20);
        distancia = trator.distanciaRobo(droneCima);
        verificar("Distância com drone diretamente acima deve ser 10.0", 
                 Math.abs(distancia - 10.0) < 0.001);
    }
    
    // Método auxiliar para verificação de testes
    private static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            System.out.println("✓ PASSOU: " + descricao);
        } else {
            System.out.println("✗ FALHOU: " + descricao);
        }
    }
}