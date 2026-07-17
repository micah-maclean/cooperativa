package com.micahmaclean.cooperativa.client;

import com.micahmaclean.cooperativa.exception.AssociadoInvalidoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class ClienteInfoUsuario {

    private final RestClient restClient;
    private final String baseUrl;
    private final boolean habilitado;

    public ClienteInfoUsuario(RestClient.Builder builder,
                              @Value("${user-info.base-url:}") String baseUrl,
                              @Value("${user-info.habilitado:false}") boolean habilitado) {
        this.restClient = builder.build();
        this.baseUrl = baseUrl;
        this.habilitado = habilitado;
    }

    public RespostaInfoUsuario verificarElegibilidade(String cpf) {
        if (!habilitado) {
            return new RespostaInfoUsuario("ABLE_TO_VOTE");
        }

        try {
            String url = baseUrl + "/users/" + cpf;
            RespostaInfoUsuario response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(RespostaInfoUsuario.class);
            return response;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new AssociadoInvalidoException(cpf);
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao verificar elegibilidade do associado: " + cpf, ex);
        }
    }

    public record RespostaInfoUsuario(String status) {}
}
