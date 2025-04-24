import java.util.ArrayList;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.Robo;
import robos.aereos.DroneAtaque;
import robos.aereos.DroneVigilancia;
import robos.terrestres.Correios;
import robos.terrestres.TanqueGuerra;

/**
 * Classe para testar as funcionalidades da classe Ambiente
 */
public class TestAmbiente {
    
    /**
     * Método principal que executa os testes
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Inicializa contadores para estatísticas de testes
        int testesExecutados = 0;
        int testesBemSucedidos = 0;
        
        System.out.println("Iniciando testes da classe Ambiente...");
        
        // 1. Teste de criação de ambiente
        System.out.println("\n=== Teste de criação de ambiente ===");
        Ambiente ambiente = new Ambiente(10, 10, 5);
        testesExecutados++;
        if (ambiente != null && ambiente.getTamX() == 10 && ambiente.getTamY() == 10 && ambiente.getTamZ() == 5) {
            System.out.println("✓ Ambiente criado com sucesso");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na criação do ambiente");
        }
        
        // 2. Teste de verificação de limites
        System.out.println("\n=== Teste de verificação de limites ===");
        testesExecutados++;
        if (ambiente.dentroDosLimites(5, 5) && !ambiente.dentroDosLimites(11, 5)) {
            System.out.println("✓ Verificação de limites 2D funcionando corretamente");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na verificação de limites 2D");
        }
        
        testesExecutados++;
        if (ambiente.dentroDosLimites(5, 5, 3) && !ambiente.dentroDosLimites(5, 5, 6)) {
            System.out.println("✓ Verificação de limites 3D funcionando corretamente");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na verificação de limites 3D");
        }
        
        // 3. Teste de criação de robôs
        System.out.println("\n=== Teste de criação de robôs ===");
        
        // 3.1 Criação de robô terrestre - TanqueGuerra
        testesExecutados++;
        Robo tanque = ambiente.criarRobo(1, 1, "T1", "Norte", 2, 3, 5, 100, 10);
        if (tanque != null && tanque instanceof TanqueGuerra) {
            System.out.println("✓ TanqueGuerra criado com sucesso");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na criação do TanqueGuerra");
        }
        
        // 3.2 Criação de robô terrestre - Correios
        testesExecutados++;
        Robo correio = ambiente.criarRobo(1, 2, "C1", "Sul", 4, 5, 3, 50, 25);
        if (correio != null && correio instanceof Correios) {
            System.out.println("✓ Correios criado com sucesso");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na criação do Correios");
        }
        
        // 3.3 Criação de robô aéreo - DroneAtaque
        testesExecutados++;
        Robo droneAtaque = ambiente.criarRobo(2, 1, "DA1", "Leste", 6, 7, 2, 4, 200, 5);
        if (droneAtaque != null && droneAtaque instanceof DroneAtaque) {
            System.out.println("✓ DroneAtaque criado com sucesso");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na criação do DroneAtaque");
        }
        
        // 3.4 Criação de robô aéreo - DroneVigilancia
        testesExecutados++;
        Robo droneVigilancia = ambiente.criarRobo(2, 2, "DV1", "Oeste", 8, 1, 3, 4, 10.5f, 180.0f);
        if (droneVigilancia != null && droneVigilancia instanceof DroneVigilancia) {
            System.out.println("✓ DroneVigilancia criado com sucesso");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na criação do DroneVigilancia");
        }
        
        // 3.5 Teste de criação de robô com parâmetros inválidos
        testesExecutados++;
        // Nesse caso robô criado está fora dos limites do ambiente
        // (10, 10) é o limite máximo do ambiente
        Robo roboInvalido = ambiente.criarRobo(1, 1, "X1", "Norte", 20, 20, 5, 100, 10);
        if (roboInvalido == null) {
            System.out.println("✓ Bloqueou criação de robô fora dos limites");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha ao bloquear robô fora dos limites");
        }
        
        // 4. Teste de adição de robôs ao ambiente
        System.out.println("\n=== Teste de adição de robôs ao ambiente ===");
        ambiente.adicionarRobo(tanque);
        ambiente.adicionarRobo(correio);
        ambiente.adicionarRobo(droneAtaque);
        ambiente.adicionarRobo(droneVigilancia);
        
        testesExecutados++;
        ArrayList<Robo> robosNoAmbiente = ambiente.getListaRobos();
        if (robosNoAmbiente.size() == 4) {
            System.out.println("✓ Robôs adicionados ao ambiente com sucesso");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na adição de robôs ao ambiente");
        }
        
        // 5. Teste de identificação de objetos por posição
        System.out.println("\n=== Teste de identificação de objetos por posição ===");
        
        // 5.1 Identificação 2D
        testesExecutados++;
        Object objetoEncontrado = ambiente.identificarObjetoPosicao(2, 3);
        if (objetoEncontrado != null && objetoEncontrado.equals(tanque)) {
            System.out.println("✓ Identificação 2D funcionando corretamente");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na identificação 2D");
        }
        
        // 5.2 Identificação 3D
        testesExecutados++;
        Object objetoAereo = ambiente.identificarObjetoPosicao(6, 7, 2);
        if (objetoAereo != null && objetoAereo.equals(droneAtaque)) {
            System.out.println("✓ Identificação 3D funcionando corretamente");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na identificação 3D");
        }
        
        // 5.3 Identificação de objeto inexistente
        testesExecutados++;
        Object objetoInexistente = ambiente.identificarObjetoPosicao(9, 9);
        if (objetoInexistente == null) {
            System.out.println("✓ Identificação de objeto inexistente funcionando corretamente");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na identificação de objeto inexistente");
        }
        

        // 6. Teste de adição de obstáculos
        System.out.println("\n=== Teste de adição de obstáculos ===");
        
        // Criação de obstáculos usando a construção correta
        Obstaculo obstaculo1 = new Obstaculo(TipoObstaculo.PAREDE, 1, 1, 1, 2);
        Obstaculo obstaculo2 = new Obstaculo(TipoObstaculo.ARVORE, 5, 5, 6, 6);
        
        ambiente.adicionarObstaculo(obstaculo1);
        ambiente.adicionarObstaculo(obstaculo2);
        
        testesExecutados++;
        ArrayList<Obstaculo> obstaculosNoAmbiente = ambiente.getListaObstaculos();
        if (obstaculosNoAmbiente.size() == 2) {
            System.out.println("✓ Obstáculos adicionados ao ambiente com sucesso");
            testesBemSucedidos++;
        } else {
            System.out.println("✗ Falha na adição de obstáculos ao ambiente");
        }
        
        // Resumo dos testes
        System.out.println("\n=== Resumo dos testes ===");
        System.out.println("Total de testes executados: " + testesExecutados);
        System.out.println("Testes bem-sucedidos: " + testesBemSucedidos);
        System.out.println("Testes falhos: " + (testesExecutados - testesBemSucedidos));
        
        if (testesBemSucedidos == testesExecutados) {
            System.out.println("\nTodos os testes passaram! A classe Ambiente está funcionando corretamente.");
        } else {
            System.out.println("\nAlguns testes falharam. Verifique a implementação da classe Ambiente.");
        }
    }
}