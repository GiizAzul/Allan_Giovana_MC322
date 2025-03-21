import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) { 
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem vindo ao Simulador de Robôs!\nCrie um ambiente com largura e altura separados por espaço");

        int largura = scanner.nextInt();
        int altura = scanner.nextInt();
        Ambiente ambiente = new Ambiente(largura,altura);
        System.out.println("Ambiente criado com largura " + largura + " e altura " + altura);
        Object[] atributos = new Object[7];

        while (true) {
            System.out.println("Digite o número da ação que deseja realizar\n0. Fechar o simulador\n1. Criar novo robô\n2. Ações de um robô\n3. Lista de robôs");
    
            int comando = scanner.nextInt();
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
                        System.out.println("Direção:");
                        atributos[1]=scanner.nextLine();
                        System.out.println("Posição X e Posição Y:");
                        atributos[2]=scanner.nextInt();
                        atributos[3]=scanner.nextInt();
                        System.out.println("Velocidade Máxima:");
                        atributos[4]=scanner.nextInt();
                        System.out.println("Munição Máxima:");
                        atributos[5]=scanner.nextInt();
                        System.out.println("Alcance:");
                        atributos[6]=scanner.nextInt();

                    } else if (categoria==2){//Correios
                        //String nome, String direcao, int posicaoX, int posicaoY, int velocidadeMaxima, int capacidadeMax, float pesoMax
                        System.out.println("Digite os atributos\nNome:");
                        atributos[0]=scanner.nextLine();
                        System.out.println("Direção:");
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
                if (ambiente.getListaRobos().size()==0){
                    System.out.println("Não há robôs criados");
                }
                else{
                    System.out.println("Escolha o Robô:");
                    for (Robo robo : ambiente.getListaRobos()){
                        System.out.println((ambiente.getListaRobos().indexOf(robo)+1)+". "+robo.nome);
                    }
                    int index = scanner.nextInt() - 1;
                    if (index >= 0 && index < ambiente.getListaRobos().size()){
                        Robo roboSelecionado = ambiente.getListaRobos().get(index);
                        if (roboSelecionado instanceof TanqueGuerra){
                            System.out.println("Digite o número da ação desejada\n1. Atirar\n2. Defender\n3. Recarregar\n4. Mover\n5. Exibir posição");
                            int acao = scanner.nextInt();
                            if (acao == 1){
                                System.out.println("Digite as coordenadas X e Y do alvo");
                                int alvoX = scanner.nextInt();
                                int alvoY = scanner.nextInt();
                                System.out.println("Digite o número de tiros");
                                int nTiros = scanner.nextInt();
                                System.out.println(((TanqueGuerra) roboSelecionado).atirar(alvoX,alvoY,nTiros));
                            } else if (acao == 2){
                                System.out.println("Digite o dano tomado");
                                int dano = scanner.nextInt();
                                System.out.println(((TanqueGuerra) roboSelecionado).defender(dano));
                            } else if (acao == 3){
                                System.out.println("Digite o número de balas recarregadas");
                                int nBalas = scanner.nextInt();
                                System.out.println(((TanqueGuerra) roboSelecionado).recarregar(nBalas));
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
                                ((TanqueGuerra) roboSelecionado).mover(destinoX-roboSelecionado.getPosicaoX(), destinoY-roboSelecionado.getPosicaoY(),vel);
                                ((TanqueGuerra) roboSelecionado).exibirPosicao();
                            } else if (acao == 5){
                                ((TanqueGuerra) roboSelecionado).exibirPosicao();
                            }
                        }
                        if (roboSelecionado instanceof Correios){
                            System.out.println("Digite o número da ação desejada\n1. Carregar pacote\n2. Entregar pacote\n3. Listar entregas\n4. Mover\n5. Exibir posição");
                            int acao = scanner.nextInt();
                            if (acao == 1){
                                System.out.println("Digite o nome ou id do pacote");
                                String id = scanner.nextLine();
                                System.out.println("Digite o peso do pacote");
                                int peso = scanner.nextInt();
                                System.out.println(((Correios) roboSelecionado).carregarPacote(id, peso));
                            } else if (acao == 2){
                                System.out.println("Digite o nome ou id do pacote");
                                String id = scanner.nextLine();
                                System.out.println("Digite as coordenadas X e Y do destino");
                                int destinoX = scanner.nextInt(); 
                                int destinoY = scanner.nextInt(); 
                                System.out.println(((Correios) roboSelecionado).entregarPacote(id, destinoX, destinoY));
                            } else if (acao == 3){
                                System.out.println(((Correios) roboSelecionado).listarEntregas());
                            } else if (acao == 4){
                                System.out.println("Digite as coordenadas X e Y do destino");
                                int destinoX = scanner.nextInt(); 
                                int destinoY = scanner.nextInt(); 
                                System.out.println("Digite a velocidade do movimento");
                                int vel = scanner.nextInt();
                                ((Correios) roboSelecionado).mover(destinoX-roboSelecionado.getPosicaoX(), destinoY-roboSelecionado.getPosicaoY(),vel);
                                ((Correios) roboSelecionado).exibirPosicao();
                            } else if (acao == 5){
                                ((Correios) roboSelecionado).exibirPosicao();
                            }
                        }
                    }
                }
            }
            else if (comando == 3){
                ArrayList<String> nomes = new ArrayList<>();
                for (Robo robo : ambiente.getListaRobos()){
                    nomes.add(robo.nome);
                }
                System.out.println(String.join(", ", nomes));
                
            }
        }
        scanner.close();
    }
}