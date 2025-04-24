import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem vindo ao Simulador de Robôs!\nCrie um ambiente com dimensões 3D");
        int tamX, tamY, tamZ;
        do {
            System.out.print("X:");
            tamX = scanner.nextInt();
            System.out.print("Y:");
            tamY = scanner.nextInt();
            System.out.print("Z:");
            tamZ = scanner.nextInt();
        } while (tamX <= 0 || tamY <= 0 || tamZ <= 0);

        Ambiente ambiente = new Ambiente(tamX, tamY, tamZ);
        String mensagem = String.format(
                "Ambiente criado com:\nTamX: %d\nTamY: %d\nTamZ: %d\n",
                tamX, tamY, tamZ);
        System.out.println(mensagem);

        Object[] atributos = new Object[8];
        ArrayList<Robo> listaRobo;

        while (true) {

            String menu = String.format(
                    "Digite o número da ação que deseja realizar:\n\n" +
                            "0 - Fechar o simulador\n" +
                            "1 - Criar novo robô\n" +
                            "2 - Ações de um robô\n" +
                            "3 - Lista de robôs\n" +
                            "4 - Remover robô\n" +
                            "5 - Criar obstáculo\n");
            System.out.print(menu);
            System.out.print("Comando:");
            int comando = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            System.out.println();

            Main.limparTerminal();

            if (comando > 5 || comando < 0) {
                System.out.println("Comando inválido!!\n");
                continue;
            }

            listaRobo = ambiente.getListaRobos();

            if (comando == 0) {
                break;
            } else if (comando == 1) {
                // Atributos gerais dos Robôs

                System.out.println("Digite os atributos básicos do Robô");

                System.out.print("Nome:");
                atributos[0] = scanner.nextLine();

                do {
                    System.out.print("Direção (Norte/Sul/Leste/Oeste):");
                    atributos[1] = scanner.nextLine();
                } while (!Robo.getDirecoesPossiveis().contains(atributos[1]));

                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                atributos[2] = coordenadas[0]; // Posição X
                atributos[3] = coordenadas[1]; // Posição Y

                menu = String.format(
                        "Digite o número do tipo de robô que deseja criar:\n\n" +
                                "1 - Terrestre\n" +
                                "2 - Aereo\n");
                System.out.print(menu);
                System.out.print("Tipo:");
                int tipo = scanner.nextInt();
                scanner.nextLine(); // Consumir quebra de linha
                System.out.println();

                if (tipo == 1) {
                    // Robôs terrestres
                    menu = String.format(
                            "Digite o número da categoria de robô que deseja criar:\n\n" +
                                    "1 - Tanque de Guerra\n" +
                                    "2 - Correios\n");
                    System.out.print(menu);
                    System.out.print("Comando:");
                    int categoria = scanner.nextInt();
                    scanner.nextLine(); // Consumir quebra de linha
                    System.out.println();

                    if (categoria == 1) {
                        // Atributos específicos do Tanque de Guerra

                        do {
                            System.out.print("Velocidade Máxima: ");
                            atributos[4] = scanner.nextInt();
                        } while ((Integer) atributos[4] <= 0);

                        do {
                            System.out.print("Munição Máxima: ");
                            atributos[5] = scanner.nextInt();
                        } while ((Integer) atributos[5] <= 0);

                        do {
                            System.out.print("Alcance: ");
                            atributos[6] = scanner.nextInt();
                        } while ((Integer) atributos[6] <= 0);

                    } else if (categoria == 2) {
                        // Atributos específicos do Correios
                        do {
                            System.out.print("Velocidade Máxima:");
                            atributos[4] = scanner.nextInt();
                        } while ((Integer) atributos[4] < 0);

                        do {
                            System.out.print("Capacidade Máxima:");
                            atributos[5] = scanner.nextInt();
                        } while ((Integer) atributos[5] < 0);

                        do {
                            System.out.print("Peso Máximo:");
                            atributos[6] = scanner.nextFloat();
                        } while ((Float) atributos[6] < 0);
                    }

                    ambiente.adicionarRobo(ambiente.criarRobo(tipo, categoria, atributos));
                } else if (tipo == 2) {
                    // Criação de Robôs Aéreos
                    menu = String.format(
                            "Digite o número da categoria de robô que deseja criar:\n\n" +
                                    "1 - Drone de Ataque\n" +
                                    "2 - Drone de Vigilância\n");
                    System.out.print(menu);
                    System.out.print("Comando:");
                    int categoria = scanner.nextInt();
                    scanner.nextLine(); // Consumir quebra de linha
                    System.out.println();

                    do {
                        System.out.print("Altura Máxima:");
                        atributos[5] = scanner.nextInt();
                    } while ((Integer) atributos[5] > ambiente.getTamZ());

                    do {
                        System.out.print("Altura Inicial:");
                        atributos[4] = scanner.nextInt();
                    } while ((Integer) atributos[4] > ambiente.getTamZ()
                            || (Integer) atributos[4] > (Integer) atributos[5]);

                    if (categoria == 1) {
                        // Atributos específicos do Drone de Ataque
                        do {
                            System.out.print("Munição:");
                            atributos[6] = scanner.nextInt();
                        } while ((Integer) atributos[6] < 0);

                        do {
                            System.out.print("Alcance:");
                            atributos[7] = scanner.nextInt();
                        } while ((Integer) atributos[7] < 0);

                    } else if (categoria == 2) {
                        // Atributos específicos do Drone de Vigilância
                        do {
                            System.out.print("Alcance do Radar:");
                            atributos[6] = scanner.nextFloat();
                        } while ((Float) atributos[6] < 0);

                        do {
                            System.out.print("Ângulo de abertura da câmera (Graus):");
                            atributos[7] = scanner.nextFloat();
                        } while ((Float) atributos[7] < 0);
                    }

                    ambiente.adicionarRobo(ambiente.criarRobo(tipo, categoria, atributos));
                    Main.limparTerminal();
                    System.out.println("Robô adicionado com sucesso!\n");

                }
            } else if (comando == 5) {
                System.out.println("Escolha as coordenadas do obstáculo X1 e X2, sendo X1 o menor número e X2 o maior");
                int X1 = scanner.nextInt();
                int X2 = scanner.nextInt();
                System.out.println("Escolha as coordenadas do obstáculo Y1 e Y2, sendo Y1 o menor número e Y2 o maior");
                int Y1 = scanner.nextInt();
                int Y2 = scanner.nextInt();
                menu = String.format("Escolha o tipo de obstáculo:\n\n" +
                        "0 - Parede\n" +
                        "1 - Árvore\n" +
                        "2 - Prédio\n" +
                        "3 - Buraco\n" +
                        "4 - Outro\n");
                System.out.print(menu);
                System.out.print("Tipo:");
                int tipo = scanner.nextInt();
                scanner.nextLine(); // Consumir quebra de linha
                System.out.println();
                if (tipo == 0) {
                    ambiente.adicionarObstaculo(new Obstaculo(TipoObstaculo.PAREDE, X1, X2, Y1, Y2));
                } else if (tipo == 1) {
                    ambiente.adicionarObstaculo(new Obstaculo(TipoObstaculo.ARVORE, X1, X2, Y1, Y2));
                } else if (tipo == 2) {
                    ambiente.adicionarObstaculo(new Obstaculo(TipoObstaculo.PREDIO, X1, X2, Y1, Y2));
                } else if (tipo == 3) {
                    ambiente.adicionarObstaculo(new Obstaculo(TipoObstaculo.BURACO, X1, X2, Y1, Y2));
                } else if (tipo == 4) {
                    Obstaculo outro = new Obstaculo(TipoObstaculo.OUTRO, X1, X2, Y1, Y2);
                    System.out.println("Digite a altura do obstáculo:");
                    int altura = scanner.nextInt();
                    System.out.println("O obstáculo é indestrutível?\n0-Não\n1-Sim");
                    int indestrutivel = scanner.nextInt();
                    int integridade = 0;
                    outro.setIndestrutivel(true);
                    if (indestrutivel == 0) {
                        System.out.println("Digite a integridade (vida) do obstáculo");
                        integridade = scanner.nextInt();
                        outro.setIndestrutivel(false);
                    }
                    outro.setAltura(altura);
                    outro.setIntegridade(integridade);
                    ambiente.adicionarObstaculo(outro);
                }

            } else {
                // Opções 2, 3 e 4 necessitam que haja ao menos um robô
                if (listaRobo.size() <= 0) {
                    System.out.println("\nNão há robôs criados!\n");
                    continue;
                }

                if (comando == 2) {
                    // Realizar ações para um determinado robô

                    System.out.println("Escolha o Robô:\n");
                    int i = 1;
                    for (Robo robo : listaRobo) {
                        System.out.printf("%d - %s%n", i++, robo.getNome());
                    }
                    int index = 0;
                    int sizeLista = listaRobo.size();
                    do {
                        System.out.print("Robo:");
                        index = scanner.nextInt();
                    } while (index <= 0 || index > sizeLista);
                    scanner.nextLine(); // Consumir quebra de linha
                    System.out.println();

                    Robo roboSelecionado = listaRobo.get(index - 1);

                    if (roboSelecionado instanceof TanqueGuerra) {
                        TanqueGuerra roboEscolhido = (TanqueGuerra) roboSelecionado;
                        menu = String.format(
                                "Digite o número da ação desejada:\n\n" +
                                        "1 - Exibir posição\n" +
                                        "2 - Atirar\n" +
                                        "3 - Recarregar\n" +
                                        "4 - Mover\n" +
                                        "5 - Identificar obstáculos\n" +
                                        "6 - Mudar direção\n");
                        System.out.print(menu);
                        System.out.print("Ação:");
                        int acao = scanner.nextInt();
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1) { // Exibição de posição
                            System.out.println(roboEscolhido.exibirPosicao());

                        } else if (roboEscolhido.getOperando()) {
                            // Verificar operabilidade do robô antes de proceder
                            if (acao == 2) {
                                System.out.print("Digite as coordenadas X e Y do alvo:");
                                int alvoX = scanner.nextInt();
                                int alvoY = scanner.nextInt();
                                System.out.print("Digite o número de tiros: ");
                                int nTiros = scanner.nextInt();
                                System.out.println(roboEscolhido.atirar(alvoX, alvoY, nTiros, ambiente));

                            } else if (acao == 3) {
                                // Recarregar balas do Tanque
                                System.out.print("Digite o número de balas recarregadas:");
                                int nBalas = scanner.nextInt();
                                System.out.println(roboEscolhido.recarregar(nBalas));

                            } else if (acao == 4) {
                                // Movimentação do Robô por DeltaX e DeltaY
                                System.out.print("Digite as coordenadas X e Y do destino:");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);

                                // Pede a velocidade para não ultrapassar o limite
                                System.out.print("Digite a velocidade do movimento:");
                                int vel = scanner.nextInt();
                                roboEscolhido.mover(coordenadas[0] - roboSelecionado.getPosicaoX(),
                                        coordenadas[1] - roboSelecionado.getPosicaoY(), vel, ambiente);

                                System.out.println(roboEscolhido.exibirPosicao());

                            } else if (acao == 5) {
                                // Identifica todos os objetos na direção atual do Tanque
                                ArrayList<Robo> listaRoboVisto = roboEscolhido.identificarRobo(ambiente,
                                        roboEscolhido.getDirecao());
                                ArrayList<Obstaculo> listaObstaculoVisto = roboEscolhido.identificarObstaculo(ambiente,
                                        roboEscolhido.getDirecao());
                                if (listaRoboVisto.size() == 0) {
                                    System.out.println("Não há robôs\n");
                                } else {
                                    System.err.println("Robôs:");
                                    for (Robo robo : listaRoboVisto) {
                                        System.out.println(robo.getNome() + " X:" + robo.getPosicaoX() + " Y:"
                                                + robo.getPosicaoY());
                                    }
                                }
                                if (listaObstaculoVisto.size() == 0) {
                                    System.out.println("Não há obstáculos\n");
                                } else {
                                    System.out.println("Obstáculos:");
                                    for (Obstaculo obstaculo : listaObstaculoVisto) {
                                        System.out
                                                .println(obstaculo.getTipo() + " X1:" + obstaculo.getX1() + " X2:"
                                                        + obstaculo.getX2() + " Y1:"
                                                        + obstaculo.getY1() + " Y2:"
                                                        + obstaculo.getY2());
                                    }
                                }
                            } else if (acao == 6) {
                                System.out.print("Digite a direção desejada (Norte/Sul/Leste/Oeste):");
                                String direcao;
                                do {
                                    direcao = scanner.nextLine();
                                } while (!Robo.getDirecoesPossiveis().contains(direcao));
                                roboEscolhido.setDirecao(direcao);
                            }
                        } else {
                            System.out.println("O robô está inoperante\n");
                        }
                    } else if (roboSelecionado instanceof Correios) {
                        Correios roboEscolhido = ((Correios) roboSelecionado);
                        menu = String.format(
                                "Digite o número da ação desejada:\n\n" +
                                        "1 - Exibir posição\n" +
                                        "2 - Carregar pacote\n" +
                                        "3 - Entregar pacote\n" +
                                        "4 - Listar entregas\n" +
                                        "5 - Mover\n" +
                                        "6 - Identificar obstáculos\n" +
                                        "7 - Mudar direção\n");
                        System.out.print(menu);
                        System.out.print("Ação:");
                        int acao = scanner.nextInt();
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1) {
                            System.out.println(roboEscolhido.exibirPosicao());

                        } else if (roboEscolhido.getOperando()) {
                            // Verifica operabilidade do robô
                            if (acao == 2) {
                                // Carregar pacote
                                System.out.print("Digite o nome ou id do pacote:");
                                String id = scanner.nextLine();
                                System.out.print("Digite o peso do pacote:");
                                int peso = scanner.nextInt();
                                System.out.println(roboEscolhido.carregarPacote(id, peso));

                            } else if (acao == 3) {
                                // Entregar um pacote
                                System.out.print("Digite o nome ou id do pacote:");
                                String id = scanner.nextLine();

                                System.out.print("Digite as coordenadas X e Y do destino:");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                                System.out.println(roboEscolhido.entregarPacote(id, coordenadas[0], coordenadas[1], ambiente));

                            } else if (acao == 4) {
                                // Listar todas as entregas
                                System.out.println(roboEscolhido.listarEntregas());

                            } else if (acao == 5) {
                                // Mover robô para um destino
                                System.out.print("Digite as coordenadas X e Y do destino:");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);

                                System.out.print("Digite a velocidade do movimento:");
                                int vel = scanner.nextInt();
                                roboEscolhido.mover(coordenadas[0] - roboSelecionado.getPosicaoX(),
                                        coordenadas[1] - roboSelecionado.getPosicaoY(), vel, ambiente);

                            } else if (acao == 6) {
                                ArrayList<Robo> listaRoboVisto = roboEscolhido.identificarRobo(ambiente,
                                        roboEscolhido.getDirecao());
                                ArrayList<Obstaculo> listaObstaculoVisto = roboEscolhido.identificarObstaculo(ambiente,
                                        roboEscolhido.getDirecao());
                                if (listaRoboVisto.size() == 0) {
                                    System.out.println("Não há robôs\n");
                                } else {
                                    // Apenas o robô obstáculos é visto pelo robô Correio
                                    Robo robo = listaRoboVisto.get(0);
                                    String info_robo = String.format(
                                            "Robô encontrado! Dados:\n\n" +
                                                    "Nome: %s\n" +
                                                    "X: %d\n" + "Y: %d\n",
                                            robo.getNome(), robo.getPosicaoX(), robo.getPosicaoY());
                                    System.out.println(info_robo);
                                }
                                if (listaObstaculoVisto.size() == 0) {
                                    System.out.println("Não há obstáculos\n");
                                } else {
                                    Obstaculo obstaculo = listaObstaculoVisto.get(0);
                                    String info_obstaculo = String.format(
                                            "Obstáculo encontrado! Dados:\n\n" +
                                                    "X1: %d X2: %d\n" + "Y1: %d Y2: %d\n",
                                            obstaculo.getX1(), obstaculo.getX2(), obstaculo.getY1(), obstaculo.getY2());
                                    System.out.println(info_obstaculo);
                                }
                            } else if (acao == 7) {
                                // Mudança de direção do robô
                                System.out.println("Digite a direção desejada (Norte/Sul/Leste/Oeste)");

                                String direcao;
                                do {
                                    direcao = scanner.nextLine();
                                } while (!Robo.getDirecoesPossiveis().contains(direcao));
                                roboEscolhido.setDirecao(direcao);
                            }
                        } else {
                            System.out.println("O robô está inoperante");
                        }
                    } else if (roboSelecionado instanceof DroneAtaque) {
                        DroneAtaque roboEscolhido = (DroneAtaque) roboSelecionado;
                        menu = String.format(
                                "Digite o número da ação desejada:\n\n" +
                                        "1 - Exibir posição\n" +
                                        "2 - Atirar\n" +
                                        "3 - Mover\n" +
                                        "4 - Identificar obstáculos\n" +
                                        "5 - Mudar direção\n");
                        System.out.print(menu);
                        System.out.print("Ação:");
                        int acao = scanner.nextInt();
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1) {
                            System.out.println(roboEscolhido.exibirPosicao());

                        } else if (roboEscolhido.getOperando()) {
                            // Verifica operabilidade do robô
                            if (acao == 2) {
                                // Executar tiro em posição
                                System.out.print("Digite as coordenadas X e Y do alvo:");
                                int alvoX = scanner.nextInt();
                                int alvoY = scanner.nextInt();
                                System.out.print("Digite o número de tiros: ");
                                int nTiros = scanner.nextInt();
                                System.out.println(roboEscolhido.atirar(alvoX, alvoY, nTiros, ambiente));

                            } else if (acao == 3) {
                                // Se movimentar

                                System.out.println("Digite as coordenadas X e Y do destino:");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                                System.out.print("Digite a altura do destino:");
                                int nAlt = scanner.nextInt();

                                roboEscolhido.mover(coordenadas[0], coordenadas[1], nAlt, ambiente);

                                System.out.println(roboEscolhido.exibirPosicao());

                            } else if (acao == 4) {
                                ArrayList<Robo> listaRoboVisto = roboEscolhido.identificarRobo(ambiente,
                                        roboEscolhido.getDirecao());
                                ArrayList<Obstaculo> listaObstaculoVisto = roboEscolhido.identificarObstaculo(ambiente,
                                        roboEscolhido.getDirecao());
                                if (listaRoboVisto.size() == 0) {
                                    System.out.println("Não há robôs\n");
                                } else {
                                    // Apenas o robô obstáculos é visto pelo robô Correio
                                    RoboAereo robo = (RoboAereo) listaRoboVisto.get(0);
                                    String info_robo = String.format(
                                            "Robô encontrado! Dados:\n\n" +
                                                    "Nome: %s\n" +
                                                    "X: %d\n" + "Y: %d\n" + "Z: %d\n",
                                            robo.getNome(), robo.getPosicaoX(), robo.getPosicaoY(), robo.getAltitude());
                                    System.out.println(info_robo);
                                }
                                if (listaObstaculoVisto.size() == 0) {
                                    System.out.println("Não há obstáculos\n");
                                } else {
                                    Obstaculo obstaculo = listaObstaculoVisto.get(0);
                                    String info_obstaculo = String.format(
                                            "Obstáculo encontrado! Dados:\n\n" +
                                                    "X1: %d X2: %d\n" + "Y1: %d Y2: %d\n" + "Altura: %d\n",
                                            obstaculo.getX1(), obstaculo.getX2(), obstaculo.getY1(), obstaculo.getY2(), obstaculo.getAltura());
                                    System.out.println(info_obstaculo);
                                }

                            } else if (acao == 5) {
                                // Mudança de direção do robô
                                System.out.println("Digite a direção desejada (Norte/Sul/Leste/Oeste)");
                                String direcao;
                                do {
                                    direcao = scanner.nextLine();
                                } while (!Robo.getDirecoesPossiveis().contains(direcao));
                                roboEscolhido.setDirecao(direcao);
                            }
                        } else {
                            System.out.println("O robô está inoperante");
                        }
                    } else if (roboSelecionado instanceof DroneVigilancia) {
                        DroneVigilancia roboEscolhido = (DroneVigilancia) roboSelecionado;
                        menu = String.format(
                                "Digite o número da ação desejada:\n\n" +
                                        "1 - Exibir posição\n" +
                                        "2 - Mover\n" +
                                        "3 - Identificar obstáculos\n" +
                                        "4 - Mudar direção\n" +
                                        "5 - Varrer Área\n");

                        if (roboEscolhido.isCamuflado()) {
                            menu += "6 - Desativar Camuflagem\n";
                        } else {
                            menu += "6 - Ativar Camuflagem\n";
                        }

                        System.out.print(menu);
                        System.out.print("Ação:");
                        int acao = scanner.nextInt();
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1) { // Exibir posição do Drone
                            System.out.println(roboEscolhido.exibirPosicao());
                        }
                        if (roboEscolhido.getOperando()) {

                            if (acao == 2) { // Movimentar Drone

                                System.out.print("Digite as coordenadas X e Y do destino:");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                                System.out.print("Digite a altura do destino:");
                                int nAlt = scanner.nextInt();

                                roboEscolhido.mover(coordenadas[0], coordenadas[1], nAlt, ambiente);

                                System.out.println(roboEscolhido.exibirPosicao());

                            } else if (acao == 3) { // Identificar obstáculos do Drone
                                // Identificar todos os obstáculos
                                ArrayList<Robo> robosAlcanceRadar = roboEscolhido.identificarObstaculo(ambiente);
                                if (robosAlcanceRadar.size() == 0) {
                                    System.out.println("Não há obstáculos");

                                } else {
                                    // Todos os obstáculos detectados são exibidos!
                                    System.out.print("Obstáculo Encontrado!\nDados:\n\n");
                                    String info_obstaculo;
                                    for (Robo robo : robosAlcanceRadar) {
                                        // Não exibir a si mesmo
                                        if (robo.getNome() == roboEscolhido.getNome()) {
                                            continue;
                                        }
                                        if (robo instanceof RoboAereo) {
                                            info_obstaculo = String.format(
                                                    "Nome: %s\n" +
                                                            "X: %d\n" + "Y: %d\n" + "Altura: %d\n",
                                                    robo.getNome(), robo.getPosicaoX(), robo.getPosicaoY(),
                                                    ((RoboAereo) robo).getAltitude());
                                        } else if (robo instanceof RoboTerrestre) {
                                            info_obstaculo = String.format(
                                                    "Nome: %s\n" +
                                                            "X: %d\n" + "Y: %d\n",
                                                    robo.getNome(), robo.getPosicaoX(), robo.getPosicaoY());
                                        } else {
                                            info_obstaculo = "Robô de tipo não definido!";
                                        }
                                        System.out.println(info_obstaculo);
                                    }
                                }

                            } else if (acao == 4) { // Mudar direção do Drone
                                System.out.println("Digite a direção desejada (Norte/Sul/Leste/Oeste)");
                                String direcao;
                                do {
                                    direcao = scanner.nextLine();
                                } while (!Robo.getDirecoesPossiveis().contains(direcao));
                                roboEscolhido.setDirecao(direcao);

                            } else if (acao == 5) { // Varrer Área
                                System.out.println("Digite as coordenadas do centro de varredura");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);

                                System.out.print("Digite o raio da varredura:");
                                int raio = scanner.nextInt();

                                ArrayList<Robo> robos_encontrados = roboEscolhido.varrerArea(ambiente, coordenadas[0],
                                        coordenadas[1], raio);

                                if (robos_encontrados.size() == 0) {
                                    System.out.println("Nenhum robô foi encontrado!");
                                } else {
                                    System.out.println("Robôs encontrados!");
                                    String info_obstaculo;
                                    for (Robo robo : robos_encontrados) {
                                        if (robo instanceof RoboAereo) {
                                            info_obstaculo = String.format(
                                                    "Nome: %s\n" +
                                                            "Tipo: Robô Aéreo\n" +
                                                            "X: %d\n" + "Y: %d\n" + "Altura: %d\n",
                                                    robo.getNome(), robo.getPosicaoX(), robo.getPosicaoY(),
                                                    ((RoboAereo) robo).getAltitude());
                                        } else if (robo instanceof RoboTerrestre) {
                                            info_obstaculo = String.format(
                                                    "Nome: %s (RoboTerrestre)\n" +
                                                            "TIpo: Robô Terrestre\n" +
                                                            "X: %d\n" + "Y: %d\n",
                                                    robo.getNome(), robo.getPosicaoX(), robo.getPosicaoY());
                                        } else {
                                            info_obstaculo = "Robô de tipo não definido!";
                                        }
                                        System.out.println(info_obstaculo);
                                    }
                                }

                            } else if (acao == 6) { // Acionar/Desativar Camuflagem
                                if (roboEscolhido.isCamuflado()) {
                                    roboEscolhido.desabilitarCamuflagem();
                                } else {
                                    roboEscolhido.acionarCamuflagem();
                                }
                            }
                        } else {
                            System.out.println("Robô escolhido não está operante!");
                        }

                    } else {
                        System.out.println("Tipo de Robô inválido!");
                    }

                } else if (comando == 3) {
                    // Exibe lista de robôs no ambiente
                    System.out.println("Lista de robôs:");
                    int i = 1;
                    for (Robo robo : listaRobo) {
                        System.out.printf("%d - %s %n", i++, robo.getNome());
                    }
                    System.out.println("");

                } else if (comando == 4) {
                    // Remoção de um robô do ambiente

                    System.out.println("Escolha o Robô:");
                    int i = 1;
                    for (Robo robo : listaRobo) {
                        System.out.printf("%d - %s %n", i++, robo.getNome());
                    }
                    int index = scanner.nextInt() - 1;
                    if (index >= 0 && index < listaRobo.size()) {
                        ambiente.removerRobo(listaRobo.get(index));
                        System.out.println("Robo removido!\n");
                    }
                }
            }
        }
        scanner.close();

    }

    public static void limparTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static int[] obterPosicao(Ambiente ambiente, Scanner scanner) {
        // Função para obter posição já com a validação do limite

        System.out.print("Posição X:");
        int destinoX = scanner.nextInt();

        System.out.print("Posição Y:");
        int destinoY = scanner.nextInt();

        while (!ambiente.dentroDosLimites(destinoX, destinoY)) {
            System.out.println("Coordenadas fora dos limites, digite novas coordenadas");

            System.out.print("Posição X:");
            destinoX = scanner.nextInt();

            System.out.print("Posição Y:");
            destinoY = scanner.nextInt();
        }
    
        System.out.println();
        return new int[] { destinoX, destinoY };
    }
}