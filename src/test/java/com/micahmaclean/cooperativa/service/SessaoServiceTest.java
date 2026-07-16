package com.micahmaclean.cooperativa.service;

import com.micahmaclean.cooperativa.dto.request.AbrirSessaoRequest;
import com.micahmaclean.cooperativa.exception.SessaoJaExisteException;
import com.micahmaclean.cooperativa.exception.SessaoNaoEncontradaException;
import com.micahmaclean.cooperativa.model.Pauta;
import com.micahmaclean.cooperativa.model.Sessao;
import com.micahmaclean.cooperativa.repository.SessaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessaoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private PautaService pautaService;

    private SessaoService sessaoService;

    @BeforeEach
    void setUp() {
        sessaoService = new SessaoService(sessaoRepository, pautaService);
    }

    @Test
    void deveAbrirSessaoComDuracaoPadrao() {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Reforma", "Alteração");
        when(pautaService.buscarPorId(pautaId)).thenReturn(pauta);
        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.empty());
        when(sessaoRepository.save(any(Sessao.class))).thenAnswer(inv -> inv.getArgument(0));

        Sessao sessao = sessaoService.abrir(pautaId, null);

        assertThat(sessao.getDuracaoSegundos()).isEqualTo(60);
        assertThat(sessao.getPauta()).isEqualTo(pauta);
        assertThat(sessao.estaAberta()).isTrue();
    }

    @Test
    void deveAbrirSessaoComDuracaoCustomizada() {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Reforma", "Alteração");
        AbrirSessaoRequest request = new AbrirSessaoRequest(120);
        when(pautaService.buscarPorId(pautaId)).thenReturn(pauta);
        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.empty());
        when(sessaoRepository.save(any(Sessao.class))).thenAnswer(inv -> inv.getArgument(0));

        Sessao sessao = sessaoService.abrir(pautaId, request);

        assertThat(sessao.getDuracaoSegundos()).isEqualTo(120);
    }

    @Test
    void deveLancarExcecaoAoAbrirSessaoParaPautaInexistente() {
        UUID pautaId = UUID.randomUUID();
        when(pautaService.buscarPorId(pautaId)).thenThrow(new RuntimeException("Pauta não encontrada"));

        assertThatThrownBy(() -> sessaoService.abrir(pautaId, null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void deveLancarExcecaoAoAbrirSegundaSessaoParaMesmaPauta() {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Reforma", "Alteração");
        Sessao sessaoExistente = new Sessao(pauta, 60);

        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.of(sessaoExistente));

        assertThatThrownBy(() -> sessaoService.abrir(pautaId, null))
                .isInstanceOf(SessaoJaExisteException.class);

        verify(sessaoRepository, never()).save(any());
    }

    @Test
    void deveBuscarSessaoExistente() {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Reforma", "Alteração");
        Sessao sessao = new Sessao(pauta, 60);
        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.of(sessao));

        Sessao encontrada = sessaoService.buscarPorPautaId(pautaId);

        assertThat(encontrada).isEqualTo(sessao);
    }

    @Test
    void deveLancarExcecaoAoBuscarSessaoInexistente() {
        UUID pautaId = UUID.randomUUID();
        when(sessaoRepository.findByPautaId(pautaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessaoService.buscarPorPautaId(pautaId))
                .isInstanceOf(SessaoNaoEncontradaException.class)
                .hasMessageContaining(pautaId.toString());
    }
}
