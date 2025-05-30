import ambiente.Ambiente;
import interfaces.Entidade;

public class TestBase {
    
    public static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            System.out.println("\033[1;32m✓ PASSOU:\033[0m " + descricao);
        } else {
            System.out.println("\033[1;31m✗ FALHOU:\033[0m " + descricao);
        }
    }

    public static void adicionarEntidadeTestTest(Entidade entidade, Ambiente ambiente) {
        try {
            ambiente.adicionarEntidade(entidade);
        } catch (Exception e) {
            verificar("Não deve haver exceção ao adicionar entidade. Erro: " + e.getMessage(), false);
        }
    }
}
