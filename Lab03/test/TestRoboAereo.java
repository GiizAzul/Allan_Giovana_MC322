import ambiente.Ambiente;
import robos.aereos.DroneAtaque;
import robos.aereos.DroneVigilancia;
import robos.aereos.RoboAereo;
import robos.geral.MateriaisRobo;
import robos.terrestres.RoboTerrestre;

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
        testarExibirPosicao();
        
        System.out.println("\nTodos os testes foram concluídos!");
    }
    
    private static void testarConstrutor() {
        System.out.println("\n== Teste do Construtor ==");
        
        // Cria ambiente para instanciar os robôs
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        // Teste 1: Criação básica de um robô aéreo genérico
        RoboAereo robo = new RoboAereo("Drone1", "Norte", MateriaisRobo.ALUMINIO, 10, 10, 5, 30, 100, ambiente);
        verificar("Nome do robô deve ser Drone1", robo.getNome().equals("Drone1"));
        verificar("Posição X inicial deve ser 10", robo.getPosicaoX() == 10);
        verificar("Posição Y inicial deve ser 10", robo.getPosicaoY() == 10);
        verificar("Altitude inicial deve ser 30", robo.getAltitude() == 30);
        verificar("Altitude máxima deve ser 100", robo.getAltitudeMaxima() == 100);
        verificar("Material do robô deve ser ALUMINIO", robo.getMateriaisRobo() == MateriaisRobo.ALUMINIO);
        
        // Teste 2: Criação de um drone de vigilância (radar mais potente)
        DroneVigilancia droneVigilancia = new DroneVigilancia("Vigilante", "Leste", MateriaisRobo.FIBRA_CARBONO, 
                                                             20, 20, 8, 50, 150, ambiente, 200, 45, 90.0f);
        verificar("Nome do drone de vigilância deve ser Vigilante", 
                 droneVigilancia.getNome().equals("Vigilante"));
        verificar("Drone de vigilância deve ser de fibra de carbono", 
                 droneVigilancia.getMateriaisRobo() == MateriaisRobo.FIBRA_CARBONO);
        verificar("Altitude do drone de vigilância deve ser 50",
                 droneVigilancia.getAltitude() == 50);
        
        // Teste 3: Criação de um drone de ataque
        DroneAtaque droneAtaque = new DroneAtaque("Atacante", "Sul", MateriaisRobo.ACO, 
                                                 30, 30, 10, 20, 80, ambiente, 150, 35);
        verificar("Nome do drone de ataque deve ser Atacante", 
                 droneAtaque.getNome().equals("Atacante"));
        verificar("Drone de ataque deve ser de aço", 
                 droneAtaque.getMateriaisRobo() == MateriaisRobo.ACO);
        verificar("Altitude do drone de ataque deve ser 20",
                 droneAtaque.getAltitude() == 20);
    }
    
    private static void testarSubirDescer() {
        System.out.println("\n== Teste de Subir e Descer ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        // Teste 1: Subir
        RoboAereo robo = new RoboAereo("Drone2", "Norte", MateriaisRobo.ALUMINIO, 10, 10, 5, 10, 100, ambiente);
        robo.subir(20, ambiente);
        verificar("Altitude após subir 20 deve ser 30", robo.getAltitude() == 30);
        
        // Teste 2: Descer
        robo.descer(15, ambiente);
        verificar("Altitude após descer 15 deve ser 15", robo.getAltitude() == 15);
        
        // Teste 3: Subir além do limite
        robo.subir(120, ambiente);
        verificar("Altitude não deve ultrapassar a máxima (100)", robo.getAltitude() == 100);
        
        // Teste 4: Descer abaixo de zero
        robo.descer(150, ambiente);
        verificar("Altitude não deve ficar negativa", robo.getAltitude() == 0);
    }
    
    private static void testarMovimento3D() {
        System.out.println("\n== Teste de Movimento 3D ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        // Teste 1: Movimento 3D simples
        RoboAereo robo = new RoboAereo("Drone3", "Leste", MateriaisRobo.FIBRA_VIDRO, 5, 5, 8, 10, 80, ambiente);
        robo.mover(10, 15, 30, ambiente);
        verificar("Posição X após movimento deve ser 10", robo.getPosicaoX() == 10);
        verificar("Posição Y após movimento deve ser 15", robo.getPosicaoY() == 15);
        verificar("Altitude após movimento deve ser 30", robo.getAltitude() == 30);
        
        // Teste 2: Movimento com obstáculo - teste sem implementar colisão física, apenas instanciação
        RoboAereo roboObstaculo = new RoboAereo("Obstaculo", "Norte", MateriaisRobo.ACO, 
                                               20, 15, 5, 30, 80, ambiente);
        ambiente.adicionarRobo(roboObstaculo);
        
        // Movimento para testar colisão
        robo.mover(25, 15, 30, ambiente);
        
        // Teste 3: Movimento com limites de altitude
        robo.mover(10, 15, 100, ambiente);
        verificar("Altitude deve ser limitada à máxima", robo.getAltitude() <= 80);
    }
    
    private static void testarAltitudeMaxima() {
        System.out.println("\n== Teste de Altitude Máxima ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        // Teste 1: Definir nova altitude máxima
        RoboAereo robo = new RoboAereo("Drone4", "Sul", MateriaisRobo.PLASTICO, 10, 10, 5, 20, 50, ambiente);
        verificar("Altitude máxima inicial deve ser 50", robo.getAltitudeMaxima() == 50);
        
        robo.setAltitudeMaxima(80);
        verificar("Nova altitude máxima deve ser 80", robo.getAltitudeMaxima() == 80);
        
        // Teste 2: Subir até nova altitude máxima
        robo.subir(70, ambiente);
        verificar("Altitude após subir deve respeitar novo limite", robo.getAltitude() == 80);
    }
    
    private static void testarDistanciaEntreRoboAereoTerrestre() {
        System.out.println("\n== Teste de Distância entre Robô Aéreo e Terrestre ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        // Criar robôs para teste
        RoboAereo roboAereo = new RoboAereo("DroneA", "Norte", MateriaisRobo.ALUMINIO, 0, 0, 5, 10, 50, ambiente);
        RoboTerrestre roboTerrestre = new RoboTerrestre("Terrestre", "Sul", MateriaisRobo.ACO, 3, 4, 6, 10);
        
        // Calculando distância
        double distancia = roboAereo.distanciaRobo(roboTerrestre);
        verificar("Distância entre robô aéreo e terrestre deve considerar a altitude", 
                 Math.abs(distancia - 11.18) < 0.1); // √(3² + 4² + 10²) ≈ 11.18
    }
    
    private static void testarDistanciaEntreRobosAereos() {
        System.out.println("\n== Teste de Distância entre Robôs Aéreos ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        // Criar robôs para teste
        RoboAereo roboAereo1 = new RoboAereo("DroneA", "Norte", MateriaisRobo.ALUMINIO, 0, 0, 5, 10, 50, ambiente);
        RoboAereo roboAereo2 = new RoboAereo("DroneB", "Sul", MateriaisRobo.FIBRA_CARBONO, 3, 4, 8, 20, 70, ambiente);
        
        // Calculando distância
        double distancia = roboAereo1.distanciaRobo(roboAereo2);
        verificar("Distância entre robôs aéreos deve considerar ambas altitudes", 
                 Math.abs(distancia - 11.18) < 0.1); // √(3² + 4² + 10²) ≈ 11.18
    }
    
    private static void testarExibirPosicao() {
        System.out.println("\n== Teste de Exibição de Posição ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        // Teste exibição de posição
        RoboAereo robo = new RoboAereo("Drone5", "Oeste", MateriaisRobo.PLASTICO, 15, 25, 7, 30, 80, ambiente);
        String posicao = robo.exibirPosicao();
        verificar("Exibição de posição deve incluir coordenadas X, Y e Z", 
                 posicao.contains("15") && posicao.contains("25") && posicao.contains("30"));
    }
    
    // Método auxiliar para verificação de testes
    private static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            System.out.println("\033[1;32m✓ PASSOU:\033[0m " + descricao);
        } else {
            System.out.println("\033[1;31m✗ FALHOU:\033[0m " + descricao);
        }
    }
}