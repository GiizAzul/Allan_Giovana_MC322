public class TestRoboAereo {
    
    public static void main(String[] args) {
        executarTestes();
    }
    
    public static void executarTestes() {
        System.out.println("Iniciando testes da classe RoboAereo...");
        
        testarConstrutor();
        testarSubirDescer();
        testarMovimento3D();
        testarAltitudeMaxima();
        testarDistanciaEntreRoboAereoTerrestre();
        testarDistanciaEntreRobosAereos();
        testarIdentificacaoObstaculos();
        testarExibirPosicao();
        
        System.out.println("\nTodos os testes foram concluídos!");
    }
    
    private static void testarConstrutor() {
        System.out.println("\n== Teste do Construtor ==");
        
        // Teste 1: Criação básica de um robô aéreo
        RoboAereo robo = new RoboAereo("Drone1", "Norte", 5, 5, 10, 50);
        verificar("Nome do robô deve ser Drone1", 
                 robo.getNome().equals("Drone1"));
        verificar("Posição X inicial deve ser 5", 
                 robo.getPosicaoX() == 5);
        verificar("Posição Y inicial deve ser 5", 
                 robo.getPosicaoY() == 5);
        verificar("Altitude inicial deve ser 10", 
                 robo.getAltitude() == 10);
        verificar("Altitude máxima deve ser 50", 
                 robo.getAltitudeMaxima() == 50);
        verificar("Direção inicial deve ser Norte", 
                 robo.getDirecao().equals("Norte"));
        verificar("Robô deve estar operando", 
                 robo.getOperando());
        verificar("Integridade inicial deve ser 100", 
                 robo.getIntegridade() == 100);
    }
    
    private static void testarSubirDescer() {
        System.out.println("\n== Teste de Subir e Descer ==");
        
        // Teste 1: Subir dentro do limite
        RoboAereo robo = new RoboAereo("Drone2", "Leste", 0, 0, 10, 50);
        robo.subir(20);
        verificar("Altitude após subir 20 deve ser 30", 
                 robo.getAltitude() == 30);
        
        // Teste 2: Subir além do limite
        robo.subir(30);
        verificar("Altitude não deve exceder o máximo", 
                 robo.getAltitude() == 50);
        
        // Teste 3: Descer
        robo.descer(20);
        verificar("Altitude após descer 20 deve ser 30", 
                 robo.getAltitude() == 30);
        
        // Teste 4: Descer abaixo de zero
        robo.descer(40);
        verificar("Altitude não deve ser negativa", 
                 robo.getAltitude() == 0);
    }
    
    private static void testarMovimento3D() {
        System.out.println("\n== Teste de Movimento 3D ==");
        
        // Teste 1: Movimento 3D completo
        RoboAereo robo = new RoboAereo("Drone3", "Norte", 5, 5, 10, 100);
        robo.mover(8, 12, 20);
        verificar("Posição X após movimento deve ser 8", 
                 robo.getPosicaoX() == 8);
        verificar("Posição Y após movimento deve ser 12", 
                 robo.getPosicaoY() == 12);
        verificar("Altitude após movimento deve ser 20", 
                 robo.getAltitude() == 20);
        
        // Teste 2: Movimento 3D para baixo
        robo.mover(15, 15, 5);
        verificar("Posição X após segundo movimento deve ser 15", 
                 robo.getPosicaoX() == 15);
        verificar("Posição Y após segundo movimento deve ser 15", 
                 robo.getPosicaoY() == 15);
        verificar("Altitude após descer deve ser 5", 
                 robo.getAltitude() == 5);
    }
    
    private static void testarAltitudeMaxima() {
        System.out.println("\n== Teste de Altitude Máxima ==");
        
        // Teste 1: Definir nova altitude máxima
        RoboAereo robo = new RoboAereo("Drone4", "Sul", 10, 10, 20, 80);
        verificar("Altitude máxima inicial deve ser 80", 
                 robo.getAltitudeMaxima() == 80);
        
        // Teste 2: Alterar altitude máxima
        robo.setAltitudeMaxima(100);
        verificar("Altitude máxima deve ser atualizada para 100", 
                 robo.getAltitudeMaxima() == 100);
        
        // Teste 3: Subir acima do novo limite
        robo.subir(90);
        verificar("Altitude deve respeitar o novo limite", 
                 robo.getAltitude() == 100);
    }
    
    private static void testarDistanciaEntreRoboAereoTerrestre() {
        System.out.println("\n== Teste de Distância Entre Robô Aéreo e Terrestre ==");
        
        // Teste 1: Distância com robô terrestre
        RoboAereo roboAereo = new RoboAereo("Drone5", "Norte", 0, 0, 30, 100);
        RoboTerrestre roboTerrestre = new RoboTerrestre("Tanque1", "Sul", 3, 4, 10);
        
        double distancia = roboAereo.distanciaRobo(roboTerrestre);
        verificar("Distância 3D entre aéreo e terrestre deve ser 30.41", 
                 Math.abs(distancia - 30.41) < 0.1);
        
        // Teste 2: Distância com robô terrestre diretamente abaixo
        RoboTerrestre roboAbaixo = new RoboTerrestre("Tanque2", "Leste", 0, 0, 10);
        distancia = roboAereo.distanciaRobo(roboAbaixo);
        verificar("Distância com robô terrestre diretamente abaixo deve ser 30.0", 
                 Math.abs(distancia - 30.0) < 0.001);
    }
    
    private static void testarDistanciaEntreRobosAereos() {
        System.out.println("\n== Teste de Distância Entre Robôs Aéreos ==");
        
        // Teste 1: Distância entre dois robôs aéreos
        RoboAereo robo1 = new RoboAereo("Drone6", "Norte", 0, 0, 10, 100);
        RoboAereo robo2 = new RoboAereo("Drone7", "Sul", 3, 4, 10, 100);
        
        double distancia = robo1.distanciaRobo(robo2);
        verificar("Distância entre robôs aéreos na mesma altitude deve ser 5.0", 
                 Math.abs(distancia - 5.0) < 0.001);
        
        // Teste 2: Distância 3D com diferença de altura
        RoboAereo robo3 = new RoboAereo("Drone8", "Leste", 3, 4, 30, 100);
        distancia = robo1.distanciaRobo(robo3);
        verificar("Distância 3D entre robôs com diferença de altura deve ser 20.62", 
                 Math.abs(distancia - 20.62) < 0.1);
    }
    
    private static void testarIdentificacaoObstaculos() {
        System.out.println("\n== Teste de Identificação de Obstáculos ==");
        
        // Configuração do ambiente e robôs
        Ambiente ambiente = new Ambiente(20, 20, 50);
        RoboAereo roboBase = new RoboAereo("DroneBase", "Norte", 5, 5, 20, 100);
        RoboAereo roboNorte1 = new RoboAereo("DroneNorte1", "Sul", 5, 8, 20, 100);
        RoboAereo roboNorte2 = new RoboAereo("DroneNorte2", "Sul", 5, 10, 20, 100);
        RoboAereo roboLeste = new RoboAereo("DroneLeste", "Oeste", 7, 5, 20, 100);
        RoboAereo roboAlto = new RoboAereo("DroneAlto", "Norte", 5, 8, 30, 100);
        
        ambiente.adicionarRobo(roboBase);
        ambiente.adicionarRobo(roboNorte1);
        ambiente.adicionarRobo(roboNorte2);
        ambiente.adicionarRobo(roboLeste);
        ambiente.adicionarRobo(roboAlto);
        
        // Teste 1: Identificação ao Norte (mesma altitude)
        verificar("Deve identificar 2 obstáculos ao Norte na mesma altitude", 
                 roboBase.identificarObstaculo(ambiente, "Norte").size() == 2);
        verificar("Primeiro obstáculo ao Norte deve ser DroneNorte1", 
                 roboBase.identificarObstaculo(ambiente, "Norte").get(0).getNome().equals("DroneNorte1"));
        
        // Teste 2: Identificação ao Leste (mesma altitude)
        verificar("Deve identificar 1 obstáculo ao Leste na mesma altitude", 
                 roboBase.identificarObstaculo(ambiente, "Leste").size() == 1);
        verificar("Obstáculo ao Leste deve ser DroneLeste", 
                 roboBase.identificarObstaculo(ambiente, "Leste").get(0).getNome().equals("DroneLeste"));
        
        // Teste 3: Não deve identificar drones em altitudes diferentes
        verificar("Não deve identificar drones em altitudes diferentes", 
                 !roboBase.identificarObstaculo(ambiente, "Norte").stream()
                        .anyMatch(r -> r.getNome().equals("DroneAlto")));
    }
    
    private static void testarExibirPosicao() {
        System.out.println("\n== Teste de Exibição de Posição ==");
        
        // Teste de exibição de posição com altitude
        RoboAereo robo = new RoboAereo("DroneFinal", "Oeste", 10, 15, 25, 100);
        String posicao = robo.exibirPosicao();
        
        verificar("Exibição de posição deve incluir altitude", 
                 posicao.contains("10") && posicao.contains("15") && posicao.contains("25"));
        verificar("Exibição de posição deve incluir nome do robô", 
                 posicao.contains("DroneFinal"));
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