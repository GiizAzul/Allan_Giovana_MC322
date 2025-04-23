public class TestRobo {
    
    public static void main(String[] args) {
        executarTestes();
    }
    
    public static void executarTestes() {
        System.out.println("Iniciando testes da classe Robo...");
        
        testarConstrutor();
        testarMovimento();
        testarIntegridade();
        testarDirecao();
        testarDefesa();
        testarIdentificacaoRobos();
        testarDistanciaEntreRobos();
        
        System.out.println("\nTodos os testes foram concluídos!");
    }
    
    private static void testarConstrutor() {
        System.out.println("\n== Teste do Construtor ==");
        
        // Teste 1: Criação básica de um robô
        Robo robo = new Robo("Geraldinho", "Norte", 5, 5);
        verificar("Nome do robô deve ser Geraldinho", 
                 robo.getNome().equals("Geraldinho"));
        verificar("Posição X inicial deve ser 5", 
                 robo.getPosicaoX() == 5);
        verificar("Posição Y inicial deve ser 5", 
                 robo.getPosicaoY() == 5);
        verificar("Direção inicial deve ser Norte", 
                 robo.getDirecao().equals("Norte"));
        verificar("Robô deve estar operando", 
                 robo.getOperando());
        verificar("Integridade inicial deve ser 100", 
                 robo.getIntegridade() == 100);
    }
    
    private static void testarMovimento() {
        System.out.println("\n== Teste de Movimento ==");
        
        // Teste 1: Movimento simples
        Robo robo = new Robo("Claudinho", "Leste", 0, 0);
        robo.mover(3, 4);
        verificar("Posição X após movimento deve ser 3", 
                 robo.getPosicaoX() == 3);
        verificar("Posição Y após movimento deve ser 4", 
                 robo.getPosicaoY() == 4);
        
        // Teste 2: Movimento negativo
        robo.mover(-1, -2);
        verificar("Posição X após movimento negativo deve ser 2", 
                 robo.getPosicaoX() == 2);
        verificar("Posição Y após movimento negativo deve ser 2", 
                 robo.getPosicaoY() == 2);
    }
    
    private static void testarIntegridade() {
        System.out.println("\n== Teste de Integridade ==");
        
        // Teste 1: Definindo integridade
        Robo robo = new Robo("Fernandinho", "Sul", 10, 10);
        robo.setIntegridade(50);
        verificar("Integridade deve ser definida para 50", 
                 robo.getIntegridade() == 50);
        verificar("Robô deve continuar operando", 
                 robo.getOperando());
        
        // Teste 2: Integridade zero (inoperante)
        robo.setIntegridade(0);
        verificar("Integridade zero deve deixar o robô inoperante", 
                 !robo.getOperando());
        verificar("Integridade deve ser 0", 
                 robo.getIntegridade() == 0);
        
        // Teste 3: Integridade negativa
        robo.setIntegridade(-10);
        verificar("Integridade negativa deve ser tratada como 0", 
                 robo.getIntegridade() == 0);
    }
    
    private static void testarDirecao() {
        System.out.println("\n== Teste de Direção ==");
        
        // Teste 1: Alterar direção
        Robo robo = new Robo("Geraldinho", "Norte", 5, 5);
        robo.setDirecao("Sul");
        verificar("Direção deve ser alterada para Sul", 
                 robo.getDirecao().equals("Sul"));
        
        // Teste 2: Verificar direções possíveis
        verificar("Deve haver 4 direções possíveis", 
                 Robo.getDirecoesPossiveis().size() == 4);
        verificar("Direções devem incluir Norte", 
                 Robo.getDirecoesPossiveis().contains("Norte"));
        verificar("Direções devem incluir Sul", 
                 Robo.getDirecoesPossiveis().contains("Sul"));
        verificar("Direções devem incluir Leste", 
                 Robo.getDirecoesPossiveis().contains("Leste"));
        verificar("Direções devem incluir Oeste", 
                 Robo.getDirecoesPossiveis().contains("Oeste"));
    }
    
    private static void testarDefesa() {
        System.out.println("\n== Teste de Defesa ==");
        
        // Teste 1: Dano parcial
        Robo robo = new Robo("Exterminador", "Leste", 3, 3);
        robo.defender(30);
        verificar("Integridade deve diminuir com o dano", 
                 robo.getIntegridade() == 70);
        verificar("Robô deve continuar operando após dano parcial", 
                 robo.getOperando());
        
        // Teste 2: Dano fatal
        robo.defender(80);
        verificar("Robô deve ficar inoperante após dano fatal", 
                 !robo.getOperando());
        verificar("Integridade deve ser 0 após dano fatal", 
                 robo.getIntegridade() == 0);
    }
    
    private static void testarIdentificacaoRobos() {
        System.out.println("\n== Teste de Identificação de Obstáculos ==");
        
        // Configuração do ambiente e robôs
        Ambiente ambiente = new Ambiente(20, 20, 10);
        Robo roboBase = new Robo("Base", "Norte", 5, 5);
        Robo roboNorte1 = new Robo("Norte1", "Sul", 5, 8);
        Robo roboNorte2 = new Robo("Norte2", "Sul", 5, 10);
        Robo roboLeste = new Robo("Leste", "Oeste", 7, 5);
        
        ambiente.adicionarRobo(roboBase);
        ambiente.adicionarRobo(roboNorte1);
        ambiente.adicionarRobo(roboNorte2);
        ambiente.adicionarRobo(roboLeste);
        
        // Teste 1: Identificação ao Norte
        verificar("Deve identificar 2 obstáculos ao Norte", 
                 roboBase.identificarRobo(ambiente, "Norte").size() == 2);
        verificar("Primeiro obstáculo ao Norte deve ser Norte1", 
                 roboBase.identificarRobo(ambiente, "Norte").get(0).getNome().equals("Norte1"));
        
        // Teste 2: Identificação ao Leste
        verificar("Deve identificar 1 obstáculo ao Leste", 
                 roboBase.identificarRobo(ambiente, "Leste").size() == 1);
        verificar("Obstáculo ao Leste deve ser Leste", 
                 roboBase.identificarRobo(ambiente, "Leste").get(0).getNome().equals("Leste"));
        
        // Teste 3: Identificação sem obstáculos
        verificar("Não deve haver obstáculos ao Sul", 
                 roboBase.identificarRobo(ambiente, "Sul").isEmpty());
    }
    
    private static void testarDistanciaEntreRobos() {
        System.out.println("\n== Teste de Cálculo de Distância ==");
        
        // Teste 1: Distância em linha reta
        Robo robo1 = new Robo("Geraldo", "Norte", 0, 0);
        Robo robo2 = new Robo("Claudio", "Sul", 3, 4);
        
        double distancia = robo1.distanciaRobo(robo2);
        verificar("Distância deve ser 5.0", 
                 Math.abs(distancia - 5.0) < 0.001);
        
        // Teste 2: Distância zero (mesma posição)
        Robo robo3 = new Robo("Robo3", "Leste", 0, 0);
        verificar("Distância entre robôs na mesma posição deve ser 0", 
                 robo1.distanciaRobo(robo3) == 0.0);
    }
    
    // Método auxiliar para verificação de testes
    private static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            System.out.println("✓ PASSOU: " + descricao);
        } else {
            System.out.println("✗ FALHOU: " + descricao);
            // Em um framework de testes completo, poderia lançar exceção aqui
        }
    }
}
