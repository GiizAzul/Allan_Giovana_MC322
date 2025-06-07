import ambiente.Ambiente;
import ambiente.CentralComunicacao;
import excecoes.ambiente.ForaDosLimitesException;
import excecoes.robos.especificos.AlvoInvalidoException;
import excecoes.robos.especificos.MunicaoInsuficienteException;
import excecoes.robos.gerais.ColisaoException;
import excecoes.robos.gerais.MovimentoInvalidoException;
import excecoes.robos.gerais.RoboDesligadoException;
import excecoes.robos.gerais.RoboDestruidoPorBuracoException;
import excecoes.sensor.SensorException;
import robos.aereos.DroneAtaque;
import robos.aereos.DroneVigilancia;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;


public class TestRoboAereo extends TestBase {
    
    public static void main(String[] args) throws AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException, SensorException, MovimentoInvalidoException {
        executarTestes();
    }
    
    public static void executarTestes() throws AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException, SensorException, MovimentoInvalidoException {
        System.out.println("Iniciando testes da classe RoboAereo...");
        
        testarConstrutor();
        testarSubirDescer();
        testarMovimento3D();
        testarAltitudeMaxima();
        testarDistanciaEntreRoboAereoTerrestre();
        testarDistanciaEntreRobosAereos();
        testarExibirPosicao();
        testarBarometro();
        testarEspecificidadesDroneAtaque();
        testarEspecificidadesDroneVigilancia();
        testarComunicacaoDroneVigilancia();
        
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
        verificar("Posição X deve ser 10", droneAtaque.getXInterno() == 10);
        verificar("Posição Y deve ser 10", droneAtaque.getYInterno() == 10);
        
        // Teste de criação de DroneVigilancia
        Robo droneVigilancia = ambiente.criarRobo(2, 2, "DV1", "Oeste", MateriaisRobo.PLASTICO, 
                                                 20, 20, 3, 5, 8, 5, 50.0f, 60.0f, 90.0f);
        verificar("DroneVigilancia deve ser criado com sucesso", droneVigilancia != null);
        verificar("Nome do drone deve ser DV1", droneVigilancia.getNome().equals("DV1"));
        verificar("Direção deve ser Oeste", droneVigilancia.getDirecao().equals("Oeste"));
    }
    
    private static void testarSubirDescer() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException, MovimentoInvalidoException {
        System.out.println("\n== Teste de Subir e Descer ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        DroneAtaque drone = (DroneAtaque)ambiente.criarRobo(2, 1, "DA2", "Norte", MateriaisRobo.ALUMINIO, 
                                      10, 10, 2, 5, 8, 5, 200, 10);
        adicionarEntidadeTest(drone, ambiente);
        
        int altitudeInicial = drone.getZ();
        drone.executarTarefa("mover",10,10,4,ambiente);
        verificar("Drone deve subir 2 unidades", drone.getZ() == altitudeInicial + 2);
        altitudeInicial = drone.getZ();
        drone.executarTarefa("mover",10,10,3,ambiente);
        verificar("Drone deve descer 1 unidade", drone.getZ() == altitudeInicial - 1);
    }
    
    private static void testarMovimento3D() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException, MovimentoInvalidoException {
        System.out.println("\n== Teste de Movimento 3D ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        DroneAtaque drone = (DroneAtaque) ambiente.criarRobo(2, 1, "DA3", "Leste", MateriaisRobo.FIBRA_CARBONO, 
                                      5, 5, 2, 5, 8, 5, 200, 10);
        adicionarEntidadeTest(drone, ambiente);
        drone.executarTarefa("mover",8,9,4,ambiente);
        verificar("Posição X após movimento deve ser 8", drone.getX() == 8);
        verificar("Posição Y após movimento deve ser 9", drone.getY() == 9);
        verificar("Posição Z após movimento deve ser 4", drone.getZ() == 4);
    }
    
    private static void testarAltitudeMaxima() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException, MovimentoInvalidoException {
        System.out.println("\n== Teste de Altitude Máxima ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        DroneAtaque drone = (DroneAtaque) ambiente.criarRobo(2, 1, "DA4", "Sul", MateriaisRobo.PLASTICO, 
                                      10, 10, 2, 5, 8, 5, 200, 10);
        adicionarEntidadeTest(drone, ambiente);
        
        drone.executarTarefa("mover",0,0,10,ambiente);
        verificar("Altitude não deve ultrapassar o máximo", drone.getZ() <= 8);
        
        drone.executarTarefa("mover",0,0,-20,ambiente);
        verificar("Altitude não deve ser negativa", drone.getZ() >= 0);
    }
    
    private static void testarDistanciaEntreRoboAereoTerrestre() throws SensorException {
        System.out.println("\n== Teste de Distância entre Robô Aéreo e Terrestre ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        Robo droneAereo = ambiente.criarRobo(2, 1, "DA5", "Norte", MateriaisRobo.ALUMINIO, 
                                            0, 0, 2, 5, 8, 5, 200, 10);
        Robo roboTerrestre = ambiente.criarRobo(1, 1, "T1", "Sul", MateriaisRobo.ACO, 
                                              3, 4, 0, 2, 5, 100, 10);
        
        adicionarEntidadeTest(droneAereo, ambiente);
        adicionarEntidadeTest(roboTerrestre, ambiente);
        
        double distancia = droneAereo.distanciaRobo(roboTerrestre);
        verificar("Distância deve ser calculada em 3D", distancia > 0);
    }
    
    private static void testarDistanciaEntreRobosAereos() throws SensorException {
        System.out.println("\n== Teste de Distância entre Robôs Aéreos ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        
        Robo drone1 = ambiente.criarRobo(2, 1, "DA6", "Norte", MateriaisRobo.ALUMINIO, 
                                       0, 0, 2, 5, 8, 5, 200, 10);
        Robo drone2 = ambiente.criarRobo(2, 1, "DA7", "Sul", MateriaisRobo.ALUMINIO, 
                                       3, 4, 3, 5, 8, 5, 200, 10);
        
        adicionarEntidadeTest(drone1, ambiente);
        adicionarEntidadeTest(drone2, ambiente);
        
        double distancia = drone1.distanciaRobo(drone2);
        verificar("Distância deve ser calculada em 3D", distancia > 0);
    }
    
    private static void testarExibirPosicao() throws SensorException {
        System.out.println("\n== Teste de Exibição de Posição ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        Robo drone = ambiente.criarRobo(2, 1, "DA8", "Oeste", MateriaisRobo.PLASTICO, 
                                      15, 25, 3, 5, 8, 5, 200, 10);
        adicionarEntidadeTest(drone, ambiente);
        
        String posicao = drone.exibirPosicao();
        verificar("Exibição deve incluir posição X", posicao.contains("15"));
        verificar("Exibição deve incluir posição Y", posicao.contains("25"));
        verificar("Exibição deve incluir posição Z", posicao.contains("3"));
    }
    
    private static void testarBarometro() throws AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException, SensorException, MovimentoInvalidoException {
        System.out.println("\n== Teste do Barômetro ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        DroneAtaque drone = (DroneAtaque) ambiente.criarRobo(2, 1, "DA9", "Norte", MateriaisRobo.ALUMINIO, 
                                      10, 10, 2, 5, 8, 5, 200, 10);
        adicionarEntidadeTest(drone, ambiente);
        
        double pressaoInicial = drone.getBarometro().acionar();
        drone.executarTarefa("mover",10,10,4,ambiente);
        verificar("Altitude deve aumentar após subida", drone.getBarometro().acionar() < pressaoInicial);
        pressaoInicial = drone.getBarometro().acionar();
        drone.executarTarefa("mover",10,10,3,ambiente);
        verificar("Altitude deve diminuir após descida", drone.getBarometro().acionar() > pressaoInicial); 
    }
    
    private static void testarEspecificidadesDroneAtaque() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException, MovimentoInvalidoException {
        System.out.println("\n== Teste de Especificidades do DroneAtaque ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        DroneAtaque drone = (DroneAtaque) ambiente.criarRobo(2, 1, "DA1", "Norte", 
                                                           MateriaisRobo.FIBRA_CARBONO, 
                                                           5, 5, 2, 5, 8, 5, 200, 10);
        adicionarEntidadeTest(drone, ambiente);

        // Teste de ataque em coordenadas
        String resultado = drone.executarTarefa("atirar coord", 7, 7, 2, 1, ambiente);
        verificar("Deve permitir atirar em coordenadas vazias", 
                 resultado.contains("Disparado realizado") && 
                 resultado.contains("Nenhum alvo foi atingido"));

        // Teste de ataque em robô
        Robo alvo = ambiente.criarRobo(1, 1, "T1", "Sul", MateriaisRobo.ACO, 7, 7, 0, 2, 5, 100, 10);
        adicionarEntidadeTest(alvo, ambiente);
        resultado = drone.executarTarefa("atirar coord", 7, 7, 0, 2, ambiente);
        verificar("Deve acertar robô alvo", resultado.contains("Robô T1 foi atingido"));

        // Teste de escudo
        String defesa = drone.defender(30, ambiente);
        verificar("Escudo deve absorver dano", defesa.contains("defendeu o dano em seu escudo"));

        // Teste de munição insuficiente
        resultado = drone.executarTarefa("atirar coord", 7, 7, 0, 300, ambiente);
        verificar("Deve identificar munição insuficiente", resultado.equals("Municao insuficiente para realizar o disparo"));

        // Teste de alvo fora de alcance
        resultado = drone.executarTarefa("atirar coord", 50, 50, 0, 1, ambiente);
        verificar("Deve identificar alvo fora do alcance", resultado.equals("Alvo fora do alcance"));

        // Teste de identificação de alvos
        resultado = drone.executarTarefa("identificar");
        verificar("Deve identificar objetos próximos", 
                 resultado.contains("Robô encontrado") || resultado.contains("Obstáculo encontrado"));
    }

    private static void testarEspecificidadesDroneVigilancia() throws SensorException, AlvoInvalidoException, MunicaoInsuficienteException, ForaDosLimitesException, RoboDestruidoPorBuracoException, ColisaoException, MovimentoInvalidoException {
        System.out.println("\n== Teste de Especificidades do DroneVigilancia ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        DroneVigilancia drone = (DroneVigilancia) ambiente.criarRobo(2, 2, "DV1", "Norte", 
                                                                    MateriaisRobo.PLASTICO, 
                                                                    10, 10, 2, 5, 8, 5, 50.0f, 60.0f, 160.0f);
        adicionarEntidadeTest(drone, ambiente);

        // Teste de varredura de área
        Robo alvo = ambiente.criarRobo(1, 1, "T1", "Sul", MateriaisRobo.ACO, 12, 12, 0, 2, 5, 100, 10);
        adicionarEntidadeTest(alvo, ambiente);
        
        String resultado = drone.executarTarefa("varrer", ambiente, 10, 10, 5);
        verificar("Deve identificar objetos na área de varredura", 
                 resultado.contains("Objetos encontrados") && resultado.contains("T1"));

        // Teste de camuflagem
        resultado = drone.executarTarefa("camuflagem");
        verificar("Deve ativar camuflagem", drone.isCamuflado());

        resultado = drone.executarTarefa("varrer", ambiente, 10, 10, 5);
        verificar("Drone camuflado não deve ser detectado", !resultado.contains("DV1"));

        drone.executarTarefa("camuflagem");
        verificar("Deve desativar camuflagem", !drone.isCamuflado());

        // Teste de identificação
        resultado = drone.executarTarefa("identificar");
        verificar("Deve identificar objetos próximos", 
                 resultado.contains("Robô encontrado") || resultado.contains("Obstáculo encontrado"));
    }

    private static void testarComunicacaoDroneVigilancia() {
        System.out.println("\n== Teste de Comunicação do DroneVigilancia ==");
        
        Ambiente ambiente = new Ambiente(50, 50, 100);
        CentralComunicacao central = new CentralComunicacao();
        
        DroneVigilancia drone1 = (DroneVigilancia) ambiente.criarRobo(2, 2, "DV1", "Norte", 
                                                                     MateriaisRobo.PLASTICO, 
                                                                     10, 10, 2, 5, 8, 5, 50.0f, 60.0f, 90.0f);
        DroneVigilancia drone2 = (DroneVigilancia) ambiente.criarRobo(2, 2, "DV2", "Sul", 
                                                                     MateriaisRobo.PLASTICO, 
                                                                     15, 15, 2, 5, 8, 5, 50.0f, 60.0f, 90.0f);
        adicionarEntidadeTest(drone1, ambiente);
        adicionarEntidadeTest(drone2, ambiente);

        try {
            // Teste de envio de mensagem
            String resultado = drone1.enviarMensagem(drone2, "Teste de mensagem", central);
            verificar("Deve enviar mensagem com sucesso", 
                     resultado.contains("Mensagem enviada com sucesso"));

            // Teste de histórico da central
            String historico = central.exibirMensagens();
            verificar("Central deve registrar a mensagem", 
                     historico.contains("De: DV1 | Para: DV2"));

            // Teste de comunicação com drone desligado
            drone1.desligar();
            try {
                drone1.enviarMensagem(drone2, "Teste com drone desligado", central);
                verificar("Deve impedir envio com drone desligado", false);
            } catch (RoboDesligadoException e) {
                verificar("Deve lançar exceção de drone desligado", 
                         e.getMessage().contains("desligado"));
            }

        } catch (Exception e) {
            verificar("Não deve ocorrer erro na comunicação", false);
            System.out.println("Erro: " + e.getMessage());
        }
    }
}