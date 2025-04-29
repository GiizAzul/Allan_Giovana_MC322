import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.aereos.RoboAereo;
import robos.terrestres.RoboTerrestre;
import robos.geral.MateriaisRobo;

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
        testarSensorColisao();  // Novo teste para o sensor de colisão
        testarColisaoComObstaculos(); // Novo teste para colisões com obstáculos
        testarColisaoComBuraco();  // Novo teste para o caso especial de buraco
        
        System.out.println("\nTodos os testes para RoboTerrestre foram concluídos!");
    }
    
    private static void testarConstrutor() {
        System.out.println("\n== Teste do Construtor ==");
        
        // Configuração do ambiente
        Ambiente ambiente = new Ambiente(20, 20, 100);

        // Teste 1: Criação básica de um robô terrestre
        RoboTerrestre robo = new RoboTerrestre("Tank01", "Norte", ambiente, MateriaisRobo.ACO, 5, 5, 7, 10);

        verificar("Nome do robô deve ser Tank01", 
                 robo.getNome().equals("Tank01"));
        verificar("Posição X inicial deve ser 5", 
                 robo.getPosicaoX() == 5);
        verificar("Posição Y inicial deve ser 5", 
                 robo.getPosicaoY() == 5);
        verificar("Direção inicial deve ser Norte", 
                 robo.getDirecao().equals("Norte"));
        verificar("Material do robô deve ser ACO", 
                 robo.getMateriaisRobo() == MateriaisRobo.ACO);
        verificar("Velocidade inicial deve ser 7", 
                 robo.getVelocidade() == 7);
        verificar("Velocidade máxima deve ser 10", 
                 robo.getVelocidadeMaxima() == 10);
        verificar("Robô deve estar operando", 
                 robo.getOperando());
        verificar("Integridade inicial deve ser 100", 
                 robo.getIntegridade() == 100);
    }
    
    private static void testarMovimentoComVelocidade() {
        System.out.println("\n== Teste de Movimento com Velocidade ==");
        
        // Configuração do ambiente
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        // Teste 1: Movimento com velocidade dentro do limite
        RoboTerrestre robo = new RoboTerrestre("Trator", "Leste", ambiente, MateriaisRobo.ALUMINIO, 0, 0, 5, 10);
        ambiente.adicionarRobo(robo);
        robo.mover(3, 4, 8, ambiente);
        verificar("Posição X após movimento deve ser 3", 
                 robo.getPosicaoX() == 3);
        verificar("Posição Y após movimento deve ser 4", 
                 robo.getPosicaoY() == 4);
        
        // Teste 2: Movimento com velocidade acima do limite
        robo.mover(2, 3, 15, ambiente);
        verificar("Posição não deve mudar quando velocidade é maior que máxima", 
                 robo.getPosicaoX() == 3 && robo.getPosicaoY() == 4);
        
        // Teste 3: Movimento com obstáculo no caminho
        RoboTerrestre roboObstaculo = new RoboTerrestre("Obstáculo", "Oeste", ambiente, MateriaisRobo.PLASTICO, 6, 4, 3, 5);
        ambiente.adicionarRobo(roboObstaculo);

        robo.mover(5, 0, 10, ambiente);

        verificar("Robo deve ocupar uma posição antes do obstáculo", 
                 robo.getPosicaoX() == 5);
        verificar("Posição Y não deve mudar pois ficou parado", 
                 robo.getPosicaoY() == 4);
    }
    
    private static void testarAlteracaoVelocidadeMaxima() {
        System.out.println("\n== Teste de Alteração da Velocidade Máxima ==");
        
        // Configuração do ambiente
        Ambiente ambiente = new Ambiente(20, 20, 100);

        // Teste 1: Alteração da velocidade máxima
        RoboTerrestre robo = new RoboTerrestre("Escavadora", "Sul", ambiente, MateriaisRobo.FIBRA_VIDRO, 10, 10, 3, 5);
        verificar("Velocidade inicial deve ser 3", 
                 robo.getVelocidade() == 3);
        verificar("Velocidade máxima inicial deve ser 5", 
                 robo.getVelocidadeMaxima() == 5);
        
        // Teste 2: Definindo nova velocidade máxima
        robo.setVelocidadeMaxima(8);
        verificar("Velocidade máxima deve ser atualizada para 8", 
                 robo.getVelocidadeMaxima() == 8);
        
        // Teste 3: Movimento com nova velocidade máxima
        ambiente.adicionarRobo(robo);
        robo.mover(2, 2, 6, ambiente);
        verificar("Deve permitir movimento com velocidade dentro do novo limite", 
                 robo.getPosicaoX() == 12 && robo.getPosicaoY() == 12);
    }
    
    private static void testarDistanciaEntreRobosTerrestre() {
        System.out.println("\n== Teste de Distância Entre Robôs Terrestres ==");
        
        Ambiente ambiente = new Ambiente(30, 30, 50);

        // Teste 1: Distância entre dois robôs terrestres
        RoboTerrestre robo1 = new RoboTerrestre("Trator1", "Norte", ambiente, MateriaisRobo.ACO, 0, 0, 4, 5);
        RoboTerrestre robo2 = new RoboTerrestre("Trator2", "Sul", ambiente, MateriaisRobo.ALUMINIO, 3, 4, 6, 8);
        
        double distancia = robo1.distanciaRobo(robo2);
        verificar("Distância entre dois robôs terrestres deve ser 5.0", 
                 Math.abs(distancia - 5.0) < 0.001);
        
        // Teste 2: Distância entre robôs na mesma posição
        RoboTerrestre robo3 = new RoboTerrestre("Trator3", "Oeste", ambiente, MateriaisRobo.PLASTICO, 0, 0, 7, 10);
        verificar("Distância entre robôs terrestres na mesma posição deve ser 0", 
                 robo1.distanciaRobo(robo3) == 0.0);
    }
    
    private static void testarDistanciaComRoboAereo() {
        System.out.println("\n== Teste de Distância com Robô Aéreo ==");
        
        Ambiente ambiente = new Ambiente(30, 30, 50);
        
        // Simulação de robôs para teste
        RoboAereo drone = new RoboAereo("Drone1", "Norte", MateriaisRobo.FIBRA_CARBONO, 3, 4, 4, 10, 50, ambiente);
        RoboTerrestre trator = new RoboTerrestre("Trator4", "Leste", ambiente, MateriaisRobo.ACO, 0, 0, 5, 7);
        
        double distancia = trator.distanciaRobo(drone);
        verificar("Distância 3D entre terrestre e aéreo deve ser aproximadamente 11.66", 
                 Math.abs(distancia - 11.18) < 0.1); // √(3² + 4² + 10²) ≈ 11.18
        
        // Teste com drone diretamente acima
        RoboAereo droneCima = new RoboAereo("DroneCima", "Norte", MateriaisRobo.ALUMINIO, 0, 0, 5, 10, 40, ambiente);
        distancia = trator.distanciaRobo(droneCima);
        verificar("Distância com drone diretamente acima deve ser 10.0", 
                 Math.abs(distancia - 10.0) < 0.001);
    }
    
    private static void testarSensorColisao() {
        System.out.println("\n== Teste de Sensor de Colisão ==");
        
        // Configuração do ambiente
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        // Teste 1: Verificar se o sensor é inicializado
        RoboTerrestre robo = new RoboTerrestre("Trator5", "Norte", ambiente, MateriaisRobo.ACO, 5, 5, 5, 10);
        verificar("Sensor de colisão deve ser inicializado", 
                 robo.getSensorColisao() != null);
        verificar("Sensor de colisão deve estar ativo por padrão", 
                 robo.getSensorColisao().isAtivo());
        
        // Teste 2: Detectar sem colisões
        ambiente.adicionarRobo(robo);
        int resultadoSemColisao = robo.getSensorColisao().acionar();
        verificar("Sem objetos próximos, sensor deve retornar 0", 
                 resultadoSemColisao == 0);
        
        // Teste 3: Detectar colisão com outro robô
        RoboTerrestre roboAlvo = new RoboTerrestre("Alvo", "Sul", ambiente, MateriaisRobo.ALUMINIO, 5, 6, 4, 8);
        ambiente.adicionarRobo(roboAlvo);
        
        // Movendo para colidir
        robo.mover(0, 1, 5, ambiente);

        verificar("Movimento deve ser interrompido antes de colidir", 
                 robo.getPosicaoY() == 5);
        verificar("Movimento deve ser interrompido antes de colidir", 
                 robo.getPosicaoY() < 6);
        
        // Teste 4: Verificar objeto colidido
        robo.getSensorColisao().acionar(); // Acionar sensor para detectar colisão
        verificar("Último robô colidido deve ser o robô alvo", 
                 robo.getSensorColisao().getUltimoRoboColidido() == roboAlvo);
    }
    
    private static void testarColisaoComObstaculos() {
        System.out.println("\n== Teste de Colisão com Obstáculos ==");
        
        // Configuração do ambiente
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        // Cria robô e obstáculos
        RoboTerrestre robo = new RoboTerrestre("Trator6", "Leste", ambiente, MateriaisRobo.PLASTICO, 5, 5, 5, 10);
        ambiente.adicionarRobo(robo);
        
        // Adiciona obstáculo parede
        Obstaculo parede = new Obstaculo(TipoObstaculo.PAREDE, 8, 9, 5, 6);
        ambiente.adicionarObstaculo(parede);
        
        // Teste 1: Detecção de obstáculo
        robo.mover(3, 0, 5, ambiente);
        verificar("Movimento deve parar antes do obstáculo", 
                 robo.getPosicaoX() == 7);
        verificar("Posição Y não deve mudar", 
                 robo.getPosicaoY() == 5);
        
        // Forçar acionar o sensor
        // Sensor só é acionado quando o robô está com a mesma coordenada
        // do obstáculo, então o mover retrocede o robô para a posição
        int tipoColisao = robo.getSensorColisao().acionar();
        verificar("Sensor não deve detectar obstáculo", 
                 tipoColisao == 0);
        
        // Teste 2: Colisão em outra direção
        Obstaculo predio = new Obstaculo(TipoObstaculo.PREDIO, 5, 6, 1, 3);
        ambiente.adicionarObstaculo(predio);
        
        // Em vez de usar setPosicaoX/Y (protected), remover o robô antigo e criar um novo
        ambiente.removerRobo(robo);
        RoboTerrestre novoRobo = new RoboTerrestre("Trator6-B", "Leste", ambiente, MateriaisRobo.PLASTICO, 5, 1, 5, 10);
        ambiente.adicionarRobo(novoRobo);
        
        // Usar o novo robô para o teste
        novoRobo.mover(0, 2, 5, ambiente);
        verificar("Movimento no eixo Y deve detectar obstáculo", 
                 novoRobo.getPosicaoY() == 1);
    }
    
    private static void testarColisaoComBuraco() {
        System.out.println("\n== Teste de Colisão com Buraco ==");
        
        // Configuração do ambiente
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        // Cria robô e buraco
        RoboTerrestre robo = new RoboTerrestre("Trator7", "Norte", ambiente, MateriaisRobo.FIBRA_VIDRO, 5, 5, 5, 10);
        ambiente.adicionarRobo(robo);
        
        // Adiciona um buraco
        Obstaculo buraco = new Obstaculo(TipoObstaculo.BURACO, 5, 6, 7, 8);
        ambiente.adicionarObstaculo(buraco);
        
        // Verifica que o robô está no ambiente antes do movimento
        verificar("Robô deve estar no ambiente antes de cair no buraco", 
                 ambiente.getListaRobos().contains(robo));
        
        // Tenta mover para o buraco
        robo.mover(0, 3, 5, ambiente);
        
        // Verifica que o robô foi removido do ambiente
        verificar("Robô deve ser removido do ambiente após cair no buraco", 
                 !ambiente.getListaRobos().contains(robo));
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