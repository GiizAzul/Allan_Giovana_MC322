package utils;
import java.io.FileWriter;
import java.util.Formatter;

import excecoes.logger.ArquivoInvalidoException;
import excecoes.logger.FalhaEscritaLogException;

import java.util.Date;


public class Logger {
    Formatter logFile;
    Date dataAtual;

    public Logger(String nomeLog) throws ArquivoInvalidoException {
        try {
            this.logFile = new Formatter(new FileWriter(nomeLog));
            this.dataAtual = new Date();
        } catch (Exception e) {
            throw new ArquivoInvalidoException("Não foi possível abrir o log: " + e.getMessage());
        }
    }

    protected void escreverLog(String mensagem, String tipoMensagem) throws FalhaEscritaLogException {
        try {
            this.logFile.format("[%s] %s %s", tipoMensagem, this.dataAtual, mensagem);
        } catch (Exception e) {
            throw new FalhaEscritaLogException();
        }
    }

    public void escreverLogAviso(String mensagem) throws FalhaEscritaLogException {
        this.escreverLog(mensagem, "WARNING");
    }

    public void escreverLogFalha(String mensagem, String tipoMensagem) throws FalhaEscritaLogException {
        this.escreverLog(mensagem, "ERROR");
    }

    public void escreverLogSucesso(String mensagem, String tipoMensagem) throws FalhaEscritaLogException {
        this.escreverLog(mensagem, "SUCCESS");
    }
    public void fecharLog() {
        this.logFile.close();
    }

}
