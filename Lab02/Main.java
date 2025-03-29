import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) { 
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem vindo ao Simulador de Robôs!\nCrie um ambiente com dimensões 3D ");

        int tamX = scanner.nextInt();
        int tamY = scanner.nextInt();
        int tamZ = scanner.nextInt();

        Ambiente ambiente = new Ambiente(tamX,tamY,tamZ);
        System.out.println("Ambiente criado com tamX:" + tamX + "tamY:" + tamY + "tamZ" + tamZ);
        Object[] atributos = new Object[7];
        ArrayList<Robo> listaRobo;

        while (true) {

            System.out.println("Digite o número da ação que deseja realizar\n0. Fechar o simulador\n1. Criar novo robô\n2. Ações de um robô\n3. Lista de robôs\n4. Remover robô");
    
            int comando = scanner.nextInt();
            listaRobo=ambiente.getListaRobos();
            
            if (comando == 0){
                break;
            }
            else if (comando == 1){
                System.out.println("Digite o número do tipo de robô que deseja criar\n1. Terrestre\n2. Aereo");
                int tipo = scanner.nextInt();

                if (tipo == 1) { // terrestre
                    System.out.println("Digite o número da categoria de robô que deseja criar\n1. Tanque de Guerra\n2. Correios");
                    int categoria = scanner.nextInt();
                    scanner.nextLine();

                    if (categoria==1){//tanque de guerra
                        System.out.println("Digite os atributos\nNome:");
                        atributos[0]=scanner.nextLine();
                        System.out.println("Direção: (Norte/Sul/Leste/Oeste)");
                        atributos[1]=scanner.nextLine();
                        System.out.println("Posição X e Posição Y:");
                        atributos[2]=scanner.nextInt();
                        atributos[3]=scanner.nextInt();
                        while(!ambiente.dentroDosLimites((Integer)atributos[2],(Integer)atributos[3])){
                            System.out.println("Coordenadas fora dos limites, digite novas coordenadas");
                            atributos[2] = scanner.nextInt(); 
                            atributos[3] = scanner.nextInt(); 
                        }
                        System.out.println("Velocidade Máxima:");
                        atributos[4]=scanner.nextInt();
                        System.out.println("Munição Máxima:");
                        atributos[5]=scanner.nextInt();
                        System.out.println("Alcance:");
                        atributos[6]=scanner.nextInt();

                    } else if (categoria==2){//Correios
                        System.out.println("Digite os atributos\nNome:");
                        atributos[0]=scanner.nextLine();
                        System.out.println("Direção: (Norte/Sul/Leste/Oeste)");
                        atributos[1]=scanner.nextLine();
                        System.out.println("Posição X e Posição Y:");
                        atributos[2]=scanner.nextInt();
                        atributos[3]=scanner.nextInt();
                        System.out.println("Velocidade Máxima:");
                        atributos[4]=scanner.nextInt();
                        System.out.println("Capacidade Máxima:");
                        atributos[5]=scanner.nextInt();
                        System.out.println("Peso Máximo:");
                        atributos[6]=scanner.nextFloat();
                    }

                    ambiente.adicionarRobo(ambiente.criarRobo(tipo, categoria, atributos));
                }
                else if (tipo == 2) { // aereo
                    System.out.println("Digite o número da categoria de robô que deseja criar\n1. Drone\n2. N lembro");
                } 
            }
            else if (comando == 2){
                if (listaRobo.size()==0){
                    System.out.println("Não há robôs criados");
                }
                else{
                    System.out.println("Escolha o Robô:");
                    for (Robo robo : listaRobo){
                        System.out.println((listaRobo.indexOf(robo)+1)+". "+robo.getNome());
                    }
                    int index = scanner.nextInt() - 1;
                    if (index >= 0 && index < listaRobo.size()){
                        Robo roboSelecionado = listaRobo.get(index);
                        if (roboSelecionado instanceof TanqueGuerra){
                            TanqueGuerra roboEscolhido = ((TanqueGuerra) roboSelecionado);
                            System.out.println("Digite o número da ação desejada\n1. Exibir posição\n2. Atirar\n3. Recarregar\n4. Mover\n5. Identificar obstáculos\n6. Mudar direção");
                            int acao = scanner.nextInt();
                            scanner.nextLine();
                            if (acao == 1){
                                System.out.println(roboEscolhido.exibirPosicao());
                            } else if (roboEscolhido.getOperando()){
                                if (acao == 2){
                                    System.out.println("Digite as coordenadas X e Y do alvo");
                                    int alvoX = scanner.nextInt();
                                    int alvoY = scanner.nextInt();
                                    System.out.println("Digite o número de tiros");
                                    int nTiros = scanner.nextInt();
                                    System.out.println(roboEscolhido.atirar(alvoX,alvoY,nTiros,ambiente));
                                } else if (acao == 3){
                                    System.out.println("Digite o número de balas recarregadas");
                                    int nBalas = scanner.nextInt();
                                    System.out.println(roboEscolhido.recarregar(nBalas));
                                } else if (acao == 4){
                                    System.out.println("Digite as coordenadas X e Y do destino");
                                    int destinoX = scanner.nextInt(); 
                                    int destinoY = scanner.nextInt(); 
                                    while(!ambiente.dentroDosLimites(destinoX,destinoY)){
                                        System.out.println("Coordenadas fora dos limites, digite novas coordenadas");
                                        destinoX = scanner.nextInt(); 
                                        destinoY = scanner.nextInt(); 
                                    }
                                    System.out.println("Digite a velocidade do movimento");
                                    int vel = scanner.nextInt();
                                    roboEscolhido.mover(destinoX-roboSelecionado.getPosicaoX(), destinoY-roboSelecionado.getPosicaoY(),vel);
                                    System.out.println(roboEscolhido.exibirPosicao());
                                } else if (acao == 5){
                                    ArrayList<Robo> listaRoboVisto= roboEscolhido.identificarRobosDirecao(ambiente, roboEscolhido.getDirecao());
                                    if (listaRoboVisto.size() == 0){
                                        System.out.println("Não há obstáculos");
                                    }
                                    else{
                                        for (Robo robo:listaRoboVisto){
                                            System.out.println(robo.getNome() + " X:" + robo.getPosicaoX() + " Y:" + robo.getPosicaoY());
                                        }
                                    }
                                } else if (acao == 6){
                                    System.out.println("Digite a direção desejada (Norte/Sul/Leste/Oeste)");
                                    String direcao = scanner.nextLine();
                                    roboEscolhido.setDirecao(direcao);
                                }
                            } else{
                                System.out.println("O robô está inoperante");
                            }
                        }
                        if (roboSelecionado instanceof Correios){
                            Correios roboEscolhido = ((Correios) roboSelecionado);
                            System.out.println("Digite o número da ação desejada\n1. Exibir posição\n2. Carregar pacote\n3. Entregar pacote\n4. Listar entregas\n5. Mover\n6. Identificar obstáculos\n7. Mudar direção");
                            int acao = scanner.nextInt();
                            scanner.nextLine();
                            if (acao == 1){
                                System.out.println(roboEscolhido.exibirPosicao());
                            } else if (roboEscolhido.getOperando()){
                                if (acao == 2){
                                    System.out.println("Digite o nome ou id do pacote");
                                    String id = scanner.nextLine();
                                    System.out.println("Digite o peso do pacote");
                                    int peso = scanner.nextInt();
                                    System.out.println(roboEscolhido.carregarPacote(id, peso));
                                } else if (acao == 3){
                                    System.out.println("Digite o nome ou id do pacote");
                                    String id = scanner.nextLine();
                                    System.out.println("Digite as coordenadas X e Y do destino");
                                    int destinoX = scanner.nextInt(); 
                                    int destinoY = scanner.nextInt(); 
                                    while(!ambiente.dentroDosLimites(destinoX,destinoY)){
                                        System.out.println("Coordenadas fora dos limites, digite novas coordenadas");
                                        destinoX = scanner.nextInt(); 
                                        destinoY = scanner.nextInt(); 
                                    }
                                    System.out.println(roboEscolhido.entregarPacote(id, destinoX, destinoY));
                                } else if (acao == 4){
                                    System.out.println(roboEscolhido.listarEntregas());
                                } else if (acao == 5){
                                    System.out.println("Digite as coordenadas X e Y do destino");
                                    int destinoX = scanner.nextInt(); 
                                    int destinoY = scanner.nextInt();
                                    while(!ambiente.dentroDosLimites(destinoX,destinoY)){
                                        System.out.println("Coordenadas fora dos limites, digite novas coordenadas");
                                        destinoX = scanner.nextInt(); 
                                        destinoY = scanner.nextInt(); 
                                    } 
                                    System.out.println("Digite a velocidade do movimento");
                                    int vel = scanner.nextInt();
                                    roboEscolhido.mover(destinoX-roboSelecionado.getPosicaoX(), destinoY-roboSelecionado.getPosicaoY(),vel);
                                } else if (acao == 6){
                                    System.out.println(roboEscolhido.exibirPosicao());
                                    ArrayList<Robo> listaRoboVisto= roboEscolhido.identificarRobosDirecao(ambiente, roboEscolhido.getDirecao());
                                    if (listaRoboVisto.size() == 0){
                                        System.out.println("Não há obstáculos");
                                    }
                                    else{
                                        Robo obstaculo = listaRoboVisto.get(0);
                                        System.out.println(obstaculo.getNome() + "X: " + obstaculo.getPosicaoX() + " Y:" + obstaculo.getPosicaoY());
                                    }
                                } else if (acao==7){
                                    System.out.println("Digite a direção desejada (Norte/Sul/Leste/Oeste)");
                                    String direcao = scanner.nextLine();
                                    roboEscolhido.setDirecao(direcao);
                                }
                            } else{
                                System.out.println("O robô está inoperante");
                            }
                        }
                    }
                }
            }
            else if (comando == 3){
                if (listaRobo.size()==0){
                    System.out.println("Não há robôs criados");
                }
                else{
                    ArrayList<String> nomes = new ArrayList<>();
                    for (Robo robo : listaRobo){
                        nomes.add(robo.getNome());
                    }
                    System.out.print("Lista de robôs: ");
                    System.out.println(String.join(", ", nomes));
                } 
                
            }
            else if (comando == 4){
                if (listaRobo.size()==0){
                    System.out.println("Não há robôs criados");
                }
                else{
                    System.out.println("Escolha o Robô:");
                    for (Robo robo : listaRobo){
                        System.out.println((listaRobo.indexOf(robo)+1)+". "+robo.getNome());
                    }
                    int index = scanner.nextInt() - 1;
                    if (index >= 0 && index < listaRobo.size()){
                        listaRobo.remove(index);
                        System.out.println("Robo removido");
                    }
                }
            }
        }
        scanner.close();
    }
}