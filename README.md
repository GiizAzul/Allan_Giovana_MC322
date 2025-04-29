# Robolândia - Sistema de Simulação de Robôs

Este repositório contém um simulador em Java que permite criar e controlar diferentes tipos de robôs em um ambiente tridimensional, com diversos obstáculos, sensores e funcionalidades específicas para cada tipo de robô.

## Pré-requisitos

- **Java 21.0.6**
- **IDE**: VSCode
- **Terminal** compatível com Linux (para os scripts shell)

## Descrição do Repositório
O repositório  é um projeto de Programação Orientada a Objetos que implementa um sistema de simulação de robôs em Java, organizado em três laboratórios até agora que demonstram a evolução do sistema:

- **Lab01**: Implementação inicial com classes básicas para ambiente e robô simples
- **Lab02**: Expansão do sistema com introdução de hierarquia de classes para diferentes tipos de robôs (terrestres e aéreos)
- **Lab03**: Versão atualizada e estruturada com organização em pacotes, sistema de sensores, obstáculos detalhados, testes unitários e diagrama UML

## Como Iniciar
### Executando a Aplicação

1. Clone o repositório ou baixe os arquivos
2. Navegue até a pasta do lab que gostaria de executar
3. Execute o script `start.sh`:

```bash
# Usando o script
./start.sh
```

### Executando os Testes

Para verificar se todas as funcionalidades estão operando corretamente **(Implementados a partir do Lab03)**:

```bash
# Usando o script
./test.sh
```

### Guia de Uso
Cada um dos Lab's possuem um conjunto de funcionalidades que podem ser exploradas após a execução do script `start.sh`. Um lab sempre possui todas as funcionalidades do anterior, podendo haver algumas alterações entre elas, de modo que cada lab é uma crescente de desenvolvimento em relação ao anterior.

### Arquivos de Teste
O repositório inclui testes para verificar o funcionamento adequado dos componentes:
- **TestAmbiente.java**: Testes de criação e manipulação do ambiente
- **TestRobo.java**: Testes das funcionalidades básicas dos robôs
- **TestRoboAereo.java**: Testes específicos para robôs aéreos
- **TestRoboTerrestre.java**: Testes específicos para robôs terrestres
Todos esses arquivos são executados ao executar o arquivo `test.sh`

## Diagrama de Classes
O sistema completo é representado pelo diagrama de classes, mostrando as relações entre as diferentes classes de robôs, sensores, obstáculos e ambiente.
![Imagem do diagrama de classes](./Lab03/UML/Simulador%20de%20Robôs.png)

## Funcionalidades dos Componentes

### Robôs
#### Classe Base - Robô
A classe `Robo` implementa as funcionalidades básicas de movimento e sensoriamento para todos os tipos de robôs, incluindo:
- Nome, direção e posição (X, Y)
- Nível de integridade e velocidade
- Métodos para movimento e detecção de colisões

#### Robôs Aéreos
- **RoboAereo**: Classe abstrata com altitude e sistemas de navegação aérea
- **DroneAtaque**: Equipado com sistema de munição e capacidade ofensiva
- **DroneVigilancia**: Sensores aprimorados para monitoramento

### Materiais dos Robôs
A classe `MateriaisRobo` define diferentes materiais que afetam o fator de redução do alcance do sensor Radar e, portanto, a detecção de obstáculos e robôs pelo sensor
Incluem: Alumínio, Aço, Plástico, Fibra de Vidro, Fibra de Carbono e outros

#### Robôs Terrestres
- **RoboTerrestre**: Classe abstrata com funcionalidades específicas para solo
- **Correios**: Transporte de pacotes e cobrimento de buracos
- **TanqueGuerra**: Blindagem reforçada e armamento pesado

### Sistema de Sensores
#### Radar
Implementação especializada para robôs aéreos com:
- Detecção de obstáculos e outros robôs em raio e ângulo de abertura específicos
- Lista de objetos detectado
- Alcance afetado por fatores de redução do tipo de obstáculo

#### GPS
Compõe todos os tipos de robôs e possui:
- Detecção de coordenadas x, y e z do robo
- Definição de ativação do sensor

#### Colisão
Implementação especializada para robôs terrestres com:
- Detecção de colisão com outros robôs ou obstáculos
- Armazenamento do ultimo robô ou obstáculo colidido

#### Barômetro
Implementação especializada para robôs aéreos com:
- Leitura de pressão atmosférica em função da altitude

### Obstáculos
#### Classe Obstáculo
Objetos que impedem a movimentação dos robôs além de outras interações
- Coordenadas (x1, x2, y1, y2) e altura
- Tipo (`TipoObstaculo`) e nível de integridade
- Obstáculos não indestrutíveis podem sem destruídos com tiros do TanqueGuerra ou DroneAtaque

#### Tipos de Obstáculo
Define altura, integridade, fator de redução do alcançe do sensor radar e indestrutibilidade padrões
- Inclue Parede, Árvore, Prédio, Buraco e outros
- O obstáculo Buraco é indestrutível, além de poder ser coberto pelo Correios e destruir qualquer robô que passe por ele

### Ambiente de Simulação
A classe `Ambiente` gerencia:
- Dimensões do espaço simulado
- Gerenciamento de obstáculos e robôs
- Identificação de objetos em determinadas posições