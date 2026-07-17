package com.micahmaclean.cooperativa.service;

import com.micahmaclean.cooperativa.dto.request.RegistrarVotoRequest;
import com.micahmaclean.cooperativa.dto.response.ResultadoVotacaoResponse;
import com.micahmaclean.cooperativa.exception.VotoDuplicadoException;
import com.micahmaclean.cooperativa.exception.VotoEmSessaoFechadaException;
import com.micahmaclean.cooperativa.model.Pauta;
import com.micahmaclean.cooperativa.model.Sessao;
import com.micahmaclean.cooperativa.model.Voto;
import com.micahmaclean.cooperativa.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoService sessaoService;

    @Mock
    private PautaService pautaService;

    private VotoService votoService;

    @BeforeEach
    void setUp() {
        votoService = new VotoService(votoRepository, sessaoService, pautaService);
    }

    @Test
    void deveRegistrarVotoComSucesso() {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Reforma", "Alteração");
        Sessao sessao = new Sessao(pauta, 60);
        RegistrarVotoRequest request = new RegistrarVotoRequest("12345678901", Voto.VotoEnum.SIM);

        when(sessaoService.buscarPorPautaId(pautaId)).thenReturn(sessao);
        when(votoRepository.existsByPautaIdAndAssociadoId(pautaId, "12345678901")).thenReturn(false);
        when(pautaService.buscarPorId(pautaId)).thenReturn(pauta);
        when(votoRepository.save(any(Voto.class))).thenAnswer(inv -> inv.getArgument(0));

        Voto voto = votoService.registrar(pautaId, request);

        assertThat(voto.getAssociadoId()).isEqualTo("12345678901");
        assertThat(voto.getVoto()).isEqualTo(Voto.VotoEnum.SIM);
        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    void deveLancarExcecaoAoVotarEmSessaoFechada() {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Reforma", "Alteração");
        Sessao sessao = new Sessao(pauta, -1); // sessão expirada
        RegistrarVotoRequest request = new RegistrarVotoRequest("12345678901", Voto.VotoEnum.NAO);

        when(sessaoService.buscarPorPautaId(pautaId)).thenReturn(sessao);

        assertThatThrownBy(() -> votoService.registrar(pautaId, request))
                .isInstanceOf(VotoEmSessaoFechadaException.class);

        verify(votoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoAoVotarDuplicado() {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Reforma", "Alteração");
        Sessao sessao = new Sessao(pauta, 60);
        RegistrarVotoRequest request = new RegistrarVotoRequest("12345678901", Voto.VotoEnum.SIM);

        when(sessaoService.buscarPorPautaId(pautaId)).thenReturn(sessao);
        when(votoRepository.existsByPautaIdAndAssociadoId(pautaId, "12345678901")).thenReturn(true);

        assertThatThrownBy(() -> votoService.registrar(pautaId, request))
                .isInstanceOf(VotoDuplicadoException.class);

        verify(votoRepository, never()).save(any());
    }

    @Test
    void deveContabilizarResultado() {
        UUID pautaId = UUID.randomUUID();
        when(sessaoService.buscarPorPautaId(pautaId)).thenReturn(new Sessao(new Pauta("R", "A"), 60));
        when(votoRepository.contabilizarVotos(pautaId))
                .thenReturn(Arrays.asList(
                        new Object[]{Voto.VotoEnum.SIM, 7L},
                        new Object[]{Voto.VotoEnum.NAO, 3L}
                ));

        ResultadoVotacaoResponse resultado = votoService.contabilizar(pautaId);

        assertThat(resultado.sim()).isEqualTo(7);
        assertThat(resultado.nao()).isEqualTo(3);
        assertThat(resultado.aprovada()).isTrue();
    }

    @Test
    void deveContabilizarComZeroVotos() {
        UUID pautaId = UUID.randomUUID();
        when(sessaoService.buscarPorPautaId(pautaId)).thenReturn(new Sessao(new Pauta("R", "A"), 60));
        when(votoRepository.contabilizarVotos(pautaId)).thenReturn(Arrays.asList());

        ResultadoVotacaoResponse resultado = votoService.contabilizar(pautaId);

        assertThat(resultado.sim()).isEqualTo(0);
        assertThat(resultado.nao()).isEqualTo(0);
        assertThat(resultado.aprovada()).isFalse();
    }
}
