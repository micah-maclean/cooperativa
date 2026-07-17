package com.micahmaclean.cooperativa.dto.request;

import com.micahmaclean.cooperativa.model.Voto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarVotoRequest(
        @Schema(description = "CPF do associado", example = "12345678901")
        @NotBlank(message = "associadoId é obrigatório")
        String associadoId,

        @Schema(description = "Voto: SIM ou NAO", example = "SIM")
        @NotNull(message = "voto é obrigatório")
        Voto.VotoEnum voto
) {
}
