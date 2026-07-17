package com.micahmaclean.cooperativa.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "voto")
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Column(name = "associado_id", nullable = false, length = 11)
    private String associadoId;

    @Column(nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private VotoEnum voto;

    @Column(name = "data_voto", nullable = false)
    private LocalDateTime dataVoto;

    protected Voto() {}

    public Voto(Pauta pauta, String associadoId, VotoEnum voto) {
        this.pauta = pauta;
        this.associadoId = associadoId;
        this.voto = voto;
        this.dataVoto = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public Pauta getPauta() { return pauta; }
    public String getAssociadoId() { return associadoId; }
    public VotoEnum getVoto() { return voto; }
    public LocalDateTime getDataVoto() { return dataVoto; }

    public enum VotoEnum {
        SIM, NAO
    }
}
