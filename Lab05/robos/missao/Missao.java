package robos.missao;

import ambiente.Ambiente;
import robos.geral.Robo;
import utils.Logger;

public interface Missao {
    String executar (Robo r , Ambiente a, Logger logger) ;
}
