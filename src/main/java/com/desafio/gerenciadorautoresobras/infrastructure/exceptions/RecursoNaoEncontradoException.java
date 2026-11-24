package com.desafio.gerenciadorautoresobras.infrastructure.exceptions;

public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
    public RecursoNaoEncontradoException(String mensagem, Throwable throwable) {
        super(mensagem,throwable);
    }
}
