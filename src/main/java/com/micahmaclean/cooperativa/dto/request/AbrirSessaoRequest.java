package com.micahmaclean.cooperativa.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;


public record AbrirSessaoRequest(
        @Schema(description = "Duração da sessão em segundos. Se não informado, usa 60 segundos por padrão.", example = "120")
        @Positive(message = "duracaoSegundos deve ser maior que zero")
        Integer duracaoSegundos
) {
}
