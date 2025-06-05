import ambiente.Ambiente;
import ambiente.CentralComunicacao;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import excecoes.ambiente.ErroComunicacaoException;
import excecoes.ambiente.ForaDosLimitesException;
import excecoes.robos.especificos.AlvoInvalidoException;
import excecoes.robos.especificos.MunicaoInsuficienteException;
import excecoes.robos.gerais.ColisaoException;
import excecoes.robos.gerais.RoboDesligadoException;
import excecoes.robos.gerais.RoboDestruidoPorBuracoException;
import excecoes.sensor.SensorException;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.terrestres.Correios;
import robos.terrestres.RoboTerrestre;
import robos.terrestres.TanqueGuerra;

public class TestRoboTerrestre extends TestBase {

    public static void main(String[] args) throws ErroComunicacaoException, RoboDesligadoException, SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        executarTestes();
    }

    public static void executarTestes() throws ErroComunicacaoException, RoboDesligadoException, SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("Iniciando testes da classe RoboTerrestre...");

        testarConstrutor();
        testarMovimentoComVelocidade();
        testarAlteracaoVelocidadeMaxima();
        testarDistanciaEntreRobosTerrestre();
        testarDistanciaComRoboAereo();
        testarSensorColisao();
        testarColisaoComObstaculos();
        testarColisaoComBuraco();
        testarEspecificidadesTanqueGuerra();
        testarEspecificidadesCorreios();
        testarLimitesMovimento();
        testarIntegridadeRobo();

        System.out.println("\nTodos os testes para RoboTerrestre foram concluídos!");
    }

    private static void testarConstrutor() {
        System.out.println("\n== Teste do Construtor ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        // Teste de criação de TanqueGuerra
        Robo tanque = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        verificar("TanqueGuerra deve ser criado com sucesso", tanque != null);
        verificar("Nome deve ser T1", tanque.getNome().equals("T1"));
        verificar("Posição X deve ser 5", tanque.getXInterno() == 5);
        verificar("Posição Y deve ser 5", tanque.getYInterno() == 5);
        verificar("Direção deve ser Norte", tanque.getDirecao().equals("Norte"));

        // Teste de criação de Correios
        Robo correio = ambiente.criarRobo(1, 2, "C1", "Sul", MateriaisRobo.PLASTICO, 10, 10, 0, 3, 5, 50, 25.0f);
        verificar("Correios deve ser criado com sucesso", correio != null);
        verificar("Nome deve ser C1", correio.getNome().equals("C1"));
        verificar("Material deve ser PLASTICO", correio.getMateriaisRobo() == MateriaisRobo.PLASTICO);
    }

    private static void testarMovimentoComVelocidade() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("\n== Teste de Movimento com Velocidade ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        RoboTerrestre robo = (RoboTerrestre) ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 0, 0, 0, 5, 10, 100, 10);
        adicionarEntidadeTest(robo, ambiente);

        robo.executarTarefa("mover",3,4,2,ambiente);
        verificar("Posição X após movimento deve ser 3", robo.getX() == 3);
        verificar("Posição Y após movimento deve ser 4", robo.getY() == 4);

        // Teste com obstáculo
        Obstaculo obstaculo = new Obstaculo(TipoObstaculo.PAREDE, 5, 6, 4, 5);
        adicionarEntidadeTest(obstaculo, ambiente);

        robo.executarTarefa("mover",3,0,2,ambiente);
        verificar("Movimento deve ser bloqueado pelo obstáculo", robo.getX() == 4);
    }

    private static void testarAlteracaoVelocidadeMaxima() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("\n== Teste de Alteração da Velocidade Máxima ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);
        RoboTerrestre robo = (RoboTerrestre) ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 5, 5, 0, 5, 10,
                100, 10);

        robo.executarTarefa("velocidade", 8);
        verificar("Nova velocidade deve ser aceita se menor que máxima", robo.getVelocidadeMaxima() == 8);

        robo.executarTarefa("mover", 1, 1, 7, ambiente);
        verificar("Deve permitir movimento com velocidade dentro do novo limite", robo.getX() == 6 && robo.getY() == 6);
    }

    private static void testarDistanciaEntreRobosTerrestre() throws SensorException {
        System.out.println("\n== Teste de Distância Entre Robôs Terrestres ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        Robo robo1 = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 0, 0, 0, 5, 10, 100, 10);
        Robo robo2 = ambiente.criarRobo(1, 1, "T2", "Sul", MateriaisRobo.ACO, 3, 4, 0, 5, 10, 100, 10);

        adicionarEntidadeTest(robo1, ambiente);
        adicionarEntidadeTest(robo2, ambiente);

        double distancia = robo1.distanciaRobo(robo2);
        verificar("Distância deve ser 5.0", Math.abs(distancia - 5.0) < 0.001);
    }

    private static void testarDistanciaComRoboAereo() throws SensorException {
        System.out.println("\n== Teste de Distância com Robô Aéreo ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        Robo terrestre = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 0, 0, 0, 5, 10, 100, 10);
        Robo aereo = ambiente.criarRobo(2, 1, "D1", "Norte", MateriaisRobo.FIBRA_CARBONO, 3, 4, 5, 5, 8, 10, 200, 10);

        adicionarEntidadeTest(terrestre, ambiente);
        adicionarEntidadeTest(aereo, ambiente);

        double distancia = terrestre.distanciaRobo(aereo);
        verificar("Distância 3D deve ser calculada corretamente", Math.abs(distancia - 7.071) < 0.001);
    }

    private static void testarSensorColisao() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("\n== Teste de Sensor de Colisão ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        RoboTerrestre robo = (RoboTerrestre)ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        adicionarEntidadeTest(robo, ambiente);

        Obstaculo obstaculo = new Obstaculo(TipoObstaculo.PAREDE, 5, 6, 6, 7);
        adicionarEntidadeTest(obstaculo, ambiente);
        robo.executarTarefa("mover",0,2,2,ambiente);
        verificar("Movimento deve ser interrompido antes da colisão", robo.getY() == 5);
    }

    private static void testarColisaoComObstaculos() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("\n== Teste de Colisão com Obstáculos ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        RoboTerrestre robo = (RoboTerrestre) ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        adicionarEntidadeTest(robo, ambiente);

        // Teste com diferentes tipos de obstáculos
        Obstaculo parede = new Obstaculo(TipoObstaculo.PAREDE, 7, 8, 5, 6);
        Obstaculo arvore = new Obstaculo(TipoObstaculo.ARVORE, 5, 6, 7, 8);

        adicionarEntidadeTest(parede, ambiente);
        adicionarEntidadeTest(arvore, ambiente);
        robo.executarTarefa("mover",3,0,2,ambiente);
        verificar("Movimento deve parar antes da parede", robo.getX() == 6);
        robo.executarTarefa("mover",0,3,2,ambiente);
        verificar("Movimento deve parar antes da árvore", robo.getY() == 6);
    }

    private static void testarColisaoComBuraco() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("\n== Teste de Colisão com Buraco ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        RoboTerrestre robo = (RoboTerrestre)ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        adicionarEntidadeTest(robo, ambiente);

        Obstaculo buraco = new Obstaculo(TipoObstaculo.BURACO, 5, 6, 7, 8);
        adicionarEntidadeTest(buraco, ambiente);

        verificar("Robô deve estar no ambiente inicialmente", ambiente.getEntidades().contains(robo));

        robo.executarTarefa("mover",0,3,2,ambiente);

        verificar("Robô deve ser removido após cair no buraco", !ambiente.getEntidades().contains(robo));
    }

    private static void testarEspecificidadesTanqueGuerra() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("\n== Teste de Especificidades do TanqueGuerra ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        TanqueGuerra tanque = (TanqueGuerra) ambiente.criarRobo(1, 1, "T1", "Norte",
                MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        adicionarEntidadeTest(tanque, ambiente);

        // Teste de ataque sem alvo
        String resultado = tanque.executarTarefa("atirar", 10, 10, 1, ambiente);
        verificar("Deve permitir atirar em posição vazia",
                resultado.contains("Disparado realizado no alvo (10, 10)") &&
                        resultado.contains("Nenhum alvo foi atingido"));

        // Teste de ataque em alvo fora de alcance
        resultado = tanque.executarTarefa("atirar", 18, 18, 1, ambiente);
        verificar("Deve identificar alvo fora de alcance",
                resultado.equals("Alvo fora do alcance"));

        // Teste de ataque em robô
        Robo alvo = ambiente.criarRobo(1, 2, "C1", "Sul", MateriaisRobo.PLASTICO, 7, 7, 0, 3, 5, 50, 25.0f);
        adicionarEntidadeTest(alvo, ambiente);
        resultado = tanque.executarTarefa("atirar", 7, 7, 2, ambiente);
        verificar("Deve acertar robô alvo",
                resultado.contains("Robô C1 foi atingido"));

        // Teste de ataque em obstáculo
        Obstaculo obstaculo = new Obstaculo(TipoObstaculo.PAREDE, 6, 6, 6, 6);
        adicionarEntidadeTest(obstaculo, ambiente);
        resultado = tanque.executarTarefa("atirar", 6, 6, 1, ambiente);
        verificar("Deve acertar obstáculo",
                resultado.contains("Obstáculo PAREDE foi atingido"));

        // Teste de munição insuficiente
        resultado = tanque.executarTarefa("atirar", 7, 7, 200, ambiente);
        verificar("Deve identificar munição insuficiente",
                resultado.equals("Municao insuficiente para realizar o disparo"));

        // Teste de recarga
        resultado = tanque.executarTarefa("recarregar", 50);
        verificar("Deve permitir recarregar munição",
                resultado.equals("Recarregamento concluido"));

        // Teste de auto-ataque
        resultado = tanque.executarTarefa("atirar", 5, 5, 1, ambiente);
        verificar("Não deve permitir atirar na própria posição",
                resultado.equals("Não é possível atirar no próprio robô"));

        // Teste de resistência
        verificar("Tanque deve ter alta resistência", tanque.getIntegridade() == 100);
        tanque.defender(30, ambiente);
        verificar("Tanque deve manter integridade após dano pequeno", tanque.getIntegridade() == 70);

    }

    private static void testarEspecificidadesCorreios() throws ErroComunicacaoException, RoboDesligadoException, SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("\n== Teste de Especificidades do Correios ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);
        CentralComunicacao central = new CentralComunicacao();

        Correios correio = (Correios) ambiente.criarRobo(1, 2, "C1", "Sul",
                MateriaisRobo.PLASTICO, 5, 5, 0, 3, 5, 50, 25.0f);
        adicionarEntidadeTest(correio, ambiente);

        // Teste de carregamento de pacotes
        String resultado = correio.executarTarefa("carregar", "P1", 10.0f);
        verificar("Deve permitir carregar pacote dentro do limite",
                resultado.equals("Pacote carregado com sucesso"));

        resultado = correio.executarTarefa("carregar", "P2", 30.0f);
        verificar("Deve impedir carregamento acima do peso máximo",
                resultado.contains("Não foi possível carregar o pacote"));

        // Teste de listagem de entregas
        resultado = correio.executarTarefa("listar");
        verificar("Deve listar entregas pendentes",
                resultado.contains("P1"));

        // Teste de entrega normal
        resultado = correio.executarTarefa("entregar", "P1", 7, 7, ambiente);
        verificar("Deve entregar pacote em posição válida",
                resultado.contains("Pacote P1 entregue na posição (7, 7)"));

        // Teste de entrega de pacote inexistente
        resultado = correio.executarTarefa("entregar", "P3", 7, 7, ambiente);
        verificar("Deve indicar pacote inexistente",
                resultado.contains("Pacote P3 não encontrado na carga"));

        // Teste de entrega em buraco
        Obstaculo buraco = new Obstaculo(TipoObstaculo.BURACO, 8, 8, 8, 8);
        adicionarEntidadeTest(buraco, ambiente);
        correio.executarTarefa("carregar", "P4", 5.0f);
        resultado = correio.executarTarefa("entregar", "P4", 8, 8, ambiente);
        verificar("Deve cobrir buraco ao entregar pacote",
                resultado.contains("Buraco coberto na posição"));

        // Teste de entrega com obstáculo no caminho
        Obstaculo parede = new Obstaculo(TipoObstaculo.PAREDE, 7, 7, 7, 7);
        adicionarEntidadeTest(parede, ambiente);
        correio.executarTarefa("carregar", "P5", 5.0f);
        resultado = correio.executarTarefa("entregar", "P5", 7, 7, ambiente);
        verificar("Deve falhar ao tentar entregar com obstáculo no caminho",
                resultado.contains("Erro ao entregar pacote"));

        // Teste expandido de comunicação
        System.out.println("\n=== Testes de Comunicação ===");

        try {
            // Criação de robôs e central
            Correios outroCorreio = (Correios) ambiente.criarRobo(1, 2, "C2", "Norte",
                    MateriaisRobo.PLASTICO, 9, 9, 0, 3, 5, 50, 25.0f);
            adicionarEntidadeTest(outroCorreio, ambiente);

            // Teste de envio de mensagem bem-sucedido
            resultado = correio.enviarMensagem(outroCorreio, "Teste de mensagem", central);
            verificar("Deve permitir envio de mensagem",
                    resultado.contains("Mensagem enviada com sucesso") &&
                            resultado.contains("C2 recebeu mensagem de C1"));

            // Teste do histórico da central
            String historico = central.exibirMensagens();
            verificar("Central deve registrar a mensagem",
                    historico.contains("De: C1 | Para: C2") &&
                            historico.contains("Teste de mensagem"));

            // Teste de comunicação com robô desligado
            correio.desligar();
            try {
                correio.enviarMensagem(outroCorreio, "Teste com robô desligado", central);
                verificar("Deve impedir envio de mensagem com robô desligado", false);
            } catch (RoboDesligadoException e) {
                verificar("Deve lançar exceção ao tentar enviar mensagem com robô desligado",
                        e.getMessage().contains("desligado"));
            }

            // Teste de recebimento com robô desligado
            outroCorreio.desligar();
            try {
                correio.ligar();
                correio.enviarMensagem(outroCorreio, "Mensagem para robô desligado", central);
                verificar("Deve impedir envio para robô desligado", false);
            } catch (ErroComunicacaoException e) {
                verificar("Deve lançar exceção ao tentar enviar mensagem para robô desligado",
                        e.getMessage().contains("desligado"));
            }

            // Teste de comunicação com destinatário nulo
            try {
                correio.enviarMensagem(null, "Teste com destinatário nulo", central);
                verificar("Deve impedir envio para destinatário nulo", false);
            } catch (ErroComunicacaoException e) {
                verificar("Deve lançar exceção ao tentar enviar mensagem para destinatário nulo",
                        e.getMessage().contains("nulo"));
            }

            // Teste de múltiplas mensagens
            outroCorreio.ligar();
            correio.enviarMensagem(outroCorreio, "Mensagem 1", central);
            correio.enviarMensagem(outroCorreio, "Mensagem 2", central);
            historico = central.exibirMensagens();
            verificar("Central deve manter histórico de múltiplas mensagens",
                    historico.contains("Mensagem 1") && historico.contains("Mensagem 2"));

        } catch (Exception e) {
            verificar("Não deve ocorrer erro inesperado na comunicação", false);
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void testarLimitesMovimento() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException {
        System.out.println("\n== Teste de Limites de Movimento ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        RoboTerrestre robo = (RoboTerrestre)ambiente.criarRobo(1, 1, "T1", "Norte",
                MateriaisRobo.ACO, 0, 0, 0, 5, 10, 100, 10);
        adicionarEntidadeTest(robo, ambiente);

        // Teste de movimento nos limites do ambiente
        robo.executarTarefa("mover",25,0,2,ambiente);
        verificar("Movimento X não deve ultrapassar limite do ambiente",
                robo.getX() < ambiente.getTamX());
        
        robo.executarTarefa("mover",0,25,2,ambiente);
        verificar("Movimento Y não deve ultrapassar limite do ambiente",
                robo.getY() < ambiente.getTamY());

        // Teste de movimento negativo
        robo.executarTarefa("mover",-5,-5,2,ambiente);
        verificar("Posição não deve ser negativa", robo.getX() >= 0 && robo.getY() >= 0);
    }

    private static void testarIntegridadeRobo() {
        System.out.println("\n== Teste de Integridade do Robô ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        // Teste com TanqueGuerra
        TanqueGuerra tanque = (TanqueGuerra) ambiente.criarRobo(1, 1, "T1", "Norte",
                MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        adicionarEntidadeTest(tanque, ambiente);

        int integridadeInicial = tanque.getIntegridade();
        tanque.defender(30, ambiente);
        verificar("Dano deve reduzir integridade do tanque",
                tanque.getIntegridade() == integridadeInicial - 30);

        // Teste com Correios
        Correios correio = (Correios) ambiente.criarRobo(1, 2, "C1", "Sul",
                MateriaisRobo.PLASTICO, 10, 10, 0, 3, 5, 50, 25.0f);
        adicionarEntidadeTest(correio, ambiente);

        integridadeInicial = correio.getIntegridade();
        correio.defender(20, ambiente);
        verificar("Dano deve reduzir integridade do correio",
                correio.getIntegridade() == integridadeInicial - 20);

        // Teste de destruição
        correio.defender(100, ambiente);
        verificar("Robô deve ser desligado quando integridade chega a 0",
                !correio.getEstado());
    }
}