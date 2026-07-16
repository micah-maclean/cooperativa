package com.micahmaclean.cooperativa.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarPautaRequest(
        @Schema(description = "Título da pauta", example = "Reforma do Estatuto Social")
        @NotBlank(message = "titulo é obrigatório")
        @Size(max = 200, message = "titulo deve ter no máximo 200 caracteres")
        String titulo,

        @Schema(description = "Descrição detalhada da pauta", example = "Alteração do artigo 5 do estatuto social")
        @Size(max = 1000, message = "descricao deve ter no máximo 1000 caracteres")
        String descricao
) {
}
