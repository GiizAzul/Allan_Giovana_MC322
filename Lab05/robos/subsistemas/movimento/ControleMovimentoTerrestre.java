package robos.subsistemas.movimento;

import ambiente.Ambiente;
import ambiente.TipoObstaculo;
import excecoes.robos.gerais.ColisaoException;
import excecoes.robos.gerais.MovimentoInvalidoException;
import excecoes.robos.gerais.RoboDestruidoPorBuracoException;
import excecoes.robos.gerais.VelocidadeMaximaException;
import excecoes.sensor.SensorException;
import robos.aereos.RoboAereo;
import robos.terrestres.RoboTerrestre;

public class ControleMovimentoTerrestre implements ControleMovimento {

    public void mover(RoboTerrestre robo, int deltaX, int deltaY, int velocidade, Ambiente ambiente)
            throws VelocidadeMaximaException, SensorException, ColisaoException, RoboDestruidoPorBuracoException, MovimentoInvalidoException {

        if (velocidade > robo.getVelocidadeMaxima()) {
            throw new VelocidadeMaximaException();
        }

        // Robo Terrestre não precisa do GPS para se movimentar (v*t)
        int posicaoX = robo.getXInterno();
        int posicaoY = robo.getYInterno();

        // Verifica se o robô está dentro dos limites do ambiente
        int destinoX = posicaoX + deltaX >= ambiente.getTamX() ? ambiente.getTamX() - 1 : posicaoX + deltaX;
        int destinoY = posicaoY + deltaY >= ambiente.getTamY() ? ambiente.getTamY() - 1 : posicaoY + deltaY;

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            int x = 0;
            int detectado = 0;
            for (x = posicaoX + passoX; x != destinoX + passoX; x += passoX) {
                ambiente.moverEntidade(robo, x, robo.getYInterno(), robo.getZInterno());
                robo.setPosicaoX(x);
                detectado = robo.getSensorColisao().acionar();
                if (detectado == 1) {
                    ambiente.moverEntidade(robo, x - passoX, robo.getYInterno(), robo.getZInterno());
                    robo.setPosicaoX(x - passoX); // Corrige a posição do robô
                    throw new ColisaoException("O robô " + robo.getNome() + " colidiu com outro robô na posição X:" + x
                            + " Y:" + posicaoY);
                } else if (detectado == 2) {
                    if (robo.getSensorColisao().getUltimoObstaculoColidido().getTipoObstaculo() == TipoObstaculo.BURACO) {
                        ambiente.moverEntidade(robo, x - passoX, robo.getYInterno(), robo.getZInterno());
                        robo.setPosicaoX(x - passoX); // Corrige a posição do robô
                        ambiente.removerEntidade(robo);
                        throw new ColisaoException("O robô " + robo.getNome() + " caiu no buraco e foi destruido");
                    } else {
                        ambiente.moverEntidade(robo, x - passoX, robo.getYInterno(), robo.getZInterno());
                        robo.setPosicaoX(x - passoX); // Corrige a posição do robô
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o obstáculo: "
                                + robo.getSensorColisao().getUltimoObstaculoColidido().getTipoObstaculo() + " na posição X:" + x
                                + " Y:"
                                + posicaoY);
                    }
                }
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            int y = 0, detectado = 0;
            for (y = posicaoY + passoY; y != destinoY + passoY; y += passoY) {
                ambiente.moverEntidade(robo, robo.getXInterno(), y, robo.getZInterno());
                robo.setPosicaoY(y);
                detectado = robo.getSensorColisao().acionar();
                if (detectado == 1) {
                    ambiente.moverEntidade(robo, robo.getXInterno(), y - passoY, robo.getZInterno());
                    robo.setPosicaoY(y - passoY); // Corrige a posição do robô
                    throw new ColisaoException("O robô " + robo.getNome() + " colidiu com outro robô na posição X:"
                            + posicaoX + " Y:" + y);
                } else if (detectado == 2) {
                    if (robo.getSensorColisao().getUltimoObstaculoColidido().getTipoObstaculo() == TipoObstaculo.BURACO) {
                        ambiente.moverEntidade(robo, robo.getXInterno(), y - passoY, robo.getZInterno());
                        robo.setPosicaoY(y - passoY); // Corrige a posição do robô
                        ambiente.removerEntidade(robo);
                        throw new ColisaoException("O robô " + robo.getNome() + " caiu no buraco e foi destruido");
                    } else {
                        ambiente.moverEntidade(robo, robo.getXInterno(), y - passoY, robo.getZInterno());
                        robo.setPosicaoY(y - passoY); // Corrige a posição do robô
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o obstáculo: "
                                + robo.getSensorColisao().getUltimoObstaculoColidido().getTipoObstaculo() + " na posição X:"
                                + posicaoX
                                + " Y:" + y);
                    }
                }
            }
        }
    }

    public void mover(RoboAereo robo, int novoX, int novoY, int novaZ, Ambiente ambiente) throws MovimentoInvalidoException {
        // Um robô terrestre não pode executar um movimento aéreo.
        throw new MovimentoInvalidoException("Controle terrestre não pode mover robô aéreo.");
    }

}
