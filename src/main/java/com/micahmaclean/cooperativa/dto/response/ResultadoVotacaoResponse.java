package com.micahmaclean.cooperativa.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResultadoVotacaoResponse(
        @Schema(description = "Número de votos SIM") long sim,
        @Schema(description = "Número de votos NAO") long nao,
        @Schema(description = "Votação aprovada (SIM > NAO)") boolean aprovada
) {
    public static ResultadoVotacaoResponse from(long sim, long nao) {
        return new ResultadoVotacaoResponse(sim, nao, sim > nao);
    }
}
