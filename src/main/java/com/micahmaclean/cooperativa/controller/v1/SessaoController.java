package com.micahmaclean.cooperativa.controller.v1;

import com.micahmaclean.cooperativa.dto.request.AbrirSessaoRequest;
import com.micahmaclean.cooperativa.dto.request.EditarSessaoRequest;
import com.micahmaclean.cooperativa.dto.response.ErroResponse;
import com.micahmaclean.cooperativa.dto.response.PautaResponse;
import com.micahmaclean.cooperativa.dto.response.SessaoResponse;
import com.micahmaclean.cooperativa.exception.SwaggerExemplos;
import com.micahmaclean.cooperativa.model.Sessao;
import com.micahmaclean.cooperativa.service.SessaoService;
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
@RequestMapping("/v1/pautas/{pautaId}/sessoes")
@Tag(name = "Sessões", description = "Cadastro e consulta de sessões de votação")
public class SessaoController {

    private final SessaoService sessaoService;

    public SessaoController(SessaoService sessaoService) {
        this.sessaoService = sessaoService;
    }

    @Operation(summary = "Cadastra uma nova sessão", description = "Cria uma sessão para votação em assembleia.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sessão criada com sucesso",
                    content = @Content(schema = @Schema(implementation = PautaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_404)
                    )),
            @ApiResponse(responseCode = "409", description = "Já existe uma sessão para esta pauta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_409)
                    )),
    })
    @PostMapping
    public ResponseEntity<SessaoResponse> abrir(@PathVariable UUID pautaId, @Valid @RequestBody(required = false) AbrirSessaoRequest request) {
        Sessao sessao = sessaoService.abrir(pautaId, request);
        SessaoResponse response = SessaoResponse.from(sessao);
        return ResponseEntity.created(URI.create("/v1/pautas/" + pautaId + "/sessoes")).body(response);
    }

    @Operation(summary = "Busca uma sessao por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sessão encontrada",
                    content = @Content(schema = @Schema(implementation = SessaoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_404)
                    ))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SessaoResponse> buscarPorId(@PathVariable UUID id) {
        Sessao sessao = sessaoService.buscarPorId(id);
        return ResponseEntity.ok(SessaoResponse.from(sessao));
    }

    @Operation(summary = "Busca sessões de uma pautaId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sessão encontrada",
                    content = @Content(schema = @Schema(implementation = SessaoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_404)
                    ))
    })
    @GetMapping
    public ResponseEntity<SessaoResponse> buscarPorPautaId(@PathVariable UUID pautaId) {
        Sessao sessao = sessaoService.buscarPorPautaId(pautaId);
        return ResponseEntity.ok(SessaoResponse.from(sessao));
    }

    @Operation(summary = "Edita uma sessão existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sessão atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = SessaoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_400)
                    )),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(value = SwaggerExemplos.ERRO_404)
                    ))
    })
    @PutMapping("/{id}")
    public ResponseEntity<SessaoResponse> editar(@PathVariable UUID id, @Valid @RequestBody EditarSessaoRequest request) {
        Sessao sessao = sessaoService.editar(id, request);
        return ResponseEntity.ok(SessaoResponse.from(sessao));
    }
}
