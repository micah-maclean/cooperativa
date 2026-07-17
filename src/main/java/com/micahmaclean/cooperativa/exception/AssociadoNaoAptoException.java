package com.micahmaclean.cooperativa.exception;

public class AssociadoNaoAptoException extends RuntimeException {
    public AssociadoNaoAptoException(String associadoId) {
        super("Associado não está apto para votar: " + associadoId);
    }
}
