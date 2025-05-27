package interfaces;

import excecoes.sensor.SensorInativoException;

public interface Entidade {
    int getX() throws SensorInativoException;
    int getY() throws SensorInativoException;
    int getZ() throws SensorInativoException;
    int getXInterno();
    int getYInterno();
    int getZInterno();
    TipoEntidade getTipo();
    String getDescricao();
    char getRepresentacao();
}
