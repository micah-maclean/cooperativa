package com.micahmaclean.cooperativa.controller.v1;

import com.micahmaclean.cooperativa.dto.request.RegistrarVotoRequest;
import com.micahmaclean.cooperativa.dto.response.ErroResponse;
import com.micahmaclean.cooperativa.dto.response.ResultadoVotacaoResponse;
import com.micahmaclean.cooperativa.exception.SwaggerExemplos;
import com.micahmaclean.cooperativa.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/pautas/{pautaId}/votos")
@Tag(name = "Votos", description = "Registro e contabilização de votos")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @Operation(summary = "Registra um voto em uma sessão")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Voto registrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou sessão encerrada",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_400))),
            @ApiResponse(responseCode = "409", description = "Associado já votou",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_409)))
    })
    @PostMapping
    public ResponseEntity<Void> registrarVoto(@PathVariable UUID pautaId,
                                              @Valid @RequestBody RegistrarVotoRequest request) {
        votoService.registrar(pautaId, request);
        return ResponseEntity.created(null).build();
    }

    @Operation(summary = "Contabiliza e retorna o resultado da votação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultado calculado",
                    content = @Content(schema = @Schema(implementation = ResultadoVotacaoResponse.class)))
    })
    @GetMapping("/resultado")
    public ResponseEntity<ResultadoVotacaoResponse> obterResultado(@PathVariable UUID pautaId) {
        ResultadoVotacaoResponse resultado = votoService.contabilizar(pautaId);
        return ResponseEntity.ok(resultado);
    }
}
