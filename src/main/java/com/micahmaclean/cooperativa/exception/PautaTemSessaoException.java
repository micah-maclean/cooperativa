package com.micahmaclean.cooperativa.exception;

public class PautaTemSessaoException extends RuntimeException {
    public PautaTemSessaoException() {
        super("Não é possível editar a pauta pois ela já possui uma sessão de votação.");
    }
}
