package com.micahmaclean.cooperativa.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EditarSessaoRequest(
        @Schema(description = "Duração da sessão em segundos", example = "60")
        @NotNull(message = "duracaoSegundos é obrigatório")
        @Positive(message = "duracaoSegundos deve ser maior que zero")
        Integer duracaoSegundos
) {
}
