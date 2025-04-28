import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;

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
        testarDistanciaEntreRobos();
        testarAcessoGPS();  
        testarFuncionamentoGPS();
        testarColisoes();   
        testarExibirPosicao();
        
        System.out.println("\nTodos os testes foram concluídos!");
    }
    
    
    private static void testarConstrutor() {
        System.out.println("\n== Teste do Construtor ==");
        
        // Teste 1: Criação básica de um robô
        Robo robo = new Robo("Geraldinho", "Norte", MateriaisRobo.ALUMINIO, 5, 5, 10);
        verificar("Nome do robô deve ser Geraldinho", 
                 robo.getNome().equals("Geraldinho"));
        verificar("Posição X inicial deve ser 5", 
                 robo.getPosicaoX() == 5);
        verificar("Posição Y inicial deve ser 5", 
                 robo.getPosicaoY() == 5);
        verificar("Direção inicial deve ser Norte", 
                 robo.getDirecao().equals("Norte"));
        verificar("Material do robô deve ser ALUMINIO", 
                 robo.getMateriaisRobo() == MateriaisRobo.ALUMINIO);
        verificar("Velocidade do robô deve ser 10",
                 robo.getVelocidade() == 10);
        verificar("Robô deve estar operando", 
                 robo.getOperando());
        verificar("Integridade inicial deve ser 100", 
                 robo.getIntegridade() == 100);
    }
    
    private static void testarMovimento() {
        System.out.println("\n== Teste de Movimento ==");
        
        // Configuração do ambiente
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        // Teste 1: Movimento simples sem obstáculos
        Robo robo = new Robo("Geraldinho", "Leste", MateriaisRobo.ALUMINIO, 0, 0, 5);
        ambiente.adicionarRobo(robo);
        robo.mover(3, 4, ambiente);
        verificar("Posição X após movimento sem obstáculos deve ser 3", 
                 robo.getPosicaoX() == 3);
        verificar("Posição Y após movimento sem obstáculos deve ser 4", 
                 robo.getPosicaoY() == 4);
        
        // Teste 2: Movimento com obstáculo no caminho
        Robo roboObstaculo = new Robo("Obstáculo", "Oeste", MateriaisRobo.ACO, 5, 4, 3);
        ambiente.adicionarRobo(roboObstaculo);
        robo.mover(5, 0, ambiente);
        verificar("Movimento deve parar antes do obstáculo", 
                 robo.getPosicaoX() == 4);
        verificar("Posição Y não deve mudar", 
                 robo.getPosicaoY() == 4);
        
        // Teste 3: Movimento para fora dos limites do ambiente
        Ambiente ambientePequeno = new Ambiente(5, 5, 5);
        Robo roboLimite = new Robo("RoboLimite", "Norte", MateriaisRobo.FIBRA_CARBONO, 3, 3, 2);
        ambientePequeno.adicionarRobo(roboLimite);
        roboLimite.mover(3, 3, ambientePequeno);
        verificar("Movimento deve parar nos limites do ambiente", 
                 roboLimite.getPosicaoX() <= 5 && roboLimite.getPosicaoY() <= 5);
    }
    
    private static void testarIntegridade() {
        System.out.println("\n== Teste de Integridade ==");
        
        // Teste 1: Definindo integridade
        Robo robo = new Robo("Fernandinho", "Sul", MateriaisRobo.PLASTICO, 10, 10, 8);
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
        Robo robo = new Robo("Geraldinho", "Norte", MateriaisRobo.ACO, 5, 5, 7);
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
        Robo robo = new Robo("Exterminador", "Leste", MateriaisRobo.ACO, 3, 3, 12);
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

    private static void testarDistanciaEntreRobos() {
        System.out.println("\n== Teste de Cálculo de Distância ==");
        
        // Teste 1: Distância em linha reta
        Robo robo1 = new Robo("Geraldo", "Norte", MateriaisRobo.ALUMINIO, 0, 0, 5);
        Robo robo2 = new Robo("Claudio", "Sul", MateriaisRobo.ACO, 3, 4, 8);
        
        double distancia = robo1.distanciaRobo(robo2);
        verificar("Distância deve ser 5.0", 
                 Math.abs(distancia - 5.0) < 0.001);
        
        // Teste 2: Distância zero (mesma posição)
        Robo robo3 = new Robo("Robo3", "Leste", MateriaisRobo.FIBRA_CARBONO, 0, 0, 6);
        verificar("Distância entre robôs na mesma posição deve ser 0", 
                 robo1.distanciaRobo(robo3) == 0.0);
                 
        // Teste 3: Distância com GPS inativo
        robo1.getGPS().desativar();
        double distanciaComGPSInativo = robo1.distanciaRobo(robo2);
        verificar("Cálculo de distância deve lidar com GPS inativo", 
                 distanciaComGPSInativo == -1);
        
        // Restaura GPS para continuar testes
        robo1.getGPS().ativar();
    }

    private static void testarAcessoGPS() {
        System.out.println("\n== Teste de Acesso via GPS ==");
        
        // Criar robô com GPS
        Robo robo = new Robo("TestGPS", "Norte", MateriaisRobo.ALUMINIO, 10, 15, 5);
        
        // Validar acesso via GPS
        verificar("Posição X deve ser obtida via GPS", 
                robo.getPosicaoX() == 10);
        verificar("Posição Y deve ser obtida via GPS", 
                robo.getPosicaoY() == 15);
        
        // Desativar GPS e verificar comportamento
        robo.getGPS().desativar();
        verificar("GPS desativado deve retornar -1 para posição X", 
                robo.getPosicaoX() == -1);
        verificar("GPS desativado deve retornar -1 para posição Y", 
                robo.getPosicaoY() == -1);
        
        // Ativar GPS novamente
        robo.getGPS().ativar();
        verificar("GPS reativado deve retornar posição correta", 
                robo.getPosicaoX() == 10 && robo.getPosicaoY() == 15);
    }
    
    private static void testarFuncionamentoGPS() {
        System.out.println("\n== Teste de Funcionamento do Sensor GPS ==");
        
        // Teste 1: Criar um robô e verificar se o GPS foi inicializado automaticamente
        Robo robo = new Robo("RoboGPS", "Leste", MateriaisRobo.ACO, 7, 8, 6);
        verificar("Robô deve ter um sensor GPS inicializado", 
                 robo.getGPS() != null);
        verificar("GPS deve estar ativo por padrão", 
                 robo.getGPS().isAtivo());
                 
        // Teste 2: Testar o método acionar() do GPS
        int[] coordenadas = robo.getGPS().acionar();
        verificar("GPS.acionar() deve retornar coordenadas corretas", 
                 coordenadas != null && coordenadas[0] == 7 && coordenadas[1] == 8 && coordenadas[2] == 0);
        
        // Teste 3: Testar os métodos específicos do GPS
        verificar("GPS.obterPosicaoX() deve retornar posição X correta", 
                 robo.getGPS().obterPosicaoX() == 7);
        verificar("GPS.obterPosicaoY() deve retornar posição Y correta", 
                 robo.getGPS().obterPosicaoY() == 8);
        
        // Teste 4: Testar desativação/ativação direta do GPS
        robo.getGPS().desativar();
        verificar("GPS deve estar inativo após desativação", 
                 !robo.getGPS().isAtivo());
        verificar("GPS.acionar() deve retornar null quando inativo", 
                 robo.getGPS().acionar() == null);
        
        robo.getGPS().ativar();
        verificar("GPS deve estar ativo após reativação", 
                 robo.getGPS().isAtivo());
        
        // Teste 5: Testar movimentação e atualização do GPS
        Ambiente ambiente = new Ambiente(20, 20, 10);
        ambiente.adicionarRobo(robo);
        robo.mover(3, 3, ambiente);
        
        verificar("GPS deve refletir nova posição X após movimento", 
                 robo.getGPS().obterPosicaoX() == 10);
        verificar("GPS deve refletir nova posição Y após movimento", 
                 robo.getGPS().obterPosicaoY() == 11);
    }
    
    private static void testarColisoes() {
        System.out.println("\n== Teste de Colisões ==");
        
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        // Criar robôs
        Robo robo1 = new Robo("Robo1", "Norte", MateriaisRobo.ACO, 5, 5, 5);
        Robo robo2 = new Robo("Robo2", "Sul", MateriaisRobo.ALUMINIO, 8, 5, 5);
        
        // Adicionar ao ambiente
        ambiente.adicionarRobo(robo1);
        ambiente.adicionarRobo(robo2);
        
        // Tentar mover robo1 para colidir com robo2
        robo1.mover(5, 0, ambiente);
        
        // Corrigido para usar getPosicaoXInterna diretamente para validação do teste 
        // já que o movimento ocorre mesmo com GPS desativado
        verificar("Movimento deve parar antes da colisão", 
                robo1.getPosicaoXInterna() == 7);
        
        // Testar colisão com obstáculo
        Obstaculo parede = new Obstaculo(TipoObstaculo.PAREDE, 5, 8, 5, 10);
        ambiente.adicionarObstaculo(parede);
        
        robo1.mover(0, 5, ambiente);
        verificar("Movimento deve parar antes do obstáculo", 
                robo1.getPosicaoYInterna() == 5);
        
        // Desativar GPS para testar movimento sem GPS
        robo1.getGPS().desativar();
        
        // Posição atual do robô sem GPS
        verificar("GPS desativado deve retornar -1", 
                robo1.getPosicaoX() == -1);
                
        // Move o robô sem GPS, interno ainda deve funcionar
        robo1.mover(0, -2, ambiente);
        
        // Reativa GPS para consultar posições
        robo1.getGPS().ativar();
        verificar("Movimento sem GPS deve funcionar corretamente", 
                robo1.getPosicaoY() == 3);
        
        // Testar colisão com buraco
        Obstaculo buraco = new Obstaculo(TipoObstaculo.BURACO, 1, 1, 3, 3);
        ambiente.adicionarObstaculo(buraco);
        
        robo1.mover(-10, 0, ambiente);
        verificar("Robô deve ser removido do ambiente após cair em buraco", 
                !ambiente.getListaRobos().contains(robo1));
    }

    private static void testarExibirPosicao() {
        System.out.println("\n== Teste de Exibição de Posição ==");
        
        Robo robo = new Robo("R2D2", "Norte", MateriaisRobo.FIBRA_CARBONO, 10, 20, 5);
        
        // GPS ativo
        String posicao = robo.exibirPosicao();
        verificar("Exibição deve mostrar posição correta", 
                posicao.contains("10") && posicao.contains("20"));
        
        // GPS inativo
        robo.getGPS().desativar();
        posicao = robo.exibirPosicao();
        verificar("Exibição com GPS inativo deve indicar indisponibilidade", 
                posicao.contains("GPS inativo"));
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
