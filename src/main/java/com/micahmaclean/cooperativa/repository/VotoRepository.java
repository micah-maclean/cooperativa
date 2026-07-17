package com.micahmaclean.cooperativa.repository;

import com.micahmaclean.cooperativa.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface VotoRepository extends JpaRepository<Voto, UUID> {
    boolean existsByPautaIdAndAssociadoId(UUID pautaId, String associadoId);

    @Query("SELECT v.voto, COUNT(v) FROM Voto v WHERE v.pauta.id = ?1 GROUP BY v.voto")
    List<Object[]> contabilizarVotos(UUID sessaoId);
}
