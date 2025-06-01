package interfaces;

import excecoes.sensor.SensorException;

public interface Entidade {
    int getX() throws SensorException;
    int getY() throws SensorException;
    int getZ() throws SensorException;
    int getXInterno();
    int getYInterno();
    int getZInterno();
    TipoEntidade getTipo();
    String getDescricao() throws SensorException;
    String getRepresentacao();
}
