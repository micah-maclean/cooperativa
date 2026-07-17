package com.micahmaclean.cooperativa.repository;

import com.micahmaclean.cooperativa.model.Pauta;
import com.micahmaclean.cooperativa.model.Voto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VotoRepositoryIT {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    void devePersistirVoto() {
        Pauta pauta = pautaRepository.save(new Pauta("Reforma", "Alteração"));
        Voto voto = new Voto(pauta, "12345678901", Voto.VotoEnum.SIM);

        Voto salvo = votoRepository.save(voto);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getAssociadoId()).isEqualTo("12345678901");
    }

    @Test
    void deveContabilizarVotos() {
        Pauta pauta = pautaRepository.save(new Pauta("Reforma", "Alteração"));
        votoRepository.save(new Voto(pauta, "111", Voto.VotoEnum.SIM));
        votoRepository.save(new Voto(pauta, "222", Voto.VotoEnum.SIM));
        votoRepository.save(new Voto(pauta, "333", Voto.VotoEnum.NAO));

        List<Object[]> resultado = votoRepository.contabilizarVotos(pauta.getId());

        assertThat(resultado).hasSize(2);
    }

    @Test
    void deveValidarVotoDuplicado() {
        Pauta pauta = pautaRepository.save(new Pauta("Reforma", "Alteração"));
        boolean existe = votoRepository.existsByPautaIdAndAssociadoId(pauta.getId(), "12345678901");

        assertThat(existe).isFalse();

        votoRepository.save(new Voto(pauta, "12345678901", Voto.VotoEnum.SIM));
        existe = votoRepository.existsByPautaIdAndAssociadoId(pauta.getId(), "12345678901");

        assertThat(existe).isTrue();
    }
}
