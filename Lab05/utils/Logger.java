package utils;
import java.io.FileWriter;
import java.util.Formatter;

import excecoes.logger.ArquivoInvalidoException;
import excecoes.logger.FalhaEscritaLogException;

import java.util.Date;


public class Logger {
    int ativo;
    Formatter logFile;
    Date dataAtual;

    public Logger(String nomeLog) throws ArquivoInvalidoException {
        this.ativo = 1;

        try {
            this.logFile = new Formatter(new FileWriter(nomeLog));
            this.dataAtual = new Date();
        } catch (Exception e) {
            throw new ArquivoInvalidoException("Não foi possível abrir o log: " + e.getMessage());
        }
    }

    protected void escreverLog(String mensagem, String tipoMensagem) throws FalhaEscritaLogException {
        try {
            if (this.ativo == 1) this.logFile.format("[%s] %s %s\n", tipoMensagem, this.dataAtual, mensagem);
        } catch (Exception e) {
            throw new FalhaEscritaLogException();
        }
    }

    public void ativarLogger() {
        this.ativo = 1;
    }

    public void desativarLogger() {
        this.ativo = 0;
    }

    public void escreverLogAviso(String mensagem) throws FalhaEscritaLogException {
        this.escreverLog(mensagem, "WARNING");
    }

    public void escreverLogFalha(String mensagem) throws FalhaEscritaLogException {
        this.escreverLog(mensagem, "ERROR");
    }

    public void escreverLogSucesso(String mensagem) throws FalhaEscritaLogException {
        this.escreverLog(mensagem, "SUCCESS");
    }

    public void escreverLogInfo(String mensagem) throws FalhaEscritaLogException {
        this.escreverLog(mensagem, "INFO");
    }

    public void fecharLog() {
        this.logFile.close();
    }

}
