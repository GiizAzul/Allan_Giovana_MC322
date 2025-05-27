package interfaces;

import ambiente.Ambiente;

public interface Atacante {
    String atirar(int alvoX, int alvoY, int alvoZ, int nTiros, Ambiente ambiente);
    String recarregar(int nBalas);
}
