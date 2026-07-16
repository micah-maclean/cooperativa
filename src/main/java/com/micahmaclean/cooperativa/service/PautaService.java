package com.micahmaclean.cooperativa.service;

import com.micahmaclean.cooperativa.dto.request.CriarPautaRequest;
import com.micahmaclean.cooperativa.dto.request.EditarPautaRequest;
import com.micahmaclean.cooperativa.exception.PautaNaoEncontradaException;
import com.micahmaclean.cooperativa.exception.PautaTemSessaoException;
import com.micahmaclean.cooperativa.model.Pauta;
import com.micahmaclean.cooperativa.repository.PautaRepository;
import com.micahmaclean.cooperativa.repository.SessaoRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PautaService {
    private final PautaRepository pautaRepository;
    private final SessaoRepository sessaoRepository;

    public PautaService(PautaRepository pautaRepository, SessaoRepository sessaoRepository) {
        this.pautaRepository = pautaRepository;
        this.sessaoRepository = sessaoRepository;
    }

    public Pauta criar(CriarPautaRequest request) {
        Pauta pauta = new Pauta(request.titulo(), request.descricao());
        return pautaRepository.save(pauta);
    }

    public Pauta buscarPorId(UUID id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new PautaNaoEncontradaException(id));
    }

    public Pauta editar(UUID id, EditarPautaRequest request) {
        Pauta pauta = buscarPorId(id);
        sessaoRepository.findByPautaId(id).ifPresent(sessao -> {
            throw new PautaTemSessaoException();
        });
        pauta.editar(request.titulo(), request.descricao());
        return pautaRepository.save(pauta);
    }
}
