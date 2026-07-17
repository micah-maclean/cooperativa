package com.micahmaclean.cooperativa.exception;

import java.util.UUID;

public class VotoEmSessaoFechadaException extends RuntimeException {
    public VotoEmSessaoFechadaException(UUID sessaoId) {
        super("Sessão de votação encerrada: " + sessaoId);
    }
}
