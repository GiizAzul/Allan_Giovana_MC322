import java.util.ArrayList;
import java.util.Scanner;

import ambiente.Ambiente;
import ambiente.CentralComunicacao;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.aereos.DroneAtaque;
import robos.aereos.DroneVigilancia;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.terrestres.Correios;
import robos.terrestres.TanqueGuerra;
import interfaces.*;
import excecoes.ambiente.ErroComunicacaoException;
import excecoes.ambiente.ForaDosLimitesException;
import excecoes.logger.ArquivoInvalidoException;
import excecoes.robos.gerais.RoboDesligadoException;

/**
 * Classe principal que implementa o simulador de robôs interativo
 * Oferece interface de linha de comando para controlar robôs e ambiente
 */
public class Main {

    /**
     * Método principal que inicia o simulador de robôs
     * Oferece dois modos: padrão (com ambiente pré-configurado) e criação livre
     * @param args Argumentos da linha de comando (não utilizados)
     * @throws ErroComunicacaoException Se houver problemas na comunicação entre robôs
     * @throws RoboDesligadoException Se tentar operar robô desligado
     */
    public static void main(String[] args) throws ErroComunicacaoException, RoboDesligadoException, ArquivoInvalidoException {
        Scanner scanner = new Scanner(System.in);
        CentralComunicacao central = new CentralComunicacao();
        System.out.println("Bem vindo ao Simulador de Robôs!");
        System.out.println(
                "Gostaria de utilizar nosso modo padrão com ambiente e entidades padrão ou o modo de criação livre?\n1 - Modo Padrão\n2 - Modo Criação Livre");
        int modo = scanner.nextInt();
        limparTerminal();
        if (modo == 1) {
            Ambiente ambiente = new Ambiente(10, 10, 10);
            try {
                ambiente.adicionarEntidade(
                        ambiente.criarRobo(1, 1, "Marquinhos Tanque", "Norte", MateriaisRobo.ACO, 3, 3, 0, 2, 5, 100, 10));
                ambiente.adicionarEntidade(ambiente.criarRobo(1, 2, "Robertinho Correio", "Sul", MateriaisRobo.PLASTICO, 4,
                        9, 0, 1, 3, 50, 25.0f));
                ambiente.adicionarEntidade(ambiente.criarRobo(2, 1, "Pedrinho Drone Ataque", "Leste",
                        MateriaisRobo.FIBRA_CARBONO, 6, 8, 1, 2, 4, 8, 200, 5));
                ambiente.adicionarEntidade(ambiente.criarRobo(2, 2, "Joãozinho Drone Vigilância", "Oeste",
                        MateriaisRobo.PLASTICO, 8, 1, 3, 5, 8, 9, 50.0f, 60.0f, 90.0f));
            } catch (ForaDosLimitesException e) {
                System.out.println("Erro ao adicionar robôs: " + e.getMessage());
            }
            try {
                ambiente.adicionarEntidade(new Obstaculo(TipoObstaculo.BURACO, 1, 2, 1, 2));
                ambiente.adicionarEntidade(new Obstaculo(TipoObstaculo.ARVORE, 5, 6, 6, 7));
                ambiente.adicionarEntidade(new Obstaculo(TipoObstaculo.PAREDE, 4, 6, 4, 5));
            } catch (ForaDosLimitesException e) {
                System.out.println("Erro ao adicionar obstáculos: " + e.getMessage());
            }

            System.out.println(
                    "Atualmente o seu ambiente tem largura, altura e profundidade 10\nExistem 4 robôs no seu ambiente, um Correio, um Tanque de Guerra, um Drone de Ataque e um Drone de Vigilância\nExistem 3 obstáculos no seu ambiente, uma parede, uma árvore e um buraco");

            ArrayList<Entidade> listaEntidades = null;
            ArrayList<Robo> listaRobo = new ArrayList<Robo>();

            menu(scanner, ambiente, central, listaEntidades, listaRobo, 4, false);

        } else if (modo == 2) {
            System.out.println("Crie um ambiente 3D com as dimensões:");
            int tamX, tamY, tamZ;
            do {
                System.out.print("X: ");
                tamX = scanner.nextInt();
                System.out.print("Y: ");
                tamY = scanner.nextInt();
                System.out.print("Z: ");
                tamZ = scanner.nextInt();
            } while (tamX <= 0 || tamY <= 0 || tamZ <= 0);

            Ambiente ambiente = new Ambiente(tamX, tamY, tamZ);
            String mensagem = String.format("Ambiente criado com:\nTamX: %d\nTamY: %d\nTamZ: %d\n",
                    tamX, tamY, tamZ);
            System.out.println(mensagem);

            Object[] atributos = new Object[14];
            ArrayList<Entidade> listaEntidades = null;
            ArrayList<Robo> listaRobo = new ArrayList<Robo>();

            menu(scanner, ambiente, central, listaEntidades, listaRobo, 7, true, atributos);

        } else {
            System.err.println("Opção inválida! Por favor, escolha 1 ou 2.");
        }
        scanner.close();
    }

    /**
     * Limpa o terminal usando códigos ANSI de escape
     * Funciona em terminais que suportam ANSI (Linux, macOS, Windows moderno)
     */
    public static void limparTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Obtém coordenadas válidas do usuário dentro dos limites do ambiente
     * Continua solicitando até que coordenadas válidas sejam fornecidas
     * @param ambiente Ambiente para verificar limites das coordenadas
     * @param scanner Scanner para leitura da entrada do usuário
     * @return Array de inteiros com coordenadas [X, Y] válidas
     */
    public static int[] obterPosicao(Ambiente ambiente, Scanner scanner) {
        System.out.print("Posição X: ");
        int destinoX = scanner.nextInt();
        System.out.print("Posição Y: ");
        int destinoY = scanner.nextInt();
        while (!ambiente.dentroDosLimites(destinoX, destinoY)) {
            System.out.println("Coordenadas fora dos limites, tente novamente:");
            System.out.print("Posição X: ");
            destinoX = scanner.nextInt();
            System.out.print("Posição Y: ");
            destinoY = scanner.nextInt();
        }
        System.out.println();
        return new int[] { destinoX, destinoY };
    }

    /**
     * Gerencia as ações específicas que podem ser executadas por cada tipo de robô
     * Apresenta menus contextuais baseados no tipo de robô selecionado e seu estado
     * @param scanner Scanner para leitura da entrada do usuário
     * @param listaRobo Lista de robôs disponíveis no ambiente
     * @param ambiente Ambiente onde os robôs operam
     * @param central Central de comunicação para troca de mensagens
     * @throws ErroComunicacaoException Se houver problemas na comunicação
     * @throws RoboDesligadoException Se tentar operar robô desligado
     */
    public static void acoesRobo(Scanner scanner, ArrayList<Robo> listaRobo, Ambiente ambiente,
            CentralComunicacao central) throws ErroComunicacaoException, RoboDesligadoException {
        String menu;
        vizualizarMapa(ambiente);
        System.out.println("Escolha o Robô:\n");
        int i = 1;
        for (Robo robo : listaRobo) {
            System.out.printf("%d - %s%n", i++, robo.getNome());
        }
        int index = 0;
        int sizeLista = listaRobo.size();
        do {
            System.out.print("Robô: ");
            index = scanner.nextInt();
        } while (index <= 0 || index > sizeLista);
        scanner.nextLine(); // Consumir quebra de linha
        System.out.println();

        Robo roboSelecionado = listaRobo.get(index - 1);
        System.out.println(roboSelecionado.getDescricao());
        if (roboSelecionado instanceof TanqueGuerra) {
            TanqueGuerra roboEscolhido = (TanqueGuerra) roboSelecionado;
            menu = String.format(
                    "Digite o número da ação desejada:\n\n" +
                            "1 - Atirar\n" +
                            "2 - Recarregar\n" +
                            "3 - Mover\n" +
                            "4 - Mudar direção\n" +
                            "5 - Alterar Estado\n");
            System.out.print(menu);
            System.out.print("Ação: ");
            int acao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            System.out.println();

            if (roboEscolhido.getEstado()) {
                // Verificar operabilidade do robô antes de proceder
                if (acao == 1) {
                    System.out.print("Digite as coordenadas X e Y do alvo: ");
                    int alvoX = scanner.nextInt();
                    int alvoY = scanner.nextInt();
                    System.out.print("Digite o número de tiros: ");
                    int nTiros = scanner.nextInt();
                    limparTerminal();
                    try {
                        System.out.println(
                            roboEscolhido.executarTarefa("atirar", alvoX, alvoY, nTiros, ambiente));
                    } catch (Exception e) {
                        System.out.println("Não foi possível realizar o disparo, erro: " + e.getMessage());
                    }

                } else if (acao == 2) {
                    // Recarregar balas do Tanque
                    System.out.print("Digite o número de balas recarregadas:");
                    int nBalas = scanner.nextInt();
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("recarregar", nBalas));
                    } catch (Exception e) {
                        System.out.println("Não foi possível fazer a recarga, erro: " + e.getMessage());
                    }
                } else if (acao == 3) {
                    // Movimentação do Robô por DeltaX e DeltaY
                    System.out.print("Digite as coordenadas X e Y do destino:\n");
                    int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                    System.out.print("Digite a velocidade do movimento: ");
                    int vel = scanner.nextInt();
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("mover", coordenadas[0] - roboSelecionado.getX(),
                                coordenadas[1] - roboSelecionado.getY(), vel, ambiente));
                    } catch (Exception e) {
                        System.out.println("Não foi possível mover o robô, erro: " + e.getMessage());
                    }
                } else if (acao == 4) {
                    System.out.print("Digite a nova direção (Norte/Sul/Leste/Oeste): ");
                    String direcao;
                    do {
                        direcao = scanner.nextLine();
                    } while (!Robo.getDirecoesPossiveis().contains(direcao));
                    limparTerminal();
                    try {
                        roboEscolhido.executarTarefa("direção", direcao);
                    } catch (Exception e) {
                        System.out.println("Não foi possível mudar a direção, erro: " + e.getMessage());
                    }

                } else if (acao == 5) {
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("desligar"));
                    } catch (Exception e) {
                        System.out.println("Não foi possível desligar o robô, erro: " + e.getMessage());
                    }
                }
            } else if (acao == 5) {
                limparTerminal();
                try {
                    System.out.println(roboEscolhido.executarTarefa("ligar"));
                } catch (Exception e) {
                    System.out.println("Não foi possível ligar o robô, erro: " + e.getMessage());
                }
            } else {
                limparTerminal();
                System.out.println("O robô está desligado\n");
            }

        } else if (roboSelecionado instanceof Correios) {
            Correios roboEscolhido = (Correios) roboSelecionado;
            menu = String.format(
                    "Digite o número da ação desejada:\n\n" +
                            "1 - Carregar pacote\n" +
                            "2 - Entregar pacote\n" +
                            "3 - Listar entregas\n" +
                            "4 - Mover\n" +
                            "5 - Mudar direção\n" +
                            "6 - Mandar mensagem\n" +
                            "7 - Alterar Estado\n");
            System.out.print(menu);
            System.out.print("Ação: ");
            int acao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            System.out.println();

            if (roboEscolhido.getEstado()) {
                if (acao == 1) {
                    System.out.print("Digite o nome ou ID do pacote: ");
                    String id = scanner.nextLine();
                    System.out.print("Digite o peso do pacote: ");
                    float peso = scanner.nextFloat();
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("carregar", id, peso));
                    } catch (Exception e) {
                        System.out.println("Não foi possível carregar o pacote, erro: " + e.getMessage());
                    }

                } else if (acao == 2) {
                    System.out.print("Digite o nome ou ID do pacote: ");
                    String id = scanner.nextLine();
                    System.out.print("Digite as coordenadas X e Y do destino: ");
                    int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                    limparTerminal();
                    try {
                        System.out.println(
                                roboEscolhido.executarTarefa("entregar", id, coordenadas[0], coordenadas[1],
                                        ambiente));
                    } catch (Exception e) {
                        System.out.println("Não foi possível entregar o pacote, erro: " + e.getMessage());
                    }
                } else if (acao == 3) {
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("listar"));
                    } catch (Exception e) {
                        System.out.println("Não foi possível listar as entregas, erro: " + e.getMessage());
                    }
                } else if (acao == 4) {
                    System.out.print("Digite as coordenadas X e Y do destino: ");
                    int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                    System.out.print("Digite a velocidade do movimento: ");
                    int vel = scanner.nextInt();
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("mover", coordenadas[0] - roboSelecionado.getX(),
                                coordenadas[1] - roboSelecionado.getY(), vel, ambiente));
                    } catch (Exception e) {
                        System.out.println("Não foi possível mover o robô, erro: " + e.getMessage());
                    }
                } else if (acao == 5) {
                    System.out.print("Digite a nova direção (Norte/Sul/Leste/Oeste): ");
                    String direcao;
                    do {
                        direcao = scanner.nextLine();
                    } while (!Robo.getDirecoesPossiveis().contains(direcao));
                    limparTerminal();
                    try {
                        roboEscolhido.executarTarefa("direção", direcao);
                    } catch (Exception e) {
                        System.out.println("Não foi possível mudar a direção, erro: " + e.getMessage());
                    }
                } else if (acao == 6) {
                    System.out.println("Escolha o Robô destinatário:\n");
                    i = 1;
                    for (Robo robo : listaRobo) {
                        System.out.printf("%d - %s%n", i++, robo.getNome());
                    }
                    index = 0;
                    do {
                        System.out.print("Robô: ");
                        index = scanner.nextInt();
                    } while (index <= 0 || index > sizeLista);
                    scanner.nextLine(); // Consumir quebra de linha
                    System.out.println();
                    Robo destinatario = listaRobo.get(index - 1);
                    // TO-DO: Alterar essa lógica de erro!!
                    if (!(destinatario instanceof Comunicavel)) {
                        throw new ErroComunicacaoException("O Robô destinatário deve ser comunicável");
                    }

                    System.out.println("Digite a mensagem a ser enviada:\n");
                    String mensagem = scanner.nextLine();
                    limparTerminal();
                    System.out.println(roboEscolhido.enviarMensagem((Comunicavel) destinatario, mensagem, central));
                } else if (acao == 7) {
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("desligar"));
                    } catch (Exception e) {
                        System.out.println("Não foi possível desligar o robô, erro: " + e.getMessage());
                    }
                }
            } else if (acao == 7) {
                limparTerminal();
                try {
                    System.out.println(roboEscolhido.executarTarefa("ligar"));
                } catch (Exception e) {
                    System.out.println("Não foi possível ligar o robô, erro: " + e.getMessage());
                }
            } else {
                limparTerminal();
                System.out.println("O robô está desligado\n");
            }

        } else if (roboSelecionado instanceof DroneAtaque) {
            DroneAtaque roboEscolhido = (DroneAtaque) roboSelecionado;
            menu = String.format(
                    "Digite o número da ação desejada:\n\n" +
                            "1 - Atirar\n" +
                            "2 - Recarregar\n" +
                            "3 - Mover\n" +
                            "4 - Identificar obstáculos\n" +
                            "5 - Mudar direção\n" +
                            "6 - Alterar Estado\n");
            System.out.print(menu);
            System.out.print("Ação: ");
            int acao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            System.out.println();

            if (roboEscolhido.getEstado()) {
                if (acao == 1) {
                    System.out.print("Digite as coordenadas X, Y e Z do alvo: ");
                    int alvoX = scanner.nextInt();
                    int alvoY = scanner.nextInt();
                    int alvoZ = scanner.nextInt();
                    System.out.print("Digite o número de tiros: ");
                    int nTiros = scanner.nextInt();
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("atirar coord", alvoX, alvoY, alvoZ,
                            nTiros, ambiente));
                    } catch (Exception e) {
                        System.out.println("Não foi possível realizar o disparo nas coordenadas especificadas, erro: " + e.getMessage());
                    }
                    
                } else if (acao == 2) {
                    System.out.print("Digite o número de balas recarregadas:");
                    int nBalas = scanner.nextInt();
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("recarregar", nBalas));
                    } catch (Exception e) {
                        System.out.println("Não foi possível fazer a recarga, erro: " + e.getMessage());
                    }
                } else if (acao == 3) {
                    System.out.print("Digite as coordenadas X e Y do destino: ");
                    int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                    System.out.print("Digite a altura do destino: ");
                    int nAlt = scanner.nextInt();
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("mover", coordenadas[0] - roboSelecionado.getX(),
                                coordenadas[1] - roboSelecionado.getY(), nAlt - roboSelecionado.getZ(),
                                ambiente));
                    } catch (Exception e) {
                        System.out.println("Não foi possível mover o robô, erro: " + e.getMessage());
                    }
                } else if (acao == 4) {
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("identificar"));
                    } catch (Exception e) {
                        System.out.println("Não foi possível identificar obstáculos, erro: " + e.getMessage());
                    }
                } else if (acao == 5) {
                    System.out.print("Digite a nova direção (Norte/Sul/Leste/Oeste): ");
                    String direcao;
                    do {
                        direcao = scanner.nextLine();
                    } while (!Robo.getDirecoesPossiveis().contains(direcao));
                    limparTerminal();
                    try {
                        roboEscolhido.executarTarefa("direção", direcao);
                    } catch (Exception e) {
                        System.out.println("Não foi possível mudar a direção, erro: " + e.getMessage());
                    }
                } else if (acao == 6) {
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("desligar"));
                    } catch (Exception e) {
                        System.out.println("Não foi possível desligar o robô, erro: " + e.getMessage());
                    }
                }
            } else if (acao == 6) {
                limparTerminal();
                try {
                    System.out.println(roboEscolhido.executarTarefa("ligar"));
                } catch (Exception e) {
                    System.out.println("Não foi possível ligar o robô, erro: " + e.getMessage());
                }
            } else {
                limparTerminal();
                System.out.println("Robô desligado!");
            }

        } else if (roboSelecionado instanceof DroneVigilancia) {
            DroneVigilancia roboEscolhido = (DroneVigilancia) roboSelecionado;
            menu = String.format(
                    "Digite o número da ação desejada:\n\n" +
                            "1 - Mover\n" +
                            "2 - Identificar obstáculos\n" +
                            "3 - Mudar direção\n" +
                            "4 - Varrer Área\n" +
                            "5 - %s Camuflagem\n" +
                            "6 - Enviar mensagem\n" +
                            "7 - Alterar Estado\n",
                    roboEscolhido.isCamuflado() ? "Desativar"
                            : "Ativar");
            System.out.print(menu);
            System.out.print("Ação: ");
            int acao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            System.out.println();

            if (roboEscolhido.getEstado()) {
                if (acao == 1) {
                    System.out.print("Digite as coordenadas X e Y do destino: ");
                    int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                    System.out.print("Digite a altura do destino: ");
                    int nAlt = scanner.nextInt();
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("mover", coordenadas[0] - roboSelecionado.getX(),
                                coordenadas[1] - roboSelecionado.getY(), nAlt - roboSelecionado.getZ(),
                                ambiente));
                    } catch (Exception e) {
                        System.out.println("Não foi possível mover o robô, erro: " + e.getMessage());
                    }
                } else if (acao == 2) {
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("identificar"));
                    } catch (Exception e) {
                        System.out.println("Não foi possível identificar obstáculos, erro: " + e.getMessage());
                    }
                } else if (acao == 3) {
                    System.out.print("Digite a nova direção (Norte/Sul/Leste/Oeste): ");
                    String direcao;
                    do {
                        direcao = scanner.nextLine();
                    } while (!Robo.getDirecoesPossiveis().contains(direcao));
                    limparTerminal();
                    try {
                        roboEscolhido.executarTarefa("direção", direcao);
                    } catch (Exception e) {
                        System.out.println("Não foi possível mudar a direção, erro: " + e.getMessage());
                    }
                } else if (acao == 4) {
                    System.out.println("Digite as coordenadas do centro de varredura:");
                    int[] centro = Main.obterPosicao(ambiente, scanner);
                    System.out.print("Digite o raio da varredura: ");
                    int raio = scanner.nextInt();
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("varrer", ambiente, centro[0],
                                centro[1], raio));
                    } catch (Exception e) {
                        System.out.println("Não foi possível realizar a varredura, erro: " + e.getMessage());
                    }
                } else if (acao == 5) {
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("camuflagem"));
                    } catch (Exception e) {
                        System.out.println("Não foi possível alterar camuflagem, erro: " + e.getMessage());
                    }
                } else if (acao == 6) {
                    System.out.println("Escolha o Robô destinatário:\n");
                    i = 1;
                    for (Robo robo : listaRobo) {
                        System.out.printf("%d - %s%n", i++, robo.getNome());
                    }
                    index = 0;
                    do {
                        System.out.print("Robô: ");
                        index = scanner.nextInt();
                    } while (index <= 0 || index > sizeLista);
                    scanner.nextLine(); // Consumir quebra de linha
                    System.out.println();
                    Robo destinatario = listaRobo.get(index - 1);
                    if (!(destinatario instanceof Comunicavel)) {
                        throw new ErroComunicacaoException("O Robô destinatário deve ser comunicável");
                    }

                    System.out.println("Digite a mensagem a ser enviada:\n");
                    String mensagem = scanner.nextLine();
                    limparTerminal();
                    System.out.println(roboEscolhido.enviarMensagem((Comunicavel) destinatario, mensagem, central));
                } else if (acao == 7) {
                    limparTerminal();
                    try {
                        System.out.println(roboEscolhido.executarTarefa("desligar"));
                    } catch (Exception e) {
                        System.out.println("Não foi possível desligar o robô, erro: " + e.getMessage());
                    }
                }
            } else if (acao == 7) {
                limparTerminal();
                try {
                    System.out.println(roboEscolhido.executarTarefa("ligar"));
                } catch (Exception e) {
                    System.out.println("Não foi possível ligar o robô, erro: " + e.getMessage());
                }
            } else {
                limparTerminal();
                System.out.println("Robô desligado!");
            }
        } else {
            System.out.println("Tipo de Robô inválido!");
        }
    }

    /**
     * Menu principal do simulador que coordena todas as funcionalidades
     * Gerencia navegação entre opções, atualização de listas e modo livre/padrão
     * @param scanner Scanner para leitura da entrada do usuário
     * @param ambiente Ambiente do simulador
     * @param central Central de comunicação
     * @param listaEntidades Lista de todas as entidades (pode ser null)
     * @param listaRobo Lista de robôs no ambiente
     * @param numTotal Número total de opções disponíveis no menu
     * @param livre Indica se está no modo de criação livre
     * @param atributos Array de atributos para criação de robôs (usado no modo livre)
     * @throws ErroComunicacaoException Se houver problemas na comunicação
     * @throws RoboDesligadoException Se tentar operar robô desligado
     */
    public static void menu(Scanner scanner, Ambiente ambiente, CentralComunicacao central,
            ArrayList<Entidade> listaEntidades, ArrayList<Robo> listaRobo, int numTotal, boolean livre,
            Object... atributos) throws ErroComunicacaoException, RoboDesligadoException {
        while (true) {
            vizualizarMapa(ambiente);
            String menu = String.format(
                    "Digite o número da ação que deseja realizar:\n\n" +
                            "0 - Fechar o simulador\n" +
                            "1 - Ações de um robô\n" +
                            "2 - Lista de robôs\n" +
                            "3 - Visualizar mapa\n" +
                            "4 - Histórico de Comunicação\n");
            if (livre) {
                menu += String.format(
                        "5 - Criar novo robô\n" +
                                "6 - Remover robô\n" +
                                "7 - Criar obstáculo\n");
            }
            System.out.print(menu);
            System.out.print("Comando: ");
            int comando;
            try {
                comando = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Entrada inválida! Por favor, digite um número.");
                scanner.nextLine(); // Consumir entrada inválida
                continue;
            }

            scanner.nextLine(); // Consumir quebra de linha
            System.out.println();

            Main.limparTerminal();

            if (comando > numTotal || comando < 0) {
                System.out.println("Comando inválido!!\n");
                continue;
            }

            listaEntidades = ambiente.getEntidades();
            listaRobo.clear();
            for (Entidade entidade : listaEntidades) {
                if (!listaRobo.contains(entidade) && entidade.getTipo() == TipoEntidade.ROBO) {
                    listaRobo.add((Robo) entidade);
                }
            }

            if (comando == 0) {
                break;
            } else if (comando == 3) {
                vizualizarMapa(ambiente);
            } else if (comando == 4) {
                System.out.println(central.exibirMensagens());
            } else {
                // Opções 2, 3 e 4 necessitam de ao menos um robô

                if (livre) {
                    menuLivre(comando, scanner, ambiente, listaRobo, atributos);
                    listaEntidades = ambiente.getEntidades();
                    listaRobo.clear();
                    for (Entidade entidade : listaEntidades) {
                        if (!listaRobo.contains(entidade) && entidade.getTipo() == TipoEntidade.ROBO) {
                            listaRobo.add((Robo) entidade);
                        }
                    }
                }
                if (listaRobo.size() <= 0) {
                    System.out.println("\nNão há robôs criados!\n");
                    continue;
                }

                if (comando == 1) {
                    acoesRobo(scanner, listaRobo, ambiente, central);
                } else if (comando == 2) {
                    // Exibe lista de robôs
                    System.out.println("Lista de robôs por estado:");
                    System.out.println("\n=== Ligados ===");
                    int i = 1;
                    for (Robo robo : listaRobo) {
                        if (robo.getEstado()) {
                            System.out.printf("%d - %s%n", i++, robo.getNome());
                        }
                    }

                    System.out.println("\n=== Desligados ===");
                    i = 1;
                    for (Robo robo : listaRobo) {
                        if (!robo.getEstado()) {
                            System.out.printf("%d - %s%n", i++, robo.getNome());
                        }
                    }
                    System.out.println("");
                }
            }

        }

    }

    /**
     * Gerencia as funcionalidades específicas do modo de criação livre
     * Permite criar robôs, remover robôs e criar obstáculos de forma interativa
     * @param comando Comando selecionado pelo usuário (5, 6 ou 7)
     * @param scanner Scanner para leitura da entrada do usuário
     * @param ambiente Ambiente onde as entidades serão criadas/removidas
     * @param listaRobo Lista atual de robôs no ambiente
     * @param atributos Array para armazenar atributos dos robôs sendo criados
     */
    public static void menuLivre(int comando, Scanner scanner, Ambiente ambiente, ArrayList<Robo> listaRobo,
            Object... atributos) {
        if (comando == 5) {
            // Atributos gerais dos Robôs
            System.out.println("Digite os atributos básicos do Robô:");
            System.out.print("Nome: ");
            atributos[0] = scanner.nextLine();

            do {
                System.out.print("Direção (Norte/Sul/Leste/Oeste): ");
                atributos[1] = scanner.nextLine();
            } while (!Robo.getDirecoesPossiveis().contains(atributos[1]));

            System.out.println("Escolha o material:");
            System.out.println("0 - Alumínio");
            System.out.println("1 - Aço");
            System.out.println("2 - Plástico");
            System.out.println("3 - Fibra de Vidro");
            System.out.println("4 - Fibra de Carbono");
            System.out.print("Material: ");
            int material = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            System.out.println();
            if (material == 0) {
                atributos[2] = (MateriaisRobo) MateriaisRobo.ALUMINIO;
            } else if (material == 1) {
                atributos[2] = (MateriaisRobo) MateriaisRobo.ACO;
            } else if (material == 2) {
                atributos[2] = (MateriaisRobo) MateriaisRobo.PLASTICO;
            } else if (material == 3) {
                atributos[2] = (MateriaisRobo) MateriaisRobo.FIBRA_VIDRO;
            } else if (material == 4) {
                atributos[2] = (MateriaisRobo) MateriaisRobo.FIBRA_CARBONO;

            }
            int[] coordenadas = Main.obterPosicao(ambiente, scanner);
            atributos[3] = coordenadas[0]; // Posição X
            atributos[4] = coordenadas[1]; // Posição Y

            do {
                System.out.print("Velocidade (maior que 0): ");
                atributos[6] = scanner.nextInt();
                scanner.nextLine(); // Consumir quebra de linha
            } while ((Integer) atributos[6] <= 0);

            do {
                System.out
                        .print("Velocidade Máxima: (maior ou igual a velocidade " + atributos[6]
                                + " definida): ");
                atributos[7] = scanner.nextInt();
            } while ((Integer) atributos[7] <= 0 || (Integer) atributos[7] < (Integer) atributos[6]);

            System.out.println("Digite o número do tipo de robô que deseja criar:");
            System.out.println("1 - Terrestre");
            System.out.println("2 - Aéreo");
            System.out.print("Tipo: ");
            int tipo = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            System.out.println();

            if (tipo == 1) {
                // Robôs terrestres
                System.out.println("Digite o número da categoria de robô terrestre:");
                System.out.println("1 - Tanque de Guerra");
                System.out.println("2 - Correios");
                System.out.print("Categoria: ");
                int categoria = scanner.nextInt();
                scanner.nextLine(); // Consumir quebra de linha
                System.out.println();
                atributos[5] = 0;

                if (categoria == 1) {
                    // Atributos específicos do Tanque de Guerra
                    do {
                        System.out.print("Munição Máxima: ");
                        atributos[8] = scanner.nextInt();
                    } while ((Integer) atributos[8] <= 0);

                    do {
                        System.out.print("Alcance: ");
                        atributos[9] = scanner.nextInt();
                    } while ((Integer) atributos[9] <= 0);

                } else if (categoria == 2) {
                    // Atributos específicos dos Correios

                    do {
                        System.out.print("Capacidade Máxima de carga: ");
                        atributos[8] = scanner.nextInt();
                    } while ((Integer) atributos[8] <= 0);

                    do {
                        System.out.print("Peso Máximo suportado: ");
                        atributos[9] = scanner.nextFloat();
                    } while ((Float) atributos[9] <= 0);
                }
                try {
                    ambiente.adicionarEntidade(ambiente.criarRobo(tipo, categoria, atributos));
                } catch (ForaDosLimitesException e) {
                    System.out.println("Não foi possível adicionar o robô, erro: " + e.getMessage());
                }

            } else if (tipo == 2) {
                // Criação de Robôs Aéreos
                System.out.println("Digite o número da categoria de robô aéreo:");
                System.out.println("1 - Drone de Ataque");
                System.out.println("2 - Drone de Vigilância");
                System.out.print("Categoria: ");
                int categoria = scanner.nextInt();
                scanner.nextLine(); // Consumir quebra de linha
                System.out.println();

                do {
                    System.out.print("Altura Máxima (não pode ultrapassar " + ambiente.getTamZ() + "): ");
                    atributos[8] = scanner.nextInt();
                } while ((Integer) atributos[8] > ambiente.getTamZ() || (Integer) atributos[8] <= 0);

                do {
                    System.out.print("Altura Inicial (Menor do que Altura Máxima): ");
                    atributos[5] = scanner.nextInt();
                } while ((Integer) atributos[5] > ambiente.getTamZ()
                        || (Integer) atributos[5] > (Integer) atributos[8]
                        || (Integer) atributos[5] <= 0);

                if (categoria == 1) {
                    // Atributos específicos do Drone de Ataque
                    do {
                        System.out.print("Munição: ");
                        atributos[9] = scanner.nextInt();
                    } while ((Integer) atributos[9] <= 0);

                    do {
                        System.out.print("Alcance: ");
                        atributos[10] = scanner.nextInt();
                    } while ((Integer) atributos[10] <= 0);

                } else if (categoria == 2) {
                    // Atributos específicos do Drone de Vigilância

                    do {
                        System.out.print("Alcance do Radar (número decimal, use ponto): ");
                        atributos[9] = scanner.nextFloat();
                    } while ((Float) atributos[9] <= 0);

                    do {
                        System.out.print("Angulo do Radar (número decimal, use ponto): ");
                        atributos[10] = scanner.nextFloat();
                    } while ((Float) atributos[10] <= 0);

                    do {
                        System.out.print("Ângulo de abertura da câmera (graus, número decimal): ");
                        atributos[11] = scanner.nextFloat();
                    } while ((Float) atributos[11] <= 0);
                }
                try {
                    ambiente.adicionarEntidade(ambiente.criarRobo(tipo, categoria, atributos));
                } catch (ForaDosLimitesException e) {
                    System.out.println("Não foi possível criar o robô Aéreo, erro: " + e.getMessage());
                }
                Main.limparTerminal();
                System.out.println("Robô aéreo adicionado com sucesso!\n");
            }

        } else if (comando == 7) {
            System.out.println("Escolha as coordenadas do obstáculo (X1 e X2, onde X1 é o menor):");
            int X1 = scanner.nextInt();
            int X2 = scanner.nextInt();
            System.out.println("Escolha as coordenadas do obstáculo (Y1 e Y2, onde Y1 é o menor):");
            int Y1 = scanner.nextInt();
            int Y2 = scanner.nextInt();
            System.out.println("Escolha o tipo de obstáculo:");
            System.out.println("0 - Parede");
            System.out.println("1 - Árvore");
            System.out.println("2 - Prédio");
            System.out.println("3 - Buraco");
            System.out.println("4 - Outro");
            System.out.print("Tipo: ");
            int tipoObs = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            System.out.println();
            try {
                if (tipoObs == 0) {
                    ambiente.adicionarEntidade(new Obstaculo(TipoObstaculo.PAREDE, X1, X2, Y1, Y2));
                } else if (tipoObs == 1) {
                    ambiente.adicionarEntidade(new Obstaculo(TipoObstaculo.ARVORE, X1, X2, Y1, Y2));
                } else if (tipoObs == 2) {
                    ambiente.adicionarEntidade(new Obstaculo(TipoObstaculo.PREDIO, X1, X2, Y1, Y2));
                } else if (tipoObs == 3) {
                    ambiente.adicionarEntidade(new Obstaculo(TipoObstaculo.BURACO, X1, X2, Y1, Y2));
                } else if (tipoObs == 4) {
                    Obstaculo outro = new Obstaculo(TipoObstaculo.OUTRO, X1, X2, Y1, Y2);
                    System.out.println("Digite a altura do obstáculo:");
                    int altura = scanner.nextInt();
                    System.out.println("O obstáculo é indestrutível?\n0-Não\n1-Sim");
                    int indestrutivel = scanner.nextInt();
                    int integridade = 0;
                    outro.setIndestrutivel(true);
                    if (indestrutivel == 0) {
                        System.out.println("Digite a integridade (vida) do obstáculo:");
                        integridade = scanner.nextInt();
                        outro.setIndestrutivel(false);
                    }
                    outro.setAltura(altura);
                    outro.setIntegridade(integridade);
                    ambiente.adicionarEntidade(outro);
                }
            } catch (Exception e) {
                System.out.println("Não foi possível adicionar o obstáculo, erro: " + e.getMessage());
            }
        } else if (comando == 6 && listaRobo.size() > 0) {
            // Remoção de um robô
            System.out.println("Escolha o Robô a ser removido:");
            int i = 1;
            for (Robo robo : listaRobo) {
                System.out.printf("%d - %s%n", i++, robo.getNome());
            }
            int index = scanner.nextInt() - 1;
            if (index >= 0 && index < listaRobo.size()) {
                ambiente.removerEntidade(listaRobo.get(index));
                System.out.println("Robô removido!\n");
            }
        }
    }

    /**
     * Exibe uma representação visual do ambiente em formato de mapa
     * Mostra um grid 2D com as entidades representadas por símbolos específicos
     * @param ambiente Ambiente a ser visualizado
     */
    public static void vizualizarMapa(Ambiente ambiente) {
        System.out.println();
        System.out.println("=== Mapa ===");
        String[][] vizumapa = ambiente.visualizarAmbiente();
        int i = 9;
        for (String[] linha : vizumapa) {
            System.out.print((i--)+" ");
            for (String chara : linha) {
                System.out.print(chara);
            }
            System.out.println();
        }
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        System.out.println();
    }

}