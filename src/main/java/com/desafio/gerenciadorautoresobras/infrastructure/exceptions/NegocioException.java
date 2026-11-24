package com.desafio.gerenciadorautoresobras.infrastructure.exceptions;

public class NegocioException extends RuntimeException {
    public NegocioException(String mensagem) {
        super(mensagem);
    }
    public NegocioException(String mensagem,Throwable throwable) {
        super(mensagem,throwable);
    }
}
