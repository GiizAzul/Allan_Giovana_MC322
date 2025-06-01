import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import excecoes.ambiente.ForaDosLimitesException;
import excecoes.robos.especificos.AlvoInvalidoException;
import excecoes.robos.especificos.MunicaoInsuficienteException;
import excecoes.robos.gerais.ColisaoException;
import excecoes.robos.gerais.RoboDestruidoPorBuracoException;
import excecoes.sensor.SensorInativoException;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;

public class TestRobo extends TestBase {
    
    public static void main(String[] args) throws SensorInativoException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        executarTestes();
    }
    
    public static void executarTestes() throws SensorInativoException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("Iniciando testes da classe Robo...");
        
        testarConstrutor();
        testarMovimento();
        testarIntegridade();
        testarDirecao();
        testarDistanciaEntreRobos();
        testarAcessoGPS();  
        testarFuncionamentoGPS();
        testarColisoes();   
        testarExibirPosicao();
        
        System.out.println("\nTodos os testes foram concluídos!");
    }
    
    private static void testarConstrutor() {
        System.out.println("\n== Teste do Construtor ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        
        verificar("Nome do robô deve ser T1", robo.getNome().equals("T1"));
        verificar("Posição X inicial deve ser 2", robo.getXInterno() == 2);
        verificar("Posição Y inicial deve ser 3", robo.getYInterno() == 3);
        verificar("Direção inicial deve ser Norte", robo.getDirecao().equals("Norte"));
        verificar("Material do robô deve ser ACO", robo.getMateriaisRobo() == MateriaisRobo.ACO);
        verificar("Integridade inicial deve ser 100", robo.getIntegridade() == 100);
    }
    
    private static void testarMovimento() throws SensorInativoException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("\n== Teste de Movimento ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        adicionarEntidadeTest(robo, ambiente);
        
        robo.executarTarefa("mover base",1,1,ambiente);
        verificar("Posição X após movimento deve ser 3", robo.getX() == 3);
        verificar("Posição Y após movimento deve ser 4", robo.getY() == 4);
        
        // Teste com obstáculo
        Obstaculo obstaculo = new Obstaculo(TipoObstaculo.PAREDE, 4, 4, 4, 4);
        adicionarEntidadeTest(obstaculo, ambiente);
        robo.executarTarefa("mover base",1,1,ambiente);
        verificar("Movimento deve ser bloqueado por obstáculo", robo.getX() == 3 && robo.getY() == 4);
    }
    
    private static void testarIntegridade() {
        System.out.println("\n== Teste de Integridade ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        
        verificar("Integridade inicial deve ser 100", robo.getIntegridade() == 100);
        robo.defender(50, ambiente);
        verificar("Integridade após dano deve ser 50", robo.getIntegridade() == 50);
    }
    
    private static void testarDirecao() {
        System.out.println("\n== Teste de Direção ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        
        verificar("Direção inicial deve ser Norte", robo.getDirecao().equals("Norte"));
        robo.setDirecao("Sul");
        verificar("Direção após mudança deve ser Sul", robo.getDirecao().equals("Sul"));
    }
    
    private static void testarDistanciaEntreRobos() throws SensorInativoException {
        System.out.println("\n== Teste de Distância Entre Robôs ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        Robo robo1 = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 0, 0, 0, 2, 5, 100, 10);
        Robo robo2 = ambiente.criarRobo(1, 1, "T2", "Sul", MateriaisRobo.ACO, 3, 4, 0, 2, 5, 100, 10);
        
        adicionarEntidadeTest(robo1, ambiente);
        adicionarEntidadeTest(robo2, ambiente);
        
        double distancia = robo1.distanciaRobo(robo2);
        verificar("Distância calculada deve ser 5.0", Math.abs(distancia - 5.0) < 0.001);
    }
    
    private static void testarAcessoGPS() throws SensorInativoException {
        System.out.println("\n== Teste de Acesso via GPS ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        
        verificar("Posição X deve ser obtida corretamente", robo.getX() == 2);
        verificar("Posição Y deve ser obtida corretamente", robo.getY() == 3);
    }
    
    private static void testarFuncionamentoGPS() throws SensorInativoException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("\n== Teste de Funcionamento do GPS ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        
        verificar("Posição X inicial deve ser 2", robo.getX() == 2);
        verificar("Posição Y inicial deve ser 3", robo.getY() == 3);
        
        adicionarEntidadeTest(robo, ambiente);
        robo.executarTarefa("mover base",1,1,ambiente);
        
        verificar("GPS deve atualizar posição X após movimento", robo.getX() == 3);
        verificar("GPS deve atualizar posição Y após movimento", robo.getY() == 4);
    }
    
    private static void testarColisoes() throws SensorInativoException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("\n== Teste de Colisões ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        
        Robo robo1 = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        Robo robo2 = ambiente.criarRobo(1, 1, "T2", "Sul", MateriaisRobo.ACO, 2, 4, 0, 2, 5, 100, 10);
        
        adicionarEntidadeTest(robo1, ambiente);
        adicionarEntidadeTest(robo2, ambiente);
        robo1.executarTarefa("mover base",0,2,ambiente);
        verificar("Movimento deve ser bloqueado por colisão", robo1.getY() == 3);
        
        // Teste com obstáculo
        Obstaculo obstaculo = new Obstaculo(TipoObstaculo.PAREDE, 3, 3, 3, 3);
        adicionarEntidadeTest(obstaculo, ambiente);
        
        robo1.executarTarefa("mover base",1,0,ambiente);
        verificar("Movimento deve ser bloqueado por obstáculo", robo1.getX() == 2);
    }
    
    private static void testarExibirPosicao() throws SensorInativoException {
        System.out.println("\n== Teste de Exibição de Posição ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        
        String posicao = robo.exibirPosicao();
        verificar("Exibição deve incluir posição X", posicao.contains("2"));
        verificar("Exibição deve incluir posição Y", posicao.contains("3"));
    }
}