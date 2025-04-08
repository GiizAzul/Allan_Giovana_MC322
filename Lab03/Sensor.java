abstract class Sensor<T> {
    private int raio;

    Sensor(int raio) { // constructor que inicializa o raio do sensor
        this.raio = raio;
    }

    int getRaio() { // método que retorna o raio do sensor
        return raio;
    }

    abstract T monitorar();
}
