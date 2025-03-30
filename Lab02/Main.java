import java.util.ArrayList;
import java.util.Scanner;

public class Main{

    public static void main(String[] args) { 
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem vindo ao Simulador de Robôs!\nCrie um ambiente com dimensões 3D no formato X Y Z");

        int tamX = scanner.nextInt();
        int tamY = scanner.nextInt();
        int tamZ = scanner.nextInt();

        Ambiente ambiente = new Ambiente(tamX,tamY,tamZ);
        String mensagem = String.format(
            "Ambiente criado com:\nTamX: %d\nTamY: %d\nTamZ: %d\n",
            tamX, tamY, tamZ
        );
        System.out.println(mensagem);

        Object[] atributos = new Object[7];
        ArrayList<Robo> listaRobo;

        while (true) {

            String menu = String.format(
                "Digite o número da ação que deseja realizar:\n\n" +
                "0 - Fechar o simulador\n" +
                "1 - Criar novo robô\n" +
                "2 - Ações de um robô\n" +
                "3 - Lista de robôs\n" +
                "4 - Remover robô\n"
            );
            System.out.print(menu);
            System.out.print("Comando:");
            int comando = scanner.nextInt() ;
            scanner.nextLine(); // Consumir quebra de linha
            System.out.println();

            if (comando > 4 || comando < 0) {
                System.out.println("Comando inválido!!\n");
                continue;
            }

            listaRobo = ambiente.getListaRobos();
            
            if (comando == 0){
                break;
            }
            else if (comando == 1){
                // Atributos gerais dos Robôs

                System.out.println("Digite os atributos básicos do Robô");

                System.out.print("Nome:");
                atributos[0]=scanner.nextLine();

                System.out.print("Direção (Norte/Sul/Leste/Oeste):");
                atributos[1]=scanner.nextLine();

                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                atributos[2] = coordenadas[0]; // Posição X
                atributos[3] = coordenadas[1]; // Posição Y

                menu = String.format(
                    "Digite o número do tipo de robô que deseja criar:\n\n" +
                    "1 - Terrestre\n" + 
                    "2 - Aereo\n"
                );
                System.out.print(menu);
                System.out.print("Tipo:");
                int tipo = scanner.nextInt() ;
                scanner.nextLine(); // Consumir quebra de linha
                System.out.println();

                if (tipo == 1) { 
                    // Robôs terrestres
                    menu = String.format(
                        "Digite o número da categoria de robô que deseja criar:\n\n" +
                        "1 - Tanque de Guerra\n" + 
                        "2 - Correios\n"
                    );
                    System.out.print(menu);
                    System.out.print("Comando:");
                    int categoria = scanner.nextInt() ;
                    scanner.nextLine(); // Consumir quebra de linha
                    System.out.println();

                    if (categoria == 1) { 
                        // Atributos específicos do Tanque de Guerra
    
                        System.out.print("Velocidade Máxima:");
                        atributos[4]=scanner.nextInt();

                        System.out.print("Munição Máxima:");
                        atributos[5]=scanner.nextInt();

                        System.out.print("Alcance:");
                        atributos[6]=scanner.nextInt();

                    } else if (categoria==2){
                        // Atributos específicos do Correios

                        System.out.print("Velocidade Máxima:");
                        atributos[4]=scanner.nextInt();

                        System.out.print("Capacidade Máxima:");
                        atributos[5]=scanner.nextInt();

                        System.out.print("Peso Máximo:");
                        atributos[6]=scanner.nextFloat();
                    }

                    ambiente.adicionarRobo(ambiente.criarRobo(tipo, categoria, atributos));
                }
                else if (tipo == 2) {
                    // Criação de Robôs Aéreos
                    menu = String.format(
                        "Digite o número da categoria de robô que deseja criar:\n\n" +
                        "1 - Drone de Ataque\n" + 
                        "2 - Drone de Vigilância\n"
                    );
                    System.out.print(menu);
                    System.out.print("Comando:");
                    int categoria = scanner.nextInt() ;
                    scanner.nextLine(); // Consumir quebra de linha
                    System.out.println();

                    System.out.print("Altura Inicial:");
                    atributos[4]=scanner.nextInt();

                    System.out.print("Altura Máxima:");
                    atributos[5]=scanner.nextInt();

                    if (categoria == 1) { 
                        // Atributos específicos do Drone de Ataque
    
                        System.out.print("Munição:");
                        atributos[6]=scanner.nextInt();

                        System.out.print("Alcance:");
                        atributos[7]=scanner.nextInt();

                    } else if (categoria == 2){
                        // Atributos específicos do Drone de Vigilância

                        System.out.print("Alcance do Radar:");
                        atributos[6] = scanner.nextInt();

                        System.out.print("Ângulo de abertura da câmera (Graus):");
                        atributos[7] = scanner.nextFloat();
                    }

                    ambiente.adicionarRobo(ambiente.criarRobo(tipo, categoria, atributos));
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
                        index = scanner.nextInt() ;
                    } while (index < 0 || index > sizeLista);
                    scanner.nextLine(); // Consumir quebra de linha
                    System.out.println();

                    Robo roboSelecionado = listaRobo.get(index);

                    if (roboSelecionado instanceof TanqueGuerra){
                        TanqueGuerra roboEscolhido = (TanqueGuerra) roboSelecionado;
                        menu = String.format(
                            "Digite o número da ação desejada:\n\n" +
                            "1 - Exibir posição\n" +
                            "2 - Atirar\n" +
                            "3 - Recarregar\n" +
                            "4 - Mover\n" +
                            "5 - Identificar obstáculos\n" +
                            "6 - Mudar direção\n"
                        );
                        System.out.print(menu);
                        System.out.print("Ação:");
                        int acao = scanner.nextInt() ;
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1){ // Exibição de posição
                            System.out.println(roboEscolhido.exibirPosicao());

                        } else if (roboEscolhido.getOperando()) {
                            // Verificar operabilidade do robô antes de proceder
                            if (acao == 2) {
                                System.out.print("Digite as coordenadas X e Y do alvo:");
                                int alvoX = scanner.nextInt();
                                int alvoY = scanner.nextInt();
                                System.out.print("Digite o número de tiros: ");
                                int nTiros = scanner.nextInt();
                                System.out.println(roboEscolhido.atirar(alvoX,alvoY,nTiros,ambiente));

                            } else if (acao == 3){
                                // Recarregar balas do Tanque
                                System.out.print("Digite o número de balas recarregadas:");
                                int nBalas = scanner.nextInt();
                                System.out.println(roboEscolhido.recarregar(nBalas));

                            } else if (acao == 4){
                                // Movimentação do Robô por DeltaX e DeltaY
                                System.out.print("Digite as coordenadas X e Y do destino:");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);

                                // Pede a velocidade para não ultrapassar o limite
                                System.out.print("Digite a velocidade do movimento:");
                                int vel = scanner.nextInt();
                                roboEscolhido.mover(coordenadas[0]-roboSelecionado.getPosicaoX(), coordenadas[1]-roboSelecionado.getPosicaoY(), vel);
                        
                                System.out.println(roboEscolhido.exibirPosicao());

                            } else if (acao == 5){
                                // Identifica todos os robôs na direção atual do Tanque
                                ArrayList<Robo> listaRoboVisto = roboEscolhido.identificarObstaculo(ambiente, roboEscolhido.getDirecao());
                                if (listaRoboVisto.size() == 0){
                                    System.out.println("Não há obstáculos\n");
                                }
                                else{
                                    for (Robo robo:listaRoboVisto){
                                        System.out.println(robo.getNome() + " X:" + robo.getPosicaoX() + " Y:" + robo.getPosicaoY());
                                    }
                                }
                            } else if (acao == 6){
                                System.out.print("Digite a direção desejada (Norte/Sul/Leste/Oeste):");
                                String direcao = scanner.nextLine();
                                roboEscolhido.setDirecao(direcao);
                            }
                        } else{
                            System.out.println("O robô está inoperante\n");
                        }
                    } else if (roboSelecionado instanceof Correios){
                        Correios roboEscolhido = ((Correios) roboSelecionado);
                        menu = String.format(
                            "Digite o número da ação desejada:\n\n" +
                            "1 - Exibir posição\n" +
                            "2 - Carregar pacote\n" +
                            "3 - Entregar pacote\n" +
                            "4 - Listar entregas\n" +
                            "5 - Mover\n" +
                            "6 - Identificar obstáculos\n" +
                            "7 - Mudar direção\n"
                        );
                        System.out.print(menu);
                        System.out.print("Ação:");
                        int acao = scanner.nextInt() ;
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1) {
                            System.out.println(roboEscolhido.exibirPosicao());

                        } else if (roboEscolhido.getOperando()){
                            // Verifica operabilidade do robô
                            if (acao == 2) {
                                // Carregar pacote
                                System.out.print("Digite o nome ou id do pacote:");
                                String id = scanner.nextLine();
                                System.out.print("Digite o peso do pacote:");
                                int peso = scanner.nextInt();
                                System.out.println(roboEscolhido.carregarPacote(id, peso));

                            } else if (acao == 3){
                                // Entregar um pacote
                                System.out.print("Digite o nome ou id do pacote:");
                                String id = scanner.nextLine();

                                System.out.print("Digite as coordenadas X e Y do destino:");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                                System.out.println(roboEscolhido.entregarPacote(id, coordenadas[0], coordenadas[1]));

                            } else if (acao == 4){
                                // Listar todas as entregas
                                System.out.println(roboEscolhido.listarEntregas());

                            } else if (acao == 5) {
                                // Mover robô para um destino
                                System.out.print("Digite as coordenadas X e Y do destino:");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);

                                System.out.print("Digite a velocidade do movimento:");
                                int vel = scanner.nextInt();
                                roboEscolhido.mover(coordenadas[0]-roboSelecionado.getPosicaoX(), coordenadas[1]-roboSelecionado.getPosicaoY(),vel);

                            } else if (acao == 6){
                                // Identificar obstáculos na direção definida

                                System.out.println(roboEscolhido.exibirPosicao());
                                ArrayList<Robo> listaRoboVisto= roboEscolhido.identificarObstaculo(ambiente, roboEscolhido.getDirecao());
                                if (listaRoboVisto.size() == 0){
                                    System.out.println("Não há obstáculos");

                                } else {
                                    // Apenas o primeiro obstáculos é visto pelo robô Correio
                                    Robo obstaculo = listaRoboVisto.get(0);
                                    String info_obstaculo = String.format(
                                        "Obstáculo encontrado! Dados:\n\n" + 
                                        "Nome: %s\n" + 
                                        "X: %d\n" + "Y: %d\n", obstaculo.getNome(), obstaculo.getPosicaoX(), obstaculo.getPosicaoY()
                                    );
                                    System.out.println(info_obstaculo);
                                }
                            } else if (acao == 7){
                                // Mudança de direção do robô
                                System.out.println("Digite a direção desejada (Norte/Sul/Leste/Oeste)");
                                String direcao = scanner.nextLine();
                                roboEscolhido.setDirecao(direcao);
                            }
                        } else{
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
                            "5 - Mudar direção\n"
                        );
                        System.out.print(menu);
                        System.out.print("Ação:");
                        int acao = scanner.nextInt() ;
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1) {
                            System.out.println(roboEscolhido.exibirPosicao());

                        } else if (roboEscolhido.getOperando()){
                            // Verifica operabilidade do robô
                            if (acao == 2) {
                                // Executar tiro em posição
                                System.out.print("Digite as coordenadas X e Y do alvo:");
                                int alvoX = scanner.nextInt();
                                int alvoY = scanner.nextInt();
                                System.out.print("Digite o número de tiros: ");
                                int nTiros = scanner.nextInt();
                                System.out.println(roboEscolhido.atirar(alvoX,alvoY,nTiros,ambiente));


                            } else if (acao == 3){
                                // Se movimentar

                                System.out.print("Digite as coordenadas X e Y do destino:");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                                System.out.print("Digite a altura do destino:");
                                int nAlt = scanner.nextInt();

                                roboEscolhido.mover(coordenadas[0], coordenadas[1], nAlt);
                        
                                System.out.println(roboEscolhido.exibirPosicao());


                            } else if (acao == 4){
                                // Identificar todos os obstáculos
                                ArrayList<Robo> listaRoboVisto = roboEscolhido.identificarObstaculo(ambiente, roboEscolhido.getDirecao());
                                if (listaRoboVisto.size() == 0){
                                    System.out.println("Não há obstáculos");

                                } else {
                                    // Apenas o primeiro obstáculos é visto pelo Drone de Ataque
                                    RoboAereo obstaculo = (RoboAereo) listaRoboVisto.get(0);
                                    String info_obstaculo = String.format(
                                        "Obstáculo encontrado! Dados:\n\n" + 
                                        "Nome: %s\n" + 
                                        "X: %d\n" + "Y: %d\n" + "Altura: %d\n", obstaculo.getNome(), obstaculo.getPosicaoX(), obstaculo.getPosicaoY(),  obstaculo.getAltitude()
                                    );
                                    System.out.println(info_obstaculo);
                                }


                            } else if (acao == 5){
                                // Mudança de direção do robô
                                System.out.println("Digite a direção desejada (Norte/Sul/Leste/Oeste)");
                                String direcao = scanner.nextLine();
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
                            "5 - Varrer Área\n"
                        );

                        if (roboEscolhido.isCamuflado()) {
                            menu = String.join(
                                menu,
                                "6 - Desativar Camuflagem"
                            );
                        } else {
                            menu = String.join(
                                menu,
                                "6 - Ativar Camuflagem"
                            );
                        }

                        System.out.print(menu);
                        System.out.print("Ação:");
                        int acao = scanner.nextInt() ;
                        scanner.nextLine(); // Consumir quebra de linha
                        System.out.println();

                        if (acao == 1) { // Exibir posição do Drone
                            System.out.println(roboEscolhido.exibirPosicao());
                        } if (roboEscolhido.getOperando()) {

                            if (acao == 2) { // Movimentar Drone

                                System.out.print("Digite as coordenadas X e Y do destino:");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);
                                System.out.print("Digite a altura do destino:");
                                int nAlt = scanner.nextInt();

                                roboEscolhido.mover(coordenadas[0], coordenadas[1], nAlt);
        
                                System.out.println(roboEscolhido.exibirPosicao());


                            } else if (acao == 3) { // Identificar obstáculos do Drone
                                // Identificar todos os obstáculos
                                ArrayList<Robo> robosAlcanceRadar = roboEscolhido.identificarObstaculo(ambiente);
                                if (robosAlcanceRadar.size() == 0){
                                    System.out.println("Não há obstáculos");

                                } else {
                                    // Todos os obstáculos detectados são exibidos!
                                    System.out.println("Obstáculo Encontrado!Dados:\n\n");
                                    String info_obstaculo;
                                    for (Robo robo : robosAlcanceRadar) {
                                        if (robo instanceof RoboAereo) {
                                            info_obstaculo = String.format(
                                                "Nome: %s\n" + 
                                                "X: %d\n" + "Y: %d\n" + "Altura: %d\n", robo.getNome(), robo.getPosicaoX(), robo.getPosicaoY(),  ((RoboAereo) robo).getAltitude()
                                            );
                                        } else if (robo instanceof RoboTerrestre) {
                                            info_obstaculo = String.format(
                                                "Nome: %s\n" + 
                                                "X: %d\n" + "Y: %d\n", robo.getNome(), robo.getPosicaoX(), robo.getPosicaoY()
                                            );
                                        } else {
                                            info_obstaculo = "Robô de tipo não definido!";
                                        }
                                        System.out.println(info_obstaculo);
                                    }
                                }


                            } else if (acao == 4) { // Mudar direção do Drone
                                System.out.println("Digite a direção desejada (Norte/Sul/Leste/Oeste)");
                                String direcao = scanner.nextLine();
                                roboEscolhido.setDirecao(direcao);

                            } else if (acao == 5) { // Varrer Área
                                System.out.println("Digite as coordenadas do centro de varredura");
                                int[] coordenadas = Main.obterPosicao(ambiente, scanner);

                                System.out.print("Digite o raio da varredura:");
                                int raio = scanner.nextInt();

                                ArrayList<Robo> robos_encontrados = roboEscolhido.varrerArea(ambiente, coordenadas[0], coordenadas[1], raio);

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
                                                "X: %d\n" + "Y: %d\n" + "Altura: %d\n", robo.getNome(), robo.getPosicaoX(), robo.getPosicaoY(),  ((RoboAereo) robo).getAltitude()
                                            );
                                        } else if (robo instanceof RoboTerrestre) {
                                            info_obstaculo = String.format(
                                                "Nome: %s (RoboTerrestre)\n" +
                                                "TIpo: Robô Terrestre\n" +
                                                "X: %d\n" + "Y: %d\n", robo.getNome(), robo.getPosicaoX(), robo.getPosicaoY()
                                            );
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
                    
                } else if (comando == 3){
                    // Exibe lista de robôs no ambiente
                    System.out.println("Lista de robôs:");
                    int i = 1;
                    for (Robo robo : listaRobo){
                        System.out.printf("%d - %s %n", i++, robo.getNome());
                    }
                } else if (comando == 4){
                    // Remoção de um robô do ambiente

                    System.out.println("Escolha o Robô:");
                    int i = 1;
                    for (Robo robo : listaRobo){
                        System.out.printf("%d - %s %n", i++, robo.getNome());
                    }
                    int index = scanner.nextInt() - 1;
                    if (index >= 0 && index < listaRobo.size()){
                        listaRobo.remove(index);
                        System.out.println("Robo removido!\n");
                    }
                }
            }
        }
        scanner.close();
    }

    public static int[] obterPosicao(Ambiente ambiente, Scanner scanner) {
        // Função para obter posição já com a validação do limite

        System.out.print("Posição X:");
        int destinoX = scanner.nextInt();

        System.out.print("Posição Y:");
        int destinoY = scanner.nextInt();

        while(!ambiente.dentroDosLimites(destinoX, destinoY)){
            System.out.println("Coordenadas fora dos limites, digite novas coordenadas");

            System.out.print("Posição X:");
            destinoX = scanner.nextInt();

            System.out.print("Posição Y:");
            destinoY = scanner.nextInt();
        }
        System.out.println();
        return new int[]{destinoX, destinoY};
    }
}