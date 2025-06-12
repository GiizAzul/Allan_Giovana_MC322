package robos.subsistemas.movimento;

import ambiente.Ambiente;
import ambiente.Obstaculo;
import ambiente.TipoObstaculo;
import excecoes.ambiente.ForaDosLimitesException;
import excecoes.logger.LoggerException;
import excecoes.robos.gerais.ColisaoException;
import excecoes.robos.gerais.MovimentoInvalidoException;
import excecoes.robos.gerais.RoboDestruidoPorBuracoException;
import excecoes.robos.gerais.VelocidadeMaximaException;
import excecoes.sensor.SensorException;
import robos.aereos.RoboAereo;
import robos.geral.Robo;
import robos.terrestres.RoboTerrestre;

public class ControleMovimentoTerrestre implements ControleMovimento {

    public void mover(RoboTerrestre robo, int deltaX, int deltaY, int velocidade, Ambiente ambiente)
            throws VelocidadeMaximaException, SensorException, LoggerException, ColisaoException, RoboDestruidoPorBuracoException,
            MovimentoInvalidoException {

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
                    if (robo.getSensorColisao().getUltimoObstaculoColidido()
                            .getTipoObstaculo() == TipoObstaculo.BURACO) {
                        ambiente.moverEntidade(robo, x - passoX, robo.getYInterno(), robo.getZInterno());
                        robo.setPosicaoX(x - passoX); // Corrige a posição do robô
                        ambiente.removerEntidade(robo);
                        throw new ColisaoException("O robô " + robo.getNome() + " caiu no buraco e foi destruido");
                    } else {
                        ambiente.moverEntidade(robo, x - passoX, robo.getYInterno(), robo.getZInterno());
                        robo.setPosicaoX(x - passoX); // Corrige a posição do robô
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o obstáculo: "
                                + robo.getSensorColisao().getUltimoObstaculoColidido().getTipoObstaculo()
                                + " na posição X:" + x
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
                    if (robo.getSensorColisao().getUltimoObstaculoColidido()
                            .getTipoObstaculo() == TipoObstaculo.BURACO) {
                        ambiente.moverEntidade(robo, robo.getXInterno(), y - passoY, robo.getZInterno());
                        robo.setPosicaoY(y - passoY); // Corrige a posição do robô
                        ambiente.removerEntidade(robo);
                        throw new ColisaoException("O robô " + robo.getNome() + " caiu no buraco e foi destruido");
                    } else {
                        ambiente.moverEntidade(robo, robo.getXInterno(), y - passoY, robo.getZInterno());
                        robo.setPosicaoY(y - passoY); // Corrige a posição do robô
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o obstáculo: "
                                + robo.getSensorColisao().getUltimoObstaculoColidido().getTipoObstaculo()
                                + " na posição X:"
                                + posicaoX
                                + " Y:" + y);
                    }
                }
            }
        }
    }

    public void mover(RoboAereo robo, int novoX, int novoY, int novaZ, Ambiente ambiente)
            throws MovimentoInvalidoException {
        // Um robô terrestre não pode executar um movimento aéreo.
        throw new MovimentoInvalidoException("Controle terrestre não pode mover robô aéreo.");
    }

    public void mover(Robo robo, int deltaX, int deltaY, Ambiente ambiente)
            throws ForaDosLimitesException, SensorException, LoggerException, RoboDestruidoPorBuracoException, ColisaoException {

        if (robo.getX() + deltaX < 0 || robo.getY() + deltaY < 0 || robo.getX() + deltaX >= ambiente.getTamX()
                || robo.getY() + deltaY >= ambiente.getTamY()) {
            throw new ForaDosLimitesException(
                    "O robô " + robo.getNome() + " tentou se mover para fora dos limites do ambiente");
        }

        int destinoX = robo.getX() + deltaX;
        int destinoY = robo.getY() + deltaY;

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            for (int x = robo.getX() + passoX; x != destinoX + passoX; x += passoX) {
                Object obj = ambiente.identificarEntidadePosicao(x, robo.getY(), robo.getZ());
                if (obj != null) {
                    if (obj instanceof Obstaculo &&
                            ((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        // Tratamento especial para buraco
                        ambiente.removerEntidade(robo);
                        throw new RoboDestruidoPorBuracoException(
                                "O robô " + robo.getNome() + " caiu em um BURACO na posição X:" + x + " Y:"
                                        + robo.getY() + " e foi destruído");
                    } else {
                        // Para outros obstáculos ou robôs, apenas para o movimento
                        throw new ColisaoException(
                                "O robô " + robo.getNome() + " interrompeu o movimento devido a um objeto na posição X:"
                                        + x + " Y:" + robo.getY());
                    }
                }
                robo.setPosicaoX(x); // Atualiza a posição X antes da colisão
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = robo.getY() + passoY; y != destinoY + passoY; y += passoY) {
                Object obj = ambiente.identificarEntidadePosicao(robo.getX(), y, robo.getZ());

                if (obj != null) {
                    if (obj instanceof Obstaculo &&
                            ((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        // Tratamento especial para buraco
                        ambiente.removerEntidade(robo);
                        throw new RoboDestruidoPorBuracoException(
                                "O robô " + robo.getNome() + " caiu em um BURACO na posição X:" + robo.getX() + " Y:"
                                        + y + " e foi destruído");
                    } else {
                        // Para outros obstáculos ou robôs, apenas para o movimento
                        throw new ColisaoException(
                                "O robô " + robo.getNome() + " interrompeu o movimento devido a um objeto na posição X:"
                                        + robo.getX() + " Y:" + y);
                    }
                }
                ambiente.moverEntidade(robo, robo.getX(), y, robo.getZ());
                robo.setPosicaoY(y);
                ; // Atualiza a posição Y antes da colisão
            }
        }
    }

}
