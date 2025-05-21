#!/bin/bash
# filepath: /home/allanrpereira/Documentos/Unicamp/MC322/Allan_Giovana_MC322/Lab03/test.sh

# Limpa a pasta de saída e a recria
rm -rf out
mkdir -p out

# Compila todos os arquivos .java, preservando a estrutura de pacotes
# find . -name "*.java" -not -path "./out/*" | xargs javac -d out
find . -name "*.java" -not -path "./out/*" -not -name "Main.java" | xargs javac -d out

echo "===== Executando testes do simulador de robôs ====="

# Teste da Classe Ambiente
echo -e "\n\033[1;34m>> Teste da Classe Ambiente\033[0m"
java -cp out TestAmbiente

# Teste da Classe Robo
echo -e "\n\033[1;34m>> Teste da Classe Robo\033[0m"
java -cp out TestRobo

# Teste da Classe RoboAereo
echo -e "\n\033[1;34m>> Teste da Classe RoboAereo\033[0m"
java -cp out TestRoboAereo

# # Teste da Classe RoboTerrestre
echo -e "\n\033[1;34m>> Teste da Classe RoboTerrestre\033[0m"
java -cp out TestRoboTerrestre

echo -e "\n\033[1;32m===== Todos os testes foram executados =====\033[0m"