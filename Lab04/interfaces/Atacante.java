package interfaces;

import ambiente.Ambiente;
import excecoes.*;
import excecoes.sensor.SensorInativoException;

public interface Atacante {
    String atirar(int alvoX, int alvoY, int alvoZ, int nTiros, Ambiente ambiente)
    throws SensorInativoException, MunicaoInsuficienteException, AlvoInvalidoException;
    String recarregar(int nBalas);
}
