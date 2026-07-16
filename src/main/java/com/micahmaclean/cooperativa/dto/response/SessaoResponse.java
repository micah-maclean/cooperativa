package com.micahmaclean.cooperativa.dto.response;

import com.micahmaclean.cooperativa.model.Sessao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessaoResponse(
        @Schema(description = "Identificador único da sessão") UUID id,
        @Schema(description = "Identificador da pauta") UUID pautaId,
        @Schema(description = "Data e hora de abertura da sessao") LocalDateTime dataAbertura,
        @Schema(description = "Duração da sessão em segundos") Integer duracaoSegundos,
        @Schema(description = "Indica se a sessão ainda está aberta para votação") boolean aberta
) {
    public static SessaoResponse from(Sessao sessao) {
        return new SessaoResponse(
                sessao.getId(),
                sessao.getPauta().getId(),
                sessao.getDataAbertura(),
                sessao.getDuracaoSegundos(),
                sessao.estaAberta()
        );
    }
}
