package com.micahmaclean.cooperativa.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ErroResponse(
        @Schema(description = "Data e hora do erro", example = "2026-07-16T12:38:05.948Z") LocalDateTime timestamp,
        @Schema(description = "Código HTTP do erro", example = "404") int status,
        @Schema(description = "Nome do erro HTTP", example = "Not Found") String erro,
        @Schema(description = "Mensagem detalhando o erro", example = "Pauta não encontrada: 3fa85f64-5717-4562-b3fc-2c963f66afa6") String mensagem
) {
}
