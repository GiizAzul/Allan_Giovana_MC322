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
                    robo.getLogger().escreverLogFalha(String.format("[MOVIMENTO] %s colidiu na posição (%d, %d, %d)", robo.getNome(), x, robo.getY(), robo.getZ()));

                    if (obj instanceof RoboAereo) {
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o objeto: " +
                                ((RoboAereo) obj).getNome() + " na posição X:" + x + " Y:" + robo.getY() + " Z:"
                                + robo.getZ());
                    } else {
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o objeto:" +
                                ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + x + " Y:" + robo.getY()
                                + " Z:" + robo.getZ());
                    }
                }
                ambiente.moverEntidade(robo, x, robo.getY(), robo.getZ());
                robo.getLogger().escreverLogInfo(String.format("[MOVIMENTO] %s moveu para posição (%d, %d, %d)", robo.getNome(), x, robo.getY(), robo.getZ()));
                robo.setPosicaoX(x);
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = robo.getY() + passoY; y != Y + passoY; y += passoY) {
                Object obj = ambiente.identificarEntidadePosicao(robo.getX(), y, robo.getZ());

                if (obj != null) {
                    robo.getLogger().escreverLogFalha(String.format("[MOVIMENTO] %s colidiu na posição (%d, %d, %d)", robo.getNome(), robo.getX(), y, robo.getZ()));

                    if (obj instanceof RoboAereo) {
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o objeto: " +
                                ((RoboAereo) obj).getNome() + " na posição X:" + robo.getX() + " Y:" + y + " Z:"
                                + robo.getZ());
                    } else {
                        throw new ColisaoException("O robô " + robo.getNome() + " colidiu com o objeto:" +
                                ((Obstaculo) obj).getTipoObstaculo() + " na posição X:" + robo.getX() + " Y:" + y
                                + " Z:" + robo.getZ());
                    }
                }

                robo.getLogger().escreverLogInfo(String.format("[MOVIMENTO] %s moveu para posição (%d, %d, %d)", robo.getNome(), robo.getX(), y, robo.getZ()));

                ambiente.moverEntidade(robo, robo.getX(), y, robo.getZ());
                robo.setPosicaoY(y);
            }
        }

        // Ajusta a altitude final - sobe ou desce conforme necessário
        int h = robo.getZ();
        robo.getLogger().escreverLogInfo(String.format("[MOVIMENTO] %s moveu-se para z = %d", robo.getNome(), Z));
        if (Z > h) {
            robo.subir(Z - h, ambiente);
        } else {
            robo.descer(h - Z, ambiente);
        }

        robo.getLogger().escreverLogSucesso(String.format("[MOVIMENTO] %s moveu-se com sucesso até o destino - (%d, %d, %d)", robo.getNome(), robo.getX(), robo.getY(), robo.getZ()));
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
            robo.getLogger().escreverLogFalha("[MOVIMENTO] Falha no movimento");
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
                    robo.getLogger().escreverLogFalha(String.format("[MOVIMENTO] %s colidiu na posição (%d, %d, %d)", robo.getNome(), x, robo.getY(), robo.getZ()));

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
                robo.getLogger().escreverLogInfo(String.format("[MOVIMENTO] %s moveu para posição (%d, %d, %d)", robo.getNome(), x, robo.getY(), robo.getZ()));
                robo.setPosicaoX(x); // Atualiza a posição X antes da colisão
            }
        }

        // Movimentação em linha reta no eixo Y
        if (deltaY != 0) {
            int passoY = deltaY > 0 ? 1 : -1;
            for (int y = robo.getY() + passoY; y != destinoY + passoY; y += passoY) {
                Object obj = ambiente.identificarEntidadePosicao(robo.getX(), y, robo.getZ());

                if (obj != null) {
                    robo.getLogger().escreverLogFalha(String.format("[MOVIMENTO] %s colidiu na posição (%d, %d, %d)", robo.getNome(), robo.getX(), y, robo.getZ()));

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
                robo.getLogger().escreverLogInfo(String.format("[MOVIMENTO] %s moveu para a posição (%d, %d, %d)", robo.getNome(), robo.getX(), y, robo.getZ()));

                ambiente.moverEntidade(robo, robo.getX(), y, robo.getZ());
                robo.setPosicaoY(y);
                // Atualiza a posição Y antes da colisão
            }
        }
        robo.getLogger().escreverLogSucesso(String.format("[MOVIMENTO] %s moveu-se com sucesso até o destino - (%d, %d, %d)", robo.getNome(), robo.getX(), robo.getY(), robo.getZ()));
    }
}
