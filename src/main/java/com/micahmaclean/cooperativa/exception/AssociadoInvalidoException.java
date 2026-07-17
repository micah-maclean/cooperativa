package com.micahmaclean.cooperativa.exception;

public class AssociadoInvalidoException extends RuntimeException {
    public AssociadoInvalidoException(String associadoId) {
        super("Associado inválido: " + associadoId);
    }
}
