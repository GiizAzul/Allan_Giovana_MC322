package interfaces;

import ambiente.Ambiente;
import excecoes.robos.especificos.AlvoInvalidoException;
import excecoes.robos.especificos.MunicaoInsuficienteException;
import excecoes.sensor.SensorException;

public interface Atacante {
    String atirar(int alvoX, int alvoY, int alvoZ, int nTiros, Ambiente ambiente)
    throws SensorException, MunicaoInsuficienteException, AlvoInvalidoException;
    String recarregar(int nBalas);
}
