package com.micahmaclean.cooperativa.exception;

import java.util.UUID;

public class SessaoNaoEncontradaException extends RuntimeException {
    public SessaoNaoEncontradaException(UUID pautaId) {
        super("Nenhuma sessão de votação encontrada para a pauta: " + pautaId);
    }
}
