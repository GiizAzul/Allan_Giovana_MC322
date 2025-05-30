import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.aereos.DroneAtaque;
import robos.aereos.DroneVigilancia;
import robos.geral.Robo;
import robos.geral.MateriaisRobo;
import robos.terrestres.Correios;
import robos.terrestres.TanqueGuerra;
import interfaces.Entidade;

public class TestAmbiente {
    
    public static void main(String[] args) {
        executarTestes();
    }
    
    public static void executarTestes() {
        System.out.println("Iniciando testes da classe Ambiente...");
        
        testarCriacaoAmbiente();
        testarVerificacaoLimites();
        testarCriacaoERobos();
        testarAdicaoEntidades();
        testarVerificacaoOcupacao();
        testarVisualizacaoAmbiente();
        
        System.out.println("\nTodos os testes foram concluídos!");
    }
    
    private static void testarCriacaoAmbiente() {
        System.out.println("\n== Teste de Criação de Ambiente ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        verificar("Ambiente deve ser criado com sucesso", ambiente != null);
        verificar("Dimensão X deve ser 10", ambiente.getTamX() == 10);
        verificar("Dimensão Y deve ser 10", ambiente.getTamY() == 10);
        verificar("Dimensão Z deve ser 5", ambiente.getTamZ() == 5);
    }
    
    private static void testarVerificacaoLimites() {
        System.out.println("\n== Teste de Verificação de Limites ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        
        verificar("Posição (5,5) deve estar dentro dos limites", ambiente.dentroDosLimites(5, 5));
        verificar("Posição (11,5) deve estar fora dos limites", !ambiente.dentroDosLimites(11, 5));
        verificar("Posição (-1,5) deve estar fora dos limites", !ambiente.dentroDosLimites(-1, 5));
        verificar("Posição (5,5,3) deve estar dentro dos limites", ambiente.dentroDosLimites(5, 5, 3));
        verificar("Posição (5,5,6) deve estar fora dos limites", !ambiente.dentroDosLimites(5, 5, 6));
    }
    
    private static void testarCriacaoERobos() {
        System.out.println("\n== Teste de Criação de Robôs ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        
        Robo tanque = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        verificar("TanqueGuerra deve ser criado com sucesso", tanque != null && tanque instanceof TanqueGuerra);
        
        Robo correio = ambiente.criarRobo(1, 2, "C1", "Sul", MateriaisRobo.PLASTICO, 4, 5, 0, 3, 5, 50, 25.0f);
        verificar("Correios deve ser criado com sucesso", correio != null && correio instanceof Correios);
        
        Robo droneAtaque = ambiente.criarRobo(2, 1, "DA1", "Leste", MateriaisRobo.FIBRA_CARBONO, 6, 7, 2, 2, 4, 5, 200, 10);
        verificar("DroneAtaque deve ser criado com sucesso", droneAtaque != null && droneAtaque instanceof DroneAtaque);
        
        Robo droneVigilancia = ambiente.criarRobo(2, 2, "DV1", "Oeste", MateriaisRobo.PLASTICO, 8, 1, 3, 5, 8, 5, 60.0f, 90.0f, 45.0f);
        verificar("DroneVigilancia deve ser criado com sucesso", droneVigilancia != null && droneVigilancia instanceof DroneVigilancia);
    }
    
    private static void testarAdicaoEntidades() {
        System.out.println("\n== Teste de Adição de Entidades ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        
        Robo tanque = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        Obstaculo parede = new Obstaculo(TipoObstaculo.PAREDE, 1, 2, 1, 2);
        
        try {
            ambiente.adicionarEntidade(tanque);
            ambiente.adicionarEntidade(parede);
            
            ArrayList<Entidade> entidades = ambiente.getEntidades();
            verificar("Ambiente deve conter 2 entidades", entidades.size() == 2);
            verificar("Ambiente deve conter o tanque", entidades.contains(tanque));
            verificar("Ambiente deve conter a parede", entidades.contains(parede));
        } catch (Exception e) {
            verificar("Não deve lançar exceção ao adicionar entidades. Erro:" + e.getMessage(), false);
        }
    }
    
    private static void testarVerificacaoOcupacao() {
        System.out.println("\n== Teste de Verificação de Ocupação ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        
        Robo tanque = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        try {
            ambiente.adicionarEntidade(tanque);
            
            verificar("Posição (2,3) deve estar ocupada", ambiente.estaOcupado(2, 3, 0));
            verificar("Posição (5,5) não deve estar ocupada", !ambiente.estaOcupado(5, 5, 0));
        } catch (Exception e) {
            verificar("Não deve lançar exceção ao adicionar entidades. Erro:" + e.getMessage(), false);
        }
    }
    
    private static void testarVisualizacaoAmbiente() {
        System.out.println("\n== Teste de Visualização do Ambiente ==");
        
        Ambiente ambiente = new Ambiente(10, 10, 5);
        
        Robo tanque = ambiente.criarRobo(1, 1, "T1", "Norte", MateriaisRobo.ACO, 2, 3, 0, 2, 5, 100, 10);
        Obstaculo parede = new Obstaculo(TipoObstaculo.PAREDE, 1, 2, 1, 2);
        
        try {
            ambiente.adicionarEntidade(tanque);
            ambiente.adicionarEntidade(parede);
            
            String[][] visualizacao = ambiente.visualizarAmbiente();
            verificar("Visualização deve ser criada", visualizacao != null);
            verificar("Visualização deve ter dimensão X correta", visualizacao.length == ambiente.getTamX());
            verificar("Visualização deve ter dimensão Y correta", visualizacao[0].length == ambiente.getTamY());
        }  catch (Exception e) {
            verificar("Não deve lançar exceção ao adicionar entidades. Erro:" + e.getMessage(), false);
        }
    }
    
    private static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            System.out.println("\033[1;32m✓ PASSOU:\033[0m " + descricao);
        } else {
            System.out.println("\033[1;31m✗ FALHOU:\033[0m " + descricao);
        }
    }
}