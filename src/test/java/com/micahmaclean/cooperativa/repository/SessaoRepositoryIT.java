package com.micahmaclean.cooperativa.repository;

import com.micahmaclean.cooperativa.model.Pauta;
import com.micahmaclean.cooperativa.model.Sessao;

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
public class SessaoRepositoryIT {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    void devePersistirSessaoComMigrationAplicada() {
        Pauta pauta = pautaRepository.save(new Pauta("Reforma", "Alteração"));
        Sessao sessao = new Sessao(pauta, 120);

        Sessao salva = sessaoRepository.save(sessao);

        assertThat(salva.getId()).isNotNull();
        assertThat(salva.getDuracaoSegundos()).isEqualTo(120);
    }

    @Test
    void deveBuscarSessaoPorPautaId() {
        Pauta pauta = pautaRepository.save(new Pauta("Reforma", "Alteração"));
        Sessao sessao = new Sessao(pauta, 60);
        sessaoRepository.save(sessao);

        Optional<Sessao> encontrada = sessaoRepository.findByPautaId(pauta.getId());

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getDuracaoSegundos()).isEqualTo(60);
    }

    @Test
    void deveBuscarSessaoPorId() {
        Pauta pauta = pautaRepository.save(new Pauta("Reforma", "Alteração"));
        Sessao sessao = new Sessao(pauta, 60);
        Sessao salva = sessaoRepository.save(sessao);

        assertThat(salva.getId()).isNotNull();

        Optional<Sessao> encontrada = sessaoRepository.findById(salva.getId());
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getPauta()).isEqualTo(pauta);
        assertThat(encontrada.get().getDuracaoSegundos()).isEqualTo(60);
    }

    @Test
    void deveRetornarVazioParaPautaSemSessao() {
        UUID pautaId = UUID.randomUUID();

        Optional<Sessao> encontrada = sessaoRepository.findByPautaId(pautaId);

        assertThat(encontrada).isEmpty();
    }
}
