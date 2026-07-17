package com.micahmaclean.cooperativa.service;

import com.micahmaclean.cooperativa.client.ClienteInfoUsuario;
import com.micahmaclean.cooperativa.dto.request.RegistrarVotoRequest;
import com.micahmaclean.cooperativa.dto.response.ResultadoVotacaoResponse;
import com.micahmaclean.cooperativa.exception.VotoDuplicadoException;
import com.micahmaclean.cooperativa.exception.VotoEmSessaoFechadaException;
import com.micahmaclean.cooperativa.model.Pauta;
import com.micahmaclean.cooperativa.model.Sessao;
import com.micahmaclean.cooperativa.model.Voto;
import com.micahmaclean.cooperativa.repository.VotoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoService sessaoService;
    private final PautaService pautaService;
    private final ClienteInfoUsuario clienteInfoUsuario;

    public VotoService(VotoRepository votoRepository, SessaoService sessaoService, PautaService pautaService, ClienteInfoUsuario clienteInfoUsuario) {
        this.votoRepository = votoRepository;
        this.sessaoService = sessaoService;
        this.pautaService = pautaService;
        this.clienteInfoUsuario = clienteInfoUsuario;
    }

    public Voto registrar(UUID pautaId, RegistrarVotoRequest request) {
        ClienteInfoUsuario.RespostaInfoUsuario  info = clienteInfoUsuario.verificarElegibilidade(request.associadoId());
        if (!"ABLE_TO_VOTE".equals(info.status())) {
            throw new com.micahmaclean.cooperativa.exception.AssociadoNaoAptoException(request.associadoId());
        }

        Sessao sessao = sessaoService.buscarPorPautaId(pautaId);
        if (!sessao.estaAberta()) {
            throw new VotoEmSessaoFechadaException(sessao.getId());
        }

        if (votoRepository.existsByPautaIdAndAssociadoId(pautaId, request.associadoId())) {
            throw new VotoDuplicadoException(pautaId, request.associadoId());
        }

        Pauta pauta = pautaService.buscarPorId(pautaId);
        Voto voto = new Voto(pauta, request.associadoId(), request.voto());
        return votoRepository.save(voto);
    }

    public ResultadoVotacaoResponse contabilizar(UUID pautaId) {
        Sessao sessao = sessaoService.buscarPorPautaId(pautaId);

        var resultados = votoRepository.contabilizarVotos(pautaId);
        long sim = 0, nao = 0;

        for (Object[] row : resultados) {
            Voto.VotoEnum votoEnum = (Voto.VotoEnum) row[0];
            long count = ((Number) row[1]).longValue();
            if (votoEnum == Voto.VotoEnum.SIM) sim = count;
            else nao = count;
        }

        return ResultadoVotacaoResponse.from(sim, nao);
    }
}
