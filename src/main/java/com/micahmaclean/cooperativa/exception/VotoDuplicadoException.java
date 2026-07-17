package com.micahmaclean.cooperativa.exception;

import java.util.UUID;

public class VotoDuplicadoException extends RuntimeException {
    public VotoDuplicadoException(UUID pautaId, String associadoId) {
        super("Associado " + associadoId + " já votou nesta pauta: " + pautaId);
    }
}
