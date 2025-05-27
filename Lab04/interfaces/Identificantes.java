package interfaces;
import java.util.ArrayList;

import ambiente.Obstaculo;
import robos.geral.*;

public interface Identificantes {
    ArrayList<Obstaculo> identificarObstaculo();
    ArrayList<Robo> identificarRobo();
}
