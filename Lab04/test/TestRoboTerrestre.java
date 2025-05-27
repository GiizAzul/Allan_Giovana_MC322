import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.aereos.DroneAtaque;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.terrestres.Correios;
import robos.terrestres.RoboTerrestre;
import robos.terrestres.TanqueGuerra;

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
        testarSensorColisao();
        testarColisaoComObstaculos();
        testarColisaoComBuraco();
        
        System.out.println("\nTodos os testes para RoboTerrestre foram concluídos!");
    }
    
    private static void testarConstrutor() {
        System.out.println("\n== Teste do Construtor ==");
        
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        // Teste de criação de TanqueGuerra
        Robo tanque = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        verificar("TanqueGuerra deve ser criado com sucesso", tanque != null);
        verificar("Nome deve ser T1", tanque.getNome().equals("T1"));
        verificar("Posição X deve ser 5", tanque.getX() == 5);
        verificar("Posição Y deve ser 5", tanque.getY() == 5);
        verificar("Direção deve ser Norte", tanque.getDirecao().equals("Norte"));
        
        // Teste de criação de Correios
        Robo correio = ambiente.criarRobo(1, 2, "C1", "Sul", MateriaisRobo.PLASTICO, 10, 10, 0, 3, 5, 50, 25.0f);
        verificar("Correios deve ser criado com sucesso", correio != null);
        verificar("Nome deve ser C1", correio.getNome().equals("C1"));
        verificar("Material deve ser PLASTICO", correio.getMateriaisRobo() == MateriaisRobo.PLASTICO);
    }
    
    private static void testarMovimentoComVelocidade() {
        System.out.println("\n== Teste de Movimento com Velocidade ==");
        
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 0, 0, 0, 5, 10, 100, 10);
        ambiente.adicionarEntidade(robo);
        
        robo.mover(3, 4, ambiente);
        verificar("Posição X após movimento deve ser 3", robo.getX() == 3);
        verificar("Posição Y após movimento deve ser 4", robo.getY() == 4);
        
        // Teste com obstáculo
        Obstaculo obstaculo = new Obstaculo(TipoObstaculo.PAREDE, 5, 6, 4, 5);
        ambiente.adicionarEntidade(obstaculo);
        
        robo.mover(3, 0, ambiente);
        verificar("Movimento deve ser bloqueado pelo obstáculo", robo.getX() == 4);
    }
    
    private static void testarAlteracaoVelocidadeMaxima() {
        System.out.println("\n== Teste de Alteração da Velocidade Máxima ==");
        
        Ambiente ambiente = new Ambiente(20, 20, 10);
        RoboTerrestre robo = (RoboTerrestre)ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        
        robo.executarTarefa("velocidade", 8);
        verificar("Nova velocidade deve ser aceita se menor que máxima", robo.getVelocidadeMaxima() == 8);
        
        robo.executarTarefa("mover", 1,1,7,ambiente);
        verificar("Deve permitir movimento com velocidade dentro do novo limite", robo.getX() == 6 && robo.getY() == 6);
    }
    
    private static void testarDistanciaEntreRobosTerrestre() {
        System.out.println("\n== Teste de Distância Entre Robôs Terrestres ==");
        
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        Robo robo1 = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 0, 0, 0, 5, 10, 100, 10);
        Robo robo2 = ambiente.criarRobo(1, 1, "T2", "Sul", MateriaisRobo.ACO, 3, 4, 0, 5, 10, 100, 10);
        
        ambiente.adicionarEntidade(robo1);
        ambiente.adicionarEntidade(robo2);
        
        double distancia = robo1.distanciaRobo(robo2);
        verificar("Distância deve ser 5.0", Math.abs(distancia - 5.0) < 0.001);
    }
    
    private static void testarDistanciaComRoboAereo() {
        System.out.println("\n== Teste de Distância com Robô Aéreo ==");
        
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        Robo terrestre = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 0, 0, 0, 5, 10, 100, 10);
        Robo aereo = ambiente.criarRobo(2, 1, "D1", "Norte", MateriaisRobo.FIBRA_CARBONO, 3, 4, 5, 5, 8, 10, 200, 10);
        
        ambiente.adicionarEntidade(terrestre);
        ambiente.adicionarEntidade(aereo);
        
        double distancia = terrestre.distanciaRobo(aereo);
        verificar("Distância 3D deve ser calculada corretamente", Math.abs(distancia - 7.071) < 0.001);
    }
    
    private static void testarSensorColisao() {
        System.out.println("\n== Teste de Sensor de Colisão ==");
        
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        ambiente.adicionarEntidade(robo);
        
        Obstaculo obstaculo = new Obstaculo(TipoObstaculo.PAREDE, 5, 6, 6, 7);
        ambiente.adicionarEntidade(obstaculo);
        
        robo.mover(0, 2, ambiente);
        verificar("Movimento deve ser interrompido antes da colisão", robo.getY() == 5);
    }
    
    private static void testarColisaoComObstaculos() {
        System.out.println("\n== Teste de Colisão com Obstáculos ==");
        
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        ambiente.adicionarEntidade(robo);
        
        // Teste com diferentes tipos de obstáculos
        Obstaculo parede = new Obstaculo(TipoObstaculo.PAREDE, 7, 8, 5, 6);
        Obstaculo arvore = new Obstaculo(TipoObstaculo.ARVORE, 5, 6, 7, 8);
        
        ambiente.adicionarEntidade(parede);
        ambiente.adicionarEntidade(arvore);
        
        robo.mover(3, 0, ambiente);
        verificar("Movimento deve parar antes da parede", robo.getX() == 6);
        
        robo.mover(0, 3, ambiente);
        verificar("Movimento deve parar antes da árvore", robo.getY() == 6);
    }
    
    private static void testarColisaoComBuraco() {
        System.out.println("\n== Teste de Colisão com Buraco ==");
        
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        ambiente.adicionarEntidade(robo);
        
        Obstaculo buraco = new Obstaculo(TipoObstaculo.BURACO, 5, 6, 7, 8);
        ambiente.adicionarEntidade(buraco);
        
        verificar("Robô deve estar no ambiente inicialmente", ambiente.getEntidades().contains(robo));
        
        robo.mover(0, 3, ambiente);
        
        verificar("Robô deve ser removido após cair no buraco", !ambiente.getEntidades().contains(robo));
    }
    
    private static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            System.out.println("\033[1;32m✓ PASSOU:\033[0m " + descricao);
        } else {
            System.out.println("\033[1;31m✗ FALHOU:\033[0m " + descricao);
        }
    }
}