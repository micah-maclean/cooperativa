package com.micahmaclean.cooperativa.service;

import com.micahmaclean.cooperativa.dto.request.CriarPautaRequest;
import com.micahmaclean.cooperativa.dto.request.EditarPautaRequest;
import com.micahmaclean.cooperativa.exception.PautaNaoEncontradaException;
import com.micahmaclean.cooperativa.model.Pauta;
import com.micahmaclean.cooperativa.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    private PautaService pautaService;

    @BeforeEach
    void setUp() {
        pautaService = new PautaService(pautaRepository);
    }

    @Test
    void deveCriarPautaComTituloEDescricao() {
        CriarPautaRequest request = new CriarPautaRequest("Reforma do estatuto", "Alteração do artigo 5");
        when(pautaRepository.save(any(Pauta.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Pauta pauta = pautaService.criar(request);

        assertThat(pauta.getTitulo()).isEqualTo("Reforma do estatuto");
        assertThat(pauta.getDescricao()).isEqualTo("Alteração do artigo 5");
        assertThat(pauta.getDataCriacao()).isNotNull();

        ArgumentCaptor<Pauta> captor = ArgumentCaptor.forClass(Pauta.class);
        verify(pautaRepository).save(captor.capture());
        assertThat(captor.getValue().getTitulo()).isEqualTo("Reforma do estatuto");
    }

    @Test
    void deveBuscarPautaExistentePorId() {
        UUID id = UUID.randomUUID();
        Pauta pauta = new Pauta("Reforma do estatuto", "Alteração do artigo 5");
        when(pautaRepository.findById(id)).thenReturn(Optional.of(pauta));

        Pauta encontrada = pautaService.buscarPorId(id);

        assertThat(encontrada).isEqualTo(pauta);
    }

    @Test
    void deveLancarExcecaoAoBuscarPautaInexistente() {
        UUID id = UUID.randomUUID();
        when(pautaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pautaService.buscarPorId(id))
                .isInstanceOf(PautaNaoEncontradaException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void deveEditarPautaExistente() {
        UUID id = UUID.randomUUID();
        Pauta pauta = new Pauta("Título antigo", "Descrição antiga");
        EditarPautaRequest request = new EditarPautaRequest("Título novo", "Descrição nova");

        when(pautaRepository.findById(id)).thenReturn(Optional.of(pauta));
        when(pautaRepository.save(any(Pauta.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Pauta editada = pautaService.editar(id, request);

        assertThat(editada.getTitulo()).isEqualTo("Título novo");
        assertThat(editada.getDescricao()).isEqualTo("Descrição nova");
        verify(pautaRepository).save(pauta);
    }

    @Test
    void deveLancarExcecaoAoEditarPautaInexistente() {
        UUID id = UUID.randomUUID();
        EditarPautaRequest request = new EditarPautaRequest("Título novo", "Descrição nova");
        when(pautaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pautaService.editar(id, request))
                .isInstanceOf(PautaNaoEncontradaException.class);

        verify(pautaRepository, never()).save(any());
    }
}
