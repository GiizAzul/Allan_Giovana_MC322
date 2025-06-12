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

public class ControleMovimentoAereo implements ControleMovimento {
    public void mover(RoboAereo robo, int X, int Y, int Z, Ambiente ambiente)
            throws SensorException, LoggerException, ColisaoException, MovimentoInvalidoException {
        int deltaX = X - robo.getX();
        int deltaY = Y - robo.getY();

        // Movimentação em linha reta no eixo X
        if (deltaX != 0) {
            int passoX = deltaX > 0 ? 1 : -1;
            for (int x = robo.getX() + passoX; x != X + passoX; x += passoX) {
                Object obj = ambiente.identificarEntidadePosicao(x, robo.getY(), robo.getZ());
                if (obj != null) {
                    if (obj instanceof RoboAereo) {
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o objeto: " +
                                ((RoboAereo) obj).getNome() + " na posição X:" + x + " Y:" + robo.getY() + " Z:"
                                + robo.getZ());
                    } else if (((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        ambiente.removerEntidade(robo);
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o objeto: "
                                + ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + x + " Y:" + robo.getY()
                                + "sendo destruído no processo!");
                    } else {
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o objeto:" +
                                ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + x + " Y:" + robo.getY()
                                + " Z:" + robo.getZ());
                    }
                }
                ambiente.moverEntidade(robo, x, robo.getY(), robo.getZ());
                robo.setPosicaoX(x);
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = robo.getY() + passoY; y != Y + passoY; y += passoY) {
                Object obj = ambiente.identificarEntidadePosicao(robo.getX(), y, robo.getZ());

                if (obj != null) {
                    if (obj instanceof RoboAereo) {
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o objeto: " +
                                ((RoboAereo) obj).getNome() + " na posição X:" + robo.getX() + " Y:" + y + " Z:"
                                + robo.getZ());
                    } else if (((Obstaculo) obj).getTipoObstaculo() == TipoObstaculo.BURACO) {
                        ambiente.removerEntidade(robo);
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o objeto: "
                                + ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + robo.getX() + " Y:" + y
                                + "sendo destruído no processo!");
                    } else {
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o objeto:" +
                                ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + robo.getX() + " Y:" + y
                                + " Z:" + robo.getZ());
                    }
                }

                ambiente.moverEntidade(robo, robo.getX(), y, robo.getZ());
                robo.setPosicaoY(y);
            }
        }

        // Ajusta a altitude final - sobe ou desce conforme necessário
        int h = robo.getZ();
        if (Z > h) {
            robo.subir(Z - h, ambiente);
        } else {
            robo.descer(h - Z, ambiente);
        }
    }

    public void mover(RoboTerrestre robo, int deltaX, int deltaY, int velocidade, Ambiente ambiente)
            throws VelocidadeMaximaException, SensorException, ColisaoException, RoboDestruidoPorBuracoException,
            MovimentoInvalidoException {
        throw new MovimentoInvalidoException("Controle aéreo não pode mover robô terrestre.");
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
