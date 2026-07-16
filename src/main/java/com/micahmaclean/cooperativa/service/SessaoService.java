package com.micahmaclean.cooperativa.service;

import com.micahmaclean.cooperativa.dto.request.AbrirSessaoRequest;
import com.micahmaclean.cooperativa.dto.request.EditarSessaoRequest;
import com.micahmaclean.cooperativa.exception.SessaoJaExisteException;
import com.micahmaclean.cooperativa.exception.SessaoNaoEncontradaException;
import com.micahmaclean.cooperativa.model.Pauta;
import com.micahmaclean.cooperativa.model.Sessao;
import com.micahmaclean.cooperativa.repository.SessaoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessaoService {
    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;

    public SessaoService(SessaoRepository sessaoRepository, PautaService pautaService) {
        this.sessaoRepository = sessaoRepository;
        this.pautaService = pautaService;
    }

    public Sessao abrir(UUID pautaId, AbrirSessaoRequest request) {
        sessaoRepository.findByPautaId(pautaId).ifPresent(sessao -> {
            throw new SessaoJaExisteException(pautaId);
        });

        Pauta pauta = pautaService.buscarPorId(pautaId);

        Integer duracao = request != null ? request.duracaoSegundos() : null;
        Sessao sessao = new Sessao(pauta, duracao);
        return sessaoRepository.save(sessao);
    }

    public Sessao buscarPorId(UUID id) {
        return sessaoRepository.findById(id)
                .orElseThrow(() -> new SessaoNaoEncontradaException(id));
    }

    public Sessao buscarPorPautaId(UUID pautaId) {
        return sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new SessaoNaoEncontradaException(pautaId));
    }

    public Sessao editar(UUID id, EditarSessaoRequest request) {
        Sessao sessao = buscarPorId(id);
        sessao.editar(request.duracaoSegundos());
        return sessaoRepository.save(sessao);
    }
}
