package com.micahmaclean.cooperativa.controller.v1;

import com.micahmaclean.cooperativa.dto.request.CriarPautaRequest;
import com.micahmaclean.cooperativa.dto.request.EditarPautaRequest;
import com.micahmaclean.cooperativa.dto.response.ErroResponse;
import com.micahmaclean.cooperativa.dto.response.PautaResponse;
import com.micahmaclean.cooperativa.exception.SwaggerExemplos;
import com.micahmaclean.cooperativa.model.Pauta;
import com.micahmaclean.cooperativa.service.PautaService;
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
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/pautas")
@Tag(name = "Pautas", description = "Cadastro e consulta de pautas de votação")
public class PautaController {

    private final PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @Operation(summary = "Cadastra uma nova pauta", description = "Cria uma pauta para futura votação em assembleia.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso",
                    content = @Content(schema = @Schema(implementation = PautaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_400)
                    )),
    })
    @PostMapping
    public ResponseEntity<PautaResponse> criar(@Valid @RequestBody CriarPautaRequest request) {
        Pauta pauta = pautaService.criar(request);
        PautaResponse response = PautaResponse.from(pauta);
        return ResponseEntity.created(URI.create("/v1/pautas/" + pauta.getId())).body(response);
    }

    @Operation(summary = "Busca uma pauta por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pauta encontrada",
                    content = @Content(schema = @Schema(implementation = PautaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_404)
                    ))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PautaResponse> buscarPorId(@PathVariable UUID id) {
        Pauta pauta = pautaService.buscarPorId(id);
        return ResponseEntity.ok(PautaResponse.from(pauta));
    }

    @Operation(summary = "Edita uma pauta existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pauta atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = PautaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_400)
                    )),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_404)
                    ))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PautaResponse> editar(@PathVariable UUID id, @Valid @RequestBody EditarPautaRequest request) {
        Pauta pauta = pautaService.editar(id, request);
        return ResponseEntity.ok(PautaResponse.from(pauta));
    }
}
