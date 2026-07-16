package com.micahmaclean.cooperativa.repository;

import com.micahmaclean.cooperativa.model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SessaoRepository extends JpaRepository<Sessao, UUID> {
    Optional<Sessao> findByPautaId(UUID pautaId);
}
