package com.micahmaclean.cooperativa.dto.response;

import com.micahmaclean.cooperativa.model.Pauta;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record PautaResponse(
        @Schema(description = "Identificador único da pauta") UUID id,
        @Schema(description = "Título da pauta") String titulo,
        @Schema(description = "Título da pauta") String descricao,
        @Schema(description = "Data e hora de criação da pauta") LocalDateTime dataCriacao
) {
    public static PautaResponse from(Pauta pauta) {
        return new PautaResponse(
                pauta.getId(),
                pauta.getTitulo(),
                pauta.getDescricao(),
                pauta.getDataCriacao()
        );
    }
}
