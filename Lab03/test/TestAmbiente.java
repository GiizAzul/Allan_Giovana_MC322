import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.Robo;
import robos.aereos.DroneAtaque;
import robos.aereos.DroneVigilancia;
import robos.terrestres.Correios;
import robos.terrestres.TanqueGuerra;

public class TestAmbiente {
    
    public static void main(String[] args) {
        executarTestes();
    }
    
    public static void executarTestes() {
        System.out.println("Iniciando testes da classe Ambiente...");
        
        testarCriacaoAmbiente();
        testarVerificacaoLimites();
        testarCriacaoRobos();
        testarAdicaoRobos();
        testarIdentificacaoObjetosPosicao();
        testarAdicaoObstaculos();
        
        System.out.println("\nTodos os testes foram concluídos!");
    }
    
    private static void testarCriacaoAmbiente() {
        System.out.println("\n== Teste de Criação de Ambiente ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        verificar("Ambiente deve ser criado com sucesso", 
                 ambiente != null);
        verificar("Dimensão X deve ser 10", 
                 ambiente.getTamX() == 10);
        verificar("Dimensão Y deve ser 10", 
                 ambiente.getTamY() == 10);
        verificar("Dimensão Z deve ser 5", 
                 ambiente.getTamZ() == 5);
    }
    
    private static void testarVerificacaoLimites() {
        System.out.println("\n== Teste de Verificação de Limites ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        
        // Teste de limites 2D
        verificar("Posição (5,5) deve estar dentro dos limites 2D", 
                 ambiente.dentroDosLimites(5, 5));
        verificar("Posição (11,5) deve estar fora dos limites 2D", 
                 !ambiente.dentroDosLimites(11, 5));
        verificar("Posição (5,11) deve estar fora dos limites 2D", 
                 !ambiente.dentroDosLimites(5, 11));
        verificar("Posição (0,0) deve estar dentro dos limites 2D", 
                 ambiente.dentroDosLimites(0, 0));
        verificar("Posição (-1,5) deve estar fora dos limites 2D", 
                 !ambiente.dentroDosLimites(-1, 5));
        
        // Teste de limites 3D
        verificar("Posição (5,5,3) deve estar dentro dos limites 3D", 
                 ambiente.dentroDosLimites(5, 5, 3));
        verificar("Posição (5,5,6) deve estar fora dos limites 3D", 
                 !ambiente.dentroDosLimites(5, 5, 6));
        verificar("Posição (5,5,0) deve estar dentro dos limites 3D", 
                 ambiente.dentroDosLimites(5, 5, 0));
        verificar("Posição (5,5,-1) deve estar fora dos limites 3D", 
                 !ambiente.dentroDosLimites(5, 5, -1));
    }
    
    private static void testarCriacaoRobos() {
        System.out.println("\n== Teste de Criação de Robôs ==");
        
        Ambiente ambiente = new Ambiente(20, 20, 10);
        
        // Teste de robô terrestre - TanqueGuerra
        Robo tanque = ambiente.criarRobo(1, 1, "T1", "Norte", 2, 3, 5, 100, 10);
        verificar("TanqueGuerra deve ser criado com sucesso", 
                 tanque != null && tanque instanceof TanqueGuerra);
        if (tanque != null) {
            verificar("TanqueGuerra deve ter o nome T1", 
                     tanque.getNome().equals("T1"));
        }
        
        // Teste de robô terrestre - Correios
        Robo correio = ambiente.criarRobo(1, 2, "C1", "Sul", 4, 5, 3, 50, 25);
        verificar("Correios deve ser criado com sucesso", 
                 correio != null && correio instanceof Correios);
        if (correio != null) {
            verificar("Correios deve ter o nome C1", 
                     correio.getNome().equals("C1"));
        }
        
        // Teste de robô aéreo - DroneAtaque
        Robo droneAtaque = ambiente.criarRobo(2, 1, "DA1", "Leste", 6, 7, 2, 4, 200, 5);
        verificar("DroneAtaque deve ser criado com sucesso", 
                 droneAtaque != null && droneAtaque instanceof DroneAtaque);
        if (droneAtaque != null) {
            verificar("DroneAtaque deve ter o nome DA1", 
                     droneAtaque.getNome().equals("DA1"));
        }
        
        // Teste de robô aéreo - DroneVigilancia
        Robo droneVigilancia = ambiente.criarRobo(2, 2, "DV1", "Oeste", 8, 1, 3, 4, 10.5f, 180.0f);
        verificar("DroneVigilancia deve ser criado com sucesso", 
                 droneVigilancia != null && droneVigilancia instanceof DroneVigilancia);
        if (droneVigilancia != null) {
            verificar("DroneVigilancia deve ter o nome DV1", 
                     droneVigilancia.getNome().equals("DV1"));
        }
        
        // Teste de criação de robô com parâmetros inválidos
        Robo roboInvalido = ambiente.criarRobo(1, 1, "X1", "Norte", 25, 25, 5, 100, 10);
        verificar("Deve retornar null para robô fora dos limites", 
                 roboInvalido == null);
        
        // Teste de tipo inválido
        Robo tipoInvalido = ambiente.criarRobo(3, 1, "I1", "Norte", 5, 5, 5, 100, 10);
        verificar("Deve retornar null para tipo de robô inválido", 
                 tipoInvalido == null);
    }
    
    private static void testarAdicaoRobos() {
        System.out.println("\n== Teste de Adição de Robôs ao Ambiente ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        
        Robo robo1 = new Robo("R1", "Norte", 1, 1);
        Robo robo2 = new Robo("R2", "Sul", 2, 2);
        Robo robo3 = new Robo("R3", "Leste", 3, 3);
        
        // Adicionar robôs
        ambiente.adicionarRobo(robo1);
        ambiente.adicionarRobo(robo2);
        ambiente.adicionarRobo(robo3);
        
        ArrayList<Robo> robos = ambiente.getListaRobos();
        verificar("Ambiente deve conter 3 robôs", 
                 robos.size() == 3);
        verificar("Ambiente deve conter o robô R1", 
                 robos.contains(robo1));
        verificar("Ambiente deve conter o robô R2", 
                 robos.contains(robo2));
        verificar("Ambiente deve conter o robô R3", 
                 robos.contains(robo3));
        
        // Teste de adição de robô fora dos limites
        Robo roboFora = new Robo("Fora", "Oeste", 15, 15);
        ambiente.adicionarRobo(roboFora);
        verificar("Não deve adicionar robô fora dos limites", 
                 !ambiente.getListaRobos().contains(roboFora));
    }
    
    private static void testarIdentificacaoObjetosPosicao() {
        System.out.println("\n== Teste de Identificação de Objetos por Posição ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        
        // Adicionar robôs em posições conhecidas
        Robo roboTerrestre = new Robo("RT", "Norte", 2, 3);
        ambiente.adicionarRobo(roboTerrestre);
        
        DroneAtaque roboAereo = new DroneAtaque("RA", "Leste", 6, 7, 2, 4, 200, 5);
        ambiente.adicionarRobo(roboAereo);
        
        // Teste de identificação 2D
        Object objetoEncontrado2D = ambiente.identificarObjetoPosicao(2, 3);
        verificar("Deve encontrar robô terrestre na posição (2,3)", 
                 objetoEncontrado2D != null && objetoEncontrado2D.equals(roboTerrestre));
        
        // Teste de identificação 3D
        Object objetoEncontrado3D = ambiente.identificarObjetoPosicao(6, 7, 2);
        verificar("Deve encontrar robô aéreo na posição (6,7,2)", 
                 objetoEncontrado3D != null && objetoEncontrado3D.equals(roboAereo));
        
        // Teste de identificação de posição vazia
        Object objetoVazio = ambiente.identificarObjetoPosicao(9, 9);
        verificar("Deve retornar null para posição vazia", 
                 objetoVazio == null);
        
        Object objetoVazio3D = ambiente.identificarObjetoPosicao(6, 7, 3);
        verificar("Deve retornar null para posição 3D vazia", 
                 objetoVazio3D == null);
    }
    
    private static void testarAdicaoObstaculos() {
        System.out.println("\n== Teste de Adição de Obstáculos ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        
        // Criar obstáculos
        Obstaculo parede = new Obstaculo(TipoObstaculo.PAREDE, 1, 2, 1, 2);
        Obstaculo arvore = new Obstaculo(TipoObstaculo.ARVORE, 5, 6, 5, 6);
        
        // Adicionar obstáculos
        ambiente.adicionarObstaculo(parede);
        ambiente.adicionarObstaculo(arvore);
        
        ArrayList<Obstaculo> obstaculos = ambiente.getListaObstaculos();
        verificar("Ambiente deve conter 2 obstáculos", 
                 obstaculos.size() == 2);
        verificar("Ambiente deve conter a parede", 
                 obstaculos.contains(parede));
        verificar("Ambiente deve conter a árvore", 
                 obstaculos.contains(arvore));
        
        // Teste de obstaculo fora dos limites
        Obstaculo obstaculoFora = new Obstaculo(TipoObstaculo.PAREDE, 15, 16, 15, 16);
        ambiente.adicionarObstaculo(obstaculoFora);
        verificar("Não deve adicionar obstáculo fora dos limites", 
                 !ambiente.getListaObstaculos().contains(obstaculoFora));
        
        // Verificar colisão com obstáculo
        Robo robo = new Robo("R1", "Norte", 1, 1);
        ambiente.adicionarRobo(robo);
        
        // Uso do método identificarObjetoPosicao para verificar colisão
        Object colisao = ambiente.identificarObjetoPosicao(1, 1);
        verificar("Deve detectar robô na posição", colisao != null);
        
        // Verifica se tem algo na posição do obstáculo
        Object objetoNaPosicao = ambiente.identificarObjetoPosicao(1, 1);
        verificar("Deve encontrar objeto na posição do robô", 
                 objetoNaPosicao != null);
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