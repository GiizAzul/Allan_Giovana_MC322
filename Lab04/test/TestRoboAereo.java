import ambiente.Ambiente;
import robos.aereos.DroneAtaque;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;


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
        testarBarometro();
        
        System.out.println("\nTodos os testes foram concluídos!");
    }
    
    private static void testarConstrutor() {
        System.out.println("\n== Teste do Construtor ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        // Teste de criação de DroneAtaque
        Robo droneAtaque = ambiente.criarRobo(2, 1, "DA1", "Leste", MateriaisRobo.FIBRA_CARBONO, 
                                            10, 10, 2, 5, 8, 5, 200, 10);
        verificar("DroneAtaque deve ser criado com sucesso", droneAtaque != null);
        verificar("Nome do drone deve ser DA1", droneAtaque.getNome().equals("DA1"));
        verificar("Posição X deve ser 10", droneAtaque.getX() == 10);
        verificar("Posição Y deve ser 10", droneAtaque.getY() == 10);
        
        // Teste de criação de DroneVigilancia
        Robo droneVigilancia = ambiente.criarRobo(2, 2, "DV1", "Oeste", MateriaisRobo.PLASTICO, 
                                                 20, 20, 3, 5, 8, 5, 50.0f, 60.0f, 90.0f);
        verificar("DroneVigilancia deve ser criado com sucesso", droneVigilancia != null);
        verificar("Nome do drone deve ser DV1", droneVigilancia.getNome().equals("DV1"));
        verificar("Direção deve ser Oeste", droneVigilancia.getDirecao().equals("Oeste"));
    }
    
    private static void testarSubirDescer() {
        System.out.println("\n== Teste de Subir e Descer ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        DroneAtaque drone = (DroneAtaque)ambiente.criarRobo(2, 1, "DA2", "Norte", MateriaisRobo.ALUMINIO, 
                                      10, 10, 2, 5, 8, 5, 200, 10);
        ambiente.adicionarEntidade(drone);
        
        int altitudeInicial = drone.getZ();
        drone.mover(0, 0, 2, ambiente);
        verificar("Drone deve subir 2 unidades", drone.getZ() == altitudeInicial + 2);
        altitudeInicial = drone.getZ();
        drone.mover(0, 0, -1, ambiente);
        verificar("Drone deve descer 1 unidade", drone.getZ() == altitudeInicial - 1);
    }
    
    private static void testarMovimento3D() {
        System.out.println("\n== Teste de Movimento 3D ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        DroneAtaque drone = (DroneAtaque) ambiente.criarRobo(2, 1, "DA3", "Leste", MateriaisRobo.FIBRA_CARBONO, 
                                      5, 5, 2, 5, 8, 5, 200, 10);
        ambiente.adicionarEntidade(drone);
        
        drone.mover(3, 4, 2, ambiente);
        verificar("Posição X após movimento deve ser 8", drone.getX() == 8);
        verificar("Posição Y após movimento deve ser 9", drone.getY() == 9);
        verificar("Posição Z após movimento deve ser 4", drone.getZ() == 4);
    }
    
    private static void testarAltitudeMaxima() {
        System.out.println("\n== Teste de Altitude Máxima ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        DroneAtaque drone = (DroneAtaque) ambiente.criarRobo(2, 1, "DA4", "Sul", MateriaisRobo.PLASTICO, 
                                      10, 10, 2, 5, 8, 5, 200, 10);
        ambiente.adicionarEntidade(drone);
        
        drone.mover(0, 0, 10, ambiente);
        verificar("Altitude não deve ultrapassar o máximo", drone.getZ() <= 8);
        
        drone.mover(0, 0, -20, ambiente);
        verificar("Altitude não deve ser negativa", drone.getZ() >= 0);
    }
    
    private static void testarDistanciaEntreRoboAereoTerrestre() {
        System.out.println("\n== Teste de Distância entre Robô Aéreo e Terrestre ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        Robo droneAereo = ambiente.criarRobo(2, 1, "DA5", "Norte", MateriaisRobo.ALUMINIO, 
                                            0, 0, 2, 5, 8, 5, 200, 10);
        Robo roboTerrestre = ambiente.criarRobo(1, 1, "T1", "Sul", MateriaisRobo.ACO, 
                                              3, 4, 0, 2, 5, 100, 10);
        
        ambiente.adicionarEntidade(droneAereo);
        ambiente.adicionarEntidade(roboTerrestre);
        
        double distancia = droneAereo.distanciaRobo(roboTerrestre);
        verificar("Distância deve ser calculada em 3D", distancia > 0);
    }
    
    private static void testarDistanciaEntreRobosAereos() {
        System.out.println("\n== Teste de Distância entre Robôs Aéreos ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        Robo drone1 = ambiente.criarRobo(2, 1, "DA6", "Norte", MateriaisRobo.ALUMINIO, 
                                       0, 0, 2, 5, 8, 5, 200, 10);
        Robo drone2 = ambiente.criarRobo(2, 1, "DA7", "Sul", MateriaisRobo.ALUMINIO, 
                                       3, 4, 3, 5, 8, 5, 200, 10);
        
        ambiente.adicionarEntidade(drone1);
        ambiente.adicionarEntidade(drone2);
        
        double distancia = drone1.distanciaRobo(drone2);
        verificar("Distância deve ser calculada em 3D", distancia > 0);
    }
    
    private static void testarExibirPosicao() {
        System.out.println("\n== Teste de Exibição de Posição ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        Robo drone = ambiente.criarRobo(2, 1, "DA8", "Oeste", MateriaisRobo.PLASTICO, 
                                      15, 25, 3, 5, 8, 5, 200, 10);
        ambiente.adicionarEntidade(drone);
        
        String posicao = drone.exibirPosicao();
        verificar("Exibição deve incluir posição X", posicao.contains("15"));
        verificar("Exibição deve incluir posição Y", posicao.contains("25"));
        verificar("Exibição deve incluir posição Z", posicao.contains("3"));
    }
    
    private static void testarBarometro() {
        System.out.println("\n== Teste do Barômetro ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        DroneAtaque drone = (DroneAtaque) ambiente.criarRobo(2, 1, "DA9", "Norte", MateriaisRobo.ALUMINIO, 
                                      10, 10, 2, 5, 8, 5, 200, 10);
        ambiente.adicionarEntidade(drone);
        
        int altitudeInicial = drone.getZ();
        drone.mover(0, 0, 2, ambiente);
        verificar("Altitude deve aumentar após subida", drone.getZ() > altitudeInicial);
        
        drone.mover(0, 0, -1, ambiente);
        verificar("Altitude deve diminuir após descida", drone.getZ() < altitudeInicial + 2);
    }
    
    private static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            System.out.println("\033[1;32m✓ PASSOU:\033[0m "  + descricao);
        } else {
            System.out.println("\033[1;31m✗ FALHOU:\033[0m " + descricao);
        }
    }
}