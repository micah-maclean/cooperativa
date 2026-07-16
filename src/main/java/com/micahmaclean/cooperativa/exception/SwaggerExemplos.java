package com.micahmaclean.cooperativa.exception;

public final class SwaggerExemplos {
    public static final String ERRO_400 = """
            {
              "timestamp": "2026-07-16T12:38:05.948Z",
              "status": 400,
              "erro": "Bad Request",
              "mensagem": "titulo: titulo é obrigatório"
            }
            """;

    public static final String ERRO_404 = """
            {
              "timestamp": "2026-07-16T12:38:05.948Z",
              "status": 404,
              "erro": "Not Found",
              "mensagem": "Entidade não encontrada: 3fa85f64-5717-4562-b3fc-2c963f66afa6"
            }
            """;

    public static final String ERRO_409 = """
            {
              "timestamp": "2026-07-16T12:38:05.948Z",
              "status": 409,
              "erro": "Conflict",
              "mensagem": "O estado atual deste recurso não permite a alteração solicitada."
            }
            """;
}
