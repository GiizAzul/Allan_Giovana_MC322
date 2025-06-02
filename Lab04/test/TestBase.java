import ambiente.Ambiente;
import robos.geral.Robo;
import ambiente.Obstaculo;
import interfaces.Entidade;

/**
 * Classe base para testes que fornece métodos utilitários
 * para verificação de condições e adição de entidades
 */
public class TestBase {
    
    /**
     * Verifica uma condição e exibe o resultado com formatação colorida
     * @param descricao Descrição do teste sendo executado
     * @param condicao Condição booleana a ser verificada (true = passou, false = falhou)
     */
    public static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            System.out.println("\033[1;32m✓ PASSOU:\033[0m " + descricao);
        } else {
            System.out.println("\033[1;31m✗ FALHOU:\033[0m " + descricao);
        }
    }

    /**
     * Tenta adicionar uma entidade genérica ao ambiente e verifica se ocorrem exceções
     * @param entidade Entidade a ser adicionada ao ambiente
     * @param ambiente Ambiente onde a entidade será adicionada
     */
    public static void adicionarEntidadeTest(Entidade entidade, Ambiente ambiente) {
        try {
            ambiente.adicionarEntidade(entidade);
        } catch (Exception e) {
            verificar("Não deve haver exceção ao adicionar entidade. Erro: " + e.getMessage(), false);
        }
    }

    /**
     * Tenta adicionar um robô ao ambiente e verifica se ocorrem exceções
     * Método específico para robôs que pode ter validações especiais
     * @param entidade Robô a ser adicionado ao ambiente
     * @param ambiente Ambiente onde o robô será adicionado
     */
    public static void adicionarEntidadeTest(Robo entidade, Ambiente ambiente) {
        try {
            ambiente.adicionarEntidade(entidade);
        } catch (Exception e) {
            verificar("Não deve haver exceção ao adicionar entidade/Robo. Erro: " + e.getMessage(), false);
        }
    }

    /**
     * Tenta adicionar um obstáculo ao ambiente e verifica se ocorrem exceções
     * Método específico para obstáculos que pode ter validações especiais
     * @param entidade Obstáculo a ser adicionado ao ambiente
     * @param ambiente Ambiente onde o obstáculo será adicionado
     */
    public static void adicionarEntidadeTest(Obstaculo entidade, Ambiente ambiente) {
        try {
            ambiente.adicionarEntidade(entidade);
        } catch (Exception e) {
            verificar("Não deve haver exceção ao adicionar entidade/Obstáculo. Erro: " + e.getMessage(), false);
        }
    }
}
