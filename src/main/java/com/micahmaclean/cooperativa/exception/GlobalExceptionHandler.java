package com.micahmaclean.cooperativa.exception;

import com.micahmaclean.cooperativa.dto.response.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PautaNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> handlePautaNaoEncontrada(PautaNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpoErro(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Erro de validação");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpoErro(HttpStatus.BAD_REQUEST, mensagem));
    }

    @ExceptionHandler(PautaTemSessaoException.class)
    public ResponseEntity<ErroResponse> handlePautaTemSessao(PautaTemSessaoException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(corpoErro(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()));
    }

    @ExceptionHandler(SessaoJaExisteException.class)
    public ResponseEntity<ErroResponse> handleSessaoJaExiste(SessaoJaExisteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(corpoErro(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(SessaoNaoEncontradaException.class)
    public ResponseEntity<ErroResponse> handleSessaoNaoEncontrada(SessaoNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpoErro(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(VotoDuplicadoException.class)
    public ResponseEntity<ErroResponse> handleVotoDuplicado(VotoDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(corpoErro(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(VotoEmSessaoFechadaException.class)
    public ResponseEntity<ErroResponse> handleVotoEmSessaoFechada(VotoEmSessaoFechadaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpoErro(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(AssociadoNaoAptoException.class)
    public ResponseEntity<ErroResponse> handleAssociadoNaoApto(AssociadoNaoAptoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(corpoErro(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    @ExceptionHandler(AssociadoInvalidoException.class)
    public ResponseEntity<ErroResponse> handleAssociadoInvalido(AssociadoInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpoErro(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    private ErroResponse corpoErro(HttpStatus status, String mensagem) {
        return new ErroResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensagem);
    }
}
