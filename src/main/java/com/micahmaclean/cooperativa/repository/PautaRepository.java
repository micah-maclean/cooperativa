package com.micahmaclean.cooperativa.repository;

import com.micahmaclean.cooperativa.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PautaRepository extends JpaRepository<Pauta, UUID> {
}
