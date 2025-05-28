import ambiente.Ambiente;
import ambiente.CentralComunicacao;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import excecoes.ErroComunicacaoException;
import excecoes.RoboDesligadoException;
import robos.aereos.DroneAtaque;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.terrestres.Correios;
import robos.terrestres.RoboTerrestre;
import robos.terrestres.TanqueGuerra;

public class TestRoboTerrestre {

    public static void main(String[] args) throws ErroComunicacaoException, RoboDesligadoException {
        executarTestes();
    }

    public static void executarTestes() throws ErroComunicacaoException, RoboDesligadoException {
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
        RoboTerrestre robo = (RoboTerrestre) ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 5, 5, 0, 5, 10,
                100, 10);

        robo.executarTarefa("velocidade", 8);
        verificar("Nova velocidade deve ser aceita se menor que máxima", robo.getVelocidadeMaxima() == 8);

        robo.executarTarefa("mover", 1, 1, 7, ambiente);
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

    private static void testarEspecificidadesTanqueGuerra() {
        System.out.println("\n== Teste de Especificidades do TanqueGuerra ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        TanqueGuerra tanque = (TanqueGuerra) ambiente.criarRobo(1, 1, "T1", "Norte",
                MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        ambiente.adicionarEntidade(tanque);

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
        ambiente.adicionarEntidade(alvo);
        resultado = tanque.executarTarefa("atirar", 7, 7, 2, ambiente);
        verificar("Deve acertar robô alvo",
                resultado.contains("Robô C1 foi atingido"));

        // Teste de ataque em obstáculo
        Obstaculo obstaculo = new Obstaculo(TipoObstaculo.PAREDE, 6, 6, 6, 6);
        ambiente.adicionarEntidade(obstaculo);
        resultado = tanque.executarTarefa("atirar", 6, 6, 1, ambiente);
        verificar("Deve acertar obstáculo",
                resultado.contains("Obstáculo PAREDE foi atingido"));

        // Teste de munição insuficiente
        resultado = tanque.executarTarefa("atirar", 7, 7, 200, ambiente);
        verificar("Deve identificar munição insuficiente",
                resultado.equals("Munição insuficiente"));

        // Teste de recarga
        resultado = tanque.executarTarefa("recarregar", 50);
        verificar("Deve permitir recarregar munição",
                resultado.equals("Recarregamento concluido"));

        // Teste de auto-ataque
        resultado = tanque.executarTarefa("atirar", 5, 5, 1, ambiente);
        verificar("Não deve permitir atirar na própria posição",
                resultado.equals("Não é possível atirar na própria posição"));

        // Teste de resistência
        verificar("Tanque deve ter alta resistência", tanque.getIntegridade() == 100);
        tanque.defender(30, ambiente);
        verificar("Tanque deve manter integridade após dano pequeno", tanque.getIntegridade() == 70);

    }

    private static void testarEspecificidadesCorreios() throws ErroComunicacaoException, RoboDesligadoException {
        System.out.println("\n== Teste de Especificidades do Correios ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);
        CentralComunicacao central = new CentralComunicacao();

        Correios correio = (Correios) ambiente.criarRobo(1, 2, "C1", "Sul",
                MateriaisRobo.PLASTICO, 5, 5, 0, 3, 5, 50, 25.0f);
        ambiente.adicionarEntidade(correio);

        // Teste de carregamento de pacotes
        String resultado = correio.executarTarefa("carregar", "P1", 10.0f);
        verificar("Deve permitir carregar pacote dentro do limite",
                resultado.equals("Pacote carregado com sucesso"));

        resultado = correio.executarTarefa("carregar", "P2", 30.0f);
        verificar("Deve impedir carregamento acima do peso máximo",
                resultado.equals("Peso excede a capacidade máxima"));

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
        ambiente.adicionarEntidade(buraco);
        correio.executarTarefa("carregar", "P4", 5.0f);
        resultado = correio.executarTarefa("entregar", "P4", 8, 8, ambiente);
        verificar("Deve cobrir buraco ao entregar pacote",
                resultado.contains("Buraco coberto na posição"));

        // Teste de entrega com obstáculo no caminho
        Obstaculo parede = new Obstaculo(TipoObstaculo.PAREDE, 7, 7, 7, 7);
        ambiente.adicionarEntidade(parede);
        correio.executarTarefa("carregar", "P5", 5.0f);
        resultado = correio.executarTarefa("entregar", "P5", 7, 7, ambiente);
        verificar("Deve falhar ao tentar entregar com obstáculo no caminho",
                resultado.equals("Entrega não concluída"));

        // Teste expandido de comunicação
        System.out.println("\n=== Testes de Comunicação ===");

        try {
            // Criação de robôs e central
            Correios outroCorreio = (Correios) ambiente.criarRobo(1, 2, "C2", "Norte",
                    MateriaisRobo.PLASTICO, 9, 9, 0, 3, 5, 50, 25.0f);
            ambiente.adicionarEntidade(outroCorreio);

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

    private static void testarLimitesMovimento() {
        System.out.println("\n== Teste de Limites de Movimento ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        Robo robo = ambiente.criarRobo(1, 1, "T1", "Norte",
                MateriaisRobo.ACO, 0, 0, 0, 5, 10, 100, 10);
        ambiente.adicionarEntidade(robo);

        // Teste de movimento nos limites do ambiente
        robo.mover(25, 0, ambiente);
        verificar("Movimento X não deve ultrapassar limite do ambiente",
                robo.getX() < ambiente.getTamX());

        robo.mover(0, 25, ambiente);
        verificar("Movimento Y não deve ultrapassar limite do ambiente",
                robo.getY() < ambiente.getTamY());

        // Teste de movimento negativo
        robo.mover(-5, -5, ambiente);
        verificar("Posição não deve ser negativa", robo.getX() >= 0 && robo.getY() >= 0);
    }

    private static void testarIntegridadeRobo() {
        System.out.println("\n== Teste de Integridade do Robô ==");

        Ambiente ambiente = new Ambiente(20, 20, 10);

        // Teste com TanqueGuerra
        TanqueGuerra tanque = (TanqueGuerra) ambiente.criarRobo(1, 1, "T1", "Norte",
                MateriaisRobo.ACO, 5, 5, 0, 5, 10, 100, 10);
        ambiente.adicionarEntidade(tanque);

        int integridadeInicial = tanque.getIntegridade();
        tanque.defender(30, ambiente);
        verificar("Dano deve reduzir integridade do tanque",
                tanque.getIntegridade() == integridadeInicial - 30);

        // Teste com Correios
        Correios correio = (Correios) ambiente.criarRobo(1, 2, "C1", "Sul",
                MateriaisRobo.PLASTICO, 10, 10, 0, 3, 5, 50, 25.0f);
        ambiente.adicionarEntidade(correio);

        integridadeInicial = correio.getIntegridade();
        correio.defender(20, ambiente);
        verificar("Dano deve reduzir integridade do correio",
                correio.getIntegridade() == integridadeInicial - 20);

        // Teste de destruição
        correio.defender(100, ambiente);
        verificar("Robô deve ser desligado quando integridade chega a 0",
                !correio.getEstado());
    }

    private static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            System.out.println("\033[1;32m✓ PASSOU:\033[0m " + descricao);
        } else {
            System.out.println("\033[1;31m✗ FALHOU:\033[0m " + descricao);
        }
    }
}