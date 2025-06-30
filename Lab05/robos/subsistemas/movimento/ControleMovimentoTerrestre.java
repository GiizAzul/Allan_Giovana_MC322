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
            robo.getLogger().escreverLogFalha("[MOVIMENTO] Não foi possível realizar o movimento!");
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
                robo.getLogger().escreverLogInfo(String.format("[MOVIMENTO] %s moveu para posição (%d, %d, %d)", robo.getNome(), x, robo.getYInterno(), robo.getZInterno()));
                detectado = robo.getSensorColisao().acionar();
                if (detectado == 1) {
                    
                    ambiente.moverEntidade(robo, x - passoX, robo.getYInterno(), robo.getZInterno());
                    robo.setPosicaoX(x - passoX); // Corrige a posição do robô
                    robo.getLogger().escreverLogFalha(String.format("[MOVIMENTO] %s colidiu na posição (%d, %d, %d)", robo.getNome(), x, robo.getYInterno(), robo.getZInterno()));
                    throw new ColisaoException("O robô " + robo.getNome() + " colidiu com outro robô na posição X:" + x
                            + " Y:" + robo.getYInterno());
                } else if (detectado == 2) {
                    robo.getLogger().escreverLogFalha(String.format("[MOVIMENTO] %s colidiu na posição (%d, %d, %d)", robo.getNome(), x, robo.getYInterno(), robo.getZInterno()));

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
                                + robo.getYInterno());
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
                robo.getLogger().escreverLogInfo(String.format("[MOVIMENTO] %s moveu para posição (%d, %d, %d)", robo.getNome(), robo.getXInterno(), y, robo.getZInterno()));
                detectado = robo.getSensorColisao().acionar();
                if (detectado == 1) {
                    ambiente.moverEntidade(robo, robo.getXInterno(), y - passoY, robo.getZInterno());
                    robo.setPosicaoY(y - passoY); // Corrige a posição do robô
                    robo.getLogger().escreverLogFalha(String.format("[MOVIMENTO] %s colidiu na posição (%d, %d, %d)", robo.getNome(), robo.getXInterno(), y, robo.getZInterno()));
                    throw new ColisaoException("O robô " + robo.getNome() + " colidiu com outro robô na posição X:"
                            + robo.getXInterno() + " Y:" + y);
                } else if (detectado == 2) {
                    
                    robo.getLogger().escreverLogFalha(String.format("[MOVIMENTO] %s colidiu na posição (%d, %d, %d)", robo.getNome(), robo.getXInterno(), y, robo.getZInterno()));
                    
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
                                + robo.getXInterno()
                                + " Y:" + y);
                    }
                }
            }
        }
        robo.getLogger().escreverLogSucesso(String.format("[MOVIMENTO] %s moveu-se com sucesso até o destino - (%d, %d, %d)", robo.getNome(), robo.getXInterno(), robo.getYInterno(), robo.getZInterno()));
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
                ; // Atualiza a posição Y antes da colisão
            }
        }
        robo.getLogger().escreverLogSucesso(String.format("[MOVIMENTO] %s moveu-se com sucesso até o destino - (%d, %d, %d)", robo.getNome(), robo.getX(), robo.getY(), robo.getZ()));
    }

}
