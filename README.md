# Simulador de Robôs 3D

Programa Java que simula um ambiente 3D com diferentes tipos de robôs, cada um com capacidades e funcionalidades específicas.

## Funcionalidades

- Criação de um ambiente 3D com dimensões personalizadas
- Adição de diferentes tipos de robôs:
  - **Robôs Terrestres**:
    - Tanque de Guerra: robô militar com capacidade de ataque
    - Correios: robô de entrega de pacotes
  - **Robôs Aéreos**:
    - Drone de Ataque: drone militar com capacidade de ataque
    - Drone de Vigilância: drone com capacidades avançadas de detecção e camuflagem

- Operações específicas para cada tipo de robô
- Interface de menu interativa

## Como usar

1. Execute o programa
2. Defina as dimensões do ambiente (X Y Z)
3. Use o menu principal para:
   - Criar novos robôs
   - Realizar ações com robôs existentes
   - Listar todos os robôs
   - Remover robôs

## Tipos de Robôs e suas Ações

### Tanque de Guerra (Terrestre)
- Atirar em alvos
- Recarregar munição
- Mover-se pelo ambiente
- Identificar obstáculos na direção atual
- Mudar direção

### Correios (Terrestre)
- Carregar pacotes
- Entregar pacotes em locais específicos
- Listar entregas
- Mover-se pelo ambiente
- Identificar obstáculos
- Mudar direção

### Drone de Ataque (Aéreo)
- Atirar em alvos
- Mover-se (incluindo controle de altitude)
- Identificar obstáculos
- Mudar direção

### Drone de Vigilância (Aéreo)
- Mover-se (incluindo controle de altitude)
- Identificar obstáculos em todas as direções
- Mudar direção
- Varrer área circular
- Ativar/desativar camuflagem