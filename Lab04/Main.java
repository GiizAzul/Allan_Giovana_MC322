import java.util.ArrayList;
import java.util.Scanner;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import robos.aereos.DroneAtaque;
import robos.aereos.DroneVigilancia;
import robos.aereos.RoboAereo;
import robos.geral.MateriaisRobo;
import robos.geral.Robo;
import robos.terrestres.Correios;
import robos.terrestres.TanqueGuerra;
import interfaces.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem vindo ao Simulador de Robôs!");
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

        Object[] atributos = new Object[10];
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
            System.out.print("Comando: ");
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
                System.out.println("Digite os atributos básicos do Robô:");
                System.out.print("Nome: ");
                atributos[0] = scanner.nextLine();

                do {
                    System.out.print("Direção (Norte/Sul/Leste/Oeste): ");
                    atributos[1] = scanner.nextLine();
                } while (!Robo.getDirecoesPossiveis().contains(atributos[1]));

                System.out.println("Escolha o material:");
                System.out.println("0 - ALUMINO");
                System.out.println("1 - ACO");
                System.out.println("2 - PLASTICO");
                System.out.println("3 - FIBRA_VIDRO");
                System.out.println("4 - FIBRA_CARBONO");
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

                    if (categoria == 1) {
                        // Atributos específicos do Tanque de Guerra
                        do {
                            System.out.print("Velocidade Máxima: ");
                            atributos[5] = scanner.nextInt();
                        } while ((Integer) atributos[5] <= 0);

                        do {
                            System.out.print("Munição Máxima: ");
                            atributos[6] = scanner.nextInt();
                        } while ((Integer) atributos[6] <= 0);

                        do {
                            System.out.print("Alcance: ");
                            atributos[7] = scanner.nextInt();
                        } while ((Integer) atributos[7] <= 0);

                    } else if (categoria == 2) {
                        // Atributos específicos dos Correios
                        do {
                            System.out.print("Velocidade Máxima: ");
                            atributos[5] = scanner.nextInt();
                        } while ((Integer) atributos[5] < 0);

                        do {
                            System.out.print("Capacidade Máxima de carga: ");
                            atributos[6] = scanner.nextInt();
                        } while ((Integer) atributos[6] < 0);

                        do {
                            System.out.print("Peso Máximo suportado: ");
                            atributos[7] = scanner.nextFloat();
                        } while ((Float) atributos[7] < 0);
                    }

                    ambiente.adicionarRobo(ambiente.criarRobo(tipo, categoria, atributos));

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
                        System.out.print("Velocidade Máxima: ");
                        atributos[5] = scanner.nextInt();
                    } while ((Integer) atributos[5] < 0);

                    do {
                        System.out.print("Altura Máxima (não pode ultrapassar " + ambiente.getTamZ() + "): ");
                        atributos[7] = scanner.nextInt();
                    } while ((Integer) atributos[7] > ambiente.getTamZ() || (Integer) atributos[7] <= 0);

                    do {
                        System.out.print("Altura Inicial (Menor do que Altura Máxima): ");
                        atributos[6] = scanner.nextInt();
                    } while ((Integer) atributos[6] > ambiente.getTamZ()
                            || (Integer) atributos[6] > (Integer) atributos[7]
                            || (Integer) atributos[6] <= 0);

                    if (categoria == 1) {
                        // Atributos específicos do Drone de Ataque
                        do {
                            System.out.print("Munição: ");
                            atributos[8] = scanner.nextInt();
                        } while ((Integer) atributos[8] < 0);

                        do {
                            System.out.print("Alcance: ");
                            atributos[9] = scanner.nextInt();
                        } while ((Integer) atributos[9] < 0);

                    } else if (categoria == 2) {
                        // Atributos específicos do Drone de Vigilância
                        do {
                            System.out.print("Alcance do Radar (número decimal, use ponto): ");
                            atributos[7] = scanner.nextFloat();
                        } while ((Float) atributos[7] < 0);

                        do {
                            System.out.print("Ângulo de abertura da câmera (graus, número decimal): ");
                            atributos[8] = scanner.nextFloat();
                        } while ((Float) atributos[8] < 0);
                    }
                    ambiente.adicionarRobo(ambiente.criarRobo(tipo, categoria, atributos));
                    Main.limparTerminal();
                    System.out.println("Robô aéreo adicionado com sucesso!\n");
                }

            } else if (comando == 5) {
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
                if (tipoObs == 0) {
                    ambiente.adicionarObstaculo(new Obstaculo(TipoObstaculo.PAREDE, X1, X2, Y1, Y2));
                } else if (tipoObs == 1) {
                    ambiente.adicionarObstaculo(new Obstaculo(TipoObstaculo.ARVORE, X1, X2, Y1, Y2));
                } else if (tipoObs == 2) {
                    ambiente.adicionarObstaculo(new Obstaculo(TipoObstaculo.PREDIO, X1, X2, Y1, Y2));
                } else if (tipoObs == 3) {
                    ambiente.adicionarObstaculo(new Obstaculo(TipoObstaculo.BURACO, X1, X2, Y1, Y2));
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
                    ambiente.adicionarObstaculo(outro);
                }

            } else {
                // Opções 2, 3 e 4 necessitam de ao menos um robô
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
                        System.out.print("Robô: ");
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
                                        "5 - Mudar direção\n");
                        System.out.print(menu);
                        System.out.print("Ação: ");
                        int acao = scanner.nextInt();
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1) { // Exibição de posição
                            System.out.println(roboEscolhido.exibirPosicao());
                        } else if (roboEscolhido.getOperando()) {
                            // Verificar operabilidade do robô antes de proceder
                            if (acao == 2) {
                                System.out.print("Digite as coordenadas X e Y do alvo: ");
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
                                System.out.print("Digite a velocidade do movimento: ");
                                int vel = scanner.nextInt();
                                roboEscolhido.mover(coordenadas[0] - roboSelecionado.getX(),
                                        coordenadas[1] - roboSelecionado.getY(), vel, ambiente);
                                System.out.println(roboEscolhido.exibirPosicao());
                            } else if (acao == 5) {
                                System.out.print("Digite a nova direção (Norte/Sul/Leste/Oeste): ");
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
                        Correios roboEscolhido = (Correios) roboSelecionado;
                        menu = String.format(
                                "Digite o número da ação desejada:\n\n" +
                                        "1 - Exibir posição\n" +
                                        "2 - Carregar pacote\n" +
                                        "3 - Entregar pacote\n" +
                                        "4 - Listar entregas\n" +
                                        "5 - Mover\n" +
                                        "6 - Mudar direção\n");
                        System.out.print(menu);
                        System.out.print("Ação: ");
                        int acao = scanner.nextInt();
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1) {
                            System.out.println(roboEscolhido.exibirPosicao());
                        } else if (roboEscolhido.getOperando()) {
                            if (acao == 2) {
                                System.out.print("Digite o nome ou ID do pacote: ");
                                String id = scanner.nextLine();
                                System.out.print("Digite o peso do pacote: ");
                                int peso = scanner.nextInt();
                                System.out.println(roboEscolhido.carregarPacote(id, peso));
                            } else if (acao == 3) {
                                System.out.print("Digite o nome ou ID do pacote: ");
                                String id = scanner.nextLine();
                                System.out.print("Digite as coordenadas X e Y do destino: ");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                                System.out.println(
                                        roboEscolhido.entregarPacote(id, coordenadas[0], coordenadas[1], ambiente));
                            } else if (acao == 4) {
                                System.out.println(roboEscolhido.listarEntregas());
                            } else if (acao == 5) {
                                System.out.print("Digite as coordenadas X e Y do destino: ");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                                System.out.print("Digite a velocidade do movimento: ");
                                int vel = scanner.nextInt();
                                roboEscolhido.mover(coordenadas[0] - roboSelecionado.getX(),
                                        coordenadas[1] - roboSelecionado.getY(), vel, ambiente);
                            } else if (acao == 6) {
                                System.out.print("Digite a nova direção (Norte/Sul/Leste/Oeste): ");
                                String direcao;
                                do {
                                    direcao = scanner.nextLine();
                                } while (!Robo.getDirecoesPossiveis().contains(direcao));
                                roboEscolhido.setDirecao(direcao);
                            }
                        } else {
                            System.out.println("O robô está inoperante\n");
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
                        System.out.print("Ação: ");
                        int acao = scanner.nextInt();
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1) {
                            System.out.println(roboEscolhido.exibirPosicao());
                        } else if (roboEscolhido.getOperando()) {
                            if (acao == 2) {
                                System.out.print("Digite as coordenadas X, Y e Z do alvo: ");
                                int alvoX = scanner.nextInt();
                                int alvoY = scanner.nextInt();
                                int alvoZ = scanner.nextInt();
                                System.out.print("Digite o número de tiros: ");
                                int nTiros = scanner.nextInt();
                                System.out.println(roboEscolhido.atirar(alvoX, alvoY, alvoZ, nTiros, ambiente));
                            } else if (acao == 3) {
                                System.out.print("Digite as coordenadas X e Y do destino: ");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                                System.out.print("Digite a altura do destino: ");
                                int nAlt = scanner.nextInt();
                                roboEscolhido.mover(coordenadas[0], coordenadas[1], nAlt, ambiente);
                                System.out.println(roboEscolhido.exibirPosicao());
                            } else if (acao == 4) {
                                ArrayList<Entidade> listaObjetosVistos = roboEscolhido.getRadar().acionar();
                                if (listaObjetosVistos.isEmpty()) {
                                    System.out.println("Nenhum objeto encontrado!");
                                    continue;
                                } else {
                                    for (Entidade elemento : listaObjetosVistos) {
                                        if (elemento instanceof Robo) {
                                            if (elemento instanceof RoboAereo) {
                                                RoboAereo r = (RoboAereo) elemento;
                                                ;
                                                System.out.printf("Robô encontrado: %s, X: %d, Y: %d, Altura: %d%n",
                                                        r.getNome(), r.getPosicaoXInterna(), r.getPosicaoYInterna(),
                                                        r.getZ());
                                            } else if (elemento instanceof Obstaculo) {
                                                Obstaculo o = (Obstaculo) elemento;
                                                System.out.printf(
                                                        "Obstáculo encontrado: %s, X1: %d, X2: %d, Y1: %d, Y2: %d, Altura: %d%n",
                                                        o.getTipo(), o.getX1(), o.getX2(), o.getY1(), o.getY2(),
                                                        o.getAltura());
                                            } else {
                                                Robo r = (Robo) elemento;
                                                System.out.printf("Robô encontrado: %s, X: %d, Y: %d%n",
                                                        r.getNome(), r.getPosicaoXInterna(), r.getPosicaoYInterna());
                                            }
                                        }
                                    }
                                }

                            } else if (acao == 5) {
                                System.out.print("Digite a nova direção (Norte/Sul/Leste/Oeste): ");
                                String direcao;
                                do {
                                    direcao = scanner.nextLine();
                                } while (!Robo.getDirecoesPossiveis().contains(direcao));
                                roboEscolhido.setDirecao(direcao);
                            }
                        } else {
                            System.out.println("Robô inoperante!");
                        }

                    } else if (roboSelecionado instanceof DroneVigilancia) {
                        DroneVigilancia roboEscolhido = (DroneVigilancia) roboSelecionado;
                        menu = String.format(
                                "Digite o número da ação desejada:\n\n" +
                                        "1 - Exibir posição\n" +
                                        "2 - Mover\n" +
                                        "3 - Identificar obstáculos\n" +
                                        "4 - Mudar direção\n" +
                                        "5 - Varrer Área\n" +
                                        "6 - %s Camuflagem\n",
                                roboEscolhido.isCamuflado() ? "Desativar" : "Ativar");
                        System.out.print(menu);
                        System.out.print("Ação: ");
                        int acao = scanner.nextInt();
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1) {
                            System.out.println(roboEscolhido.exibirPosicao());
                        }
                        if (roboEscolhido.getOperando()) {
                            if (acao == 2) {
                                System.out.print("Digite as coordenadas X e Y do destino: ");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                                System.out.print("Digite a altura do destino: ");
                                int nAlt = scanner.nextInt();
                                roboEscolhido.mover(coordenadas[0], coordenadas[1], nAlt, ambiente);
                                System.out.println(roboEscolhido.exibirPosicao());
                            } else if (acao == 3) {
                                ArrayList<Entidade> listaObjetosVistos = roboEscolhido.getRadar().acionar();
                                if (listaObjetosVistos.isEmpty()) {
                                    System.out.println("Nenhum objeto encontrado!");
                                    continue;
                                } else {
                                    for (Entidade elemento : listaObjetosVistos) {
                                        if (elemento instanceof Robo) {
                                            if (elemento instanceof RoboAereo) {
                                                RoboAereo r = (RoboAereo) elemento;
                                                ;
                                                System.out.printf("Robô encontrado: %s, X: %d, Y: %d, Altura: %d%n",
                                                        r.getNome(), r.getPosicaoXInterna(), r.getPosicaoYInterna(),
                                                        r.getZ());
                                            } else if (elemento instanceof Obstaculo) {
                                                Obstaculo o = (Obstaculo) elemento;
                                                System.out.printf(
                                                        "Obstáculo encontrado: %s, X1: %d, X2: %d, Y1: %d, Y2: %d, Altura: %d%n",
                                                        o.getTipo(), o.getX1(), o.getX2(), o.getY1(), o.getY2(),
                                                        o.getAltura());
                                            } else {
                                                Robo r = (Robo) elemento;
                                                System.out.printf("Robô encontrado: %s, X: %d, Y: %d%n",
                                                        r.getNome(), r.getPosicaoXInterna(), r.getPosicaoYInterna());
                                            }
                                        }
                                    }
                                }
                            } else if (acao == 4) {
                                System.out.print("Digite a nova direção (Norte/Sul/Leste/Oeste): ");
                                String direcao;
                                do {
                                    direcao = scanner.nextLine();
                                } while (!Robo.getDirecoesPossiveis().contains(direcao));
                                roboEscolhido.setDirecao(direcao);
                            } else if (acao == 5) {
                                System.out.println("Digite as coordenadas do centro de varredura:");
                                int[] centro = Main.obterPosicao(ambiente, scanner);
                                System.out.print("Digite o raio da varredura: ");
                                int raio = scanner.nextInt();
                                ArrayList<Entidade> objetos_encontrados = roboEscolhido.varrerArea(ambiente, centro[0],
                                        centro[1], raio);
                                if (objetos_encontrados.isEmpty())
                                    System.out.println("Nenhum objeto foi encontrado!");
                                else {
                                    System.out.println("Objetos encontrados:");
                                    for (Entidade obj : objetos_encontrados) {
                                        if (obj instanceof Robo) {
                                            Robo r = (Robo) obj;
                                            System.out.printf("Robô: %s, X: %d, Y: %d, Altura: %s%n",
                                                    r.getNome(), r.getX(), r.getY(),
                                                    (r instanceof RoboAereo ? ((RoboAereo) r).getZ() : "N/A"));
                                        } else {
                                            Obstaculo o = (Obstaculo) obj;
                                            System.out.printf(
                                                    "Obstáculo: %s, X1: %d, X2: %d, Y1: %d, Y2: %d, Altura: %d%n",
                                                    o.getTipo(), o.getX1(), o.getX2(), o.getY1(), o.getY2(),
                                                    o.getAltura());
                                        }
                                    }
                                }
                            } else if (acao == 6) {
                                if (roboEscolhido.isCamuflado())
                                    roboEscolhido.desabilitarCamuflagem();
                                else
                                    roboEscolhido.acionarCamuflagem();
                            }
                        } else {
                            System.out.println("Robô inoperante!");
                        }
                    } else {
                        System.out.println("Tipo de Robô inválido!");
                    }
                } else if (comando == 3) {
                    // Exibe lista de robôs
                    System.out.println("Lista de robôs:");
                    int i = 1;
                    for (Robo robo : listaRobo) {
                        System.out.printf("%d - %s%n", i++, robo.getNome());
                    }
                    System.out.println("");
                } else if (comando == 4) {
                    // Remoção de um robô
                    System.out.println("Escolha o Robô a ser removido:");
                    int i = 1;
                    for (Robo robo : listaRobo) {
                        System.out.printf("%d - %s%n", i++, robo.getNome());
                    }
                    int index = scanner.nextInt() - 1;
                    if (index >= 0 && index < listaRobo.size()) {
                        ambiente.removerRobo(listaRobo.get(index));
                        System.out.println("Robô removido!\n");
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
}