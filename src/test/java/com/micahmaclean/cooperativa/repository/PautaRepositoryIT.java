package com.micahmaclean.cooperativa.repository;

import com.micahmaclean.cooperativa.model.Pauta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PautaRepositoryIT {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    void devePersistirEBuscarPautaComMigrationAplicada() {
        Pauta pauta = new Pauta("Reforma do estatuto", "Alteração do artigo 5");

        Pauta salva = pautaRepository.save(pauta);

        assertThat(salva.getId()).isNotNull();

        Optional<Pauta> encontrada = pautaRepository.findById(salva.getId());
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getTitulo()).isEqualTo("Reforma do estatuto");
        assertThat(encontrada.get().getDescricao()).isEqualTo("Alteração do artigo 5");
        assertThat(encontrada.get().getDataCriacao()).isNotNull();
    }

    @Test
    void deveRetornarVazioParaIdInexistente() {
        Optional<Pauta> encontrada = pautaRepository.findById(UUID.randomUUID());

        assertThat(encontrada).isEmpty();
    }
}
