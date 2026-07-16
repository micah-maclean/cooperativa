package com.micahmaclean.cooperativa.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessao")
public class Sessao {

    private static final int DURACAO_PADRAO_SEGUNDOS = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false, unique = true)
    private Pauta pauta;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "duracao_segundos", nullable = false)
    private Integer duracaoSegundos;

    protected Sessao() {
        // exigido pelo JPA
    }

    public Sessao(Pauta pauta, Integer duracaoSegundos) {
        this.pauta = pauta;
        this.dataAbertura = LocalDateTime.now();
        this.duracaoSegundos = duracaoSegundos != null ? duracaoSegundos : DURACAO_PADRAO_SEGUNDOS;
    }

    public UUID getId() {
        return id;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public Integer getDuracaoSegundos() {
        return duracaoSegundos;
    }

    public boolean estaAberta() {
        return LocalDateTime.now().isBefore(dataAbertura.plusSeconds(duracaoSegundos));
    }

    public void editar(Integer duracaoSegundos) {
        this.duracaoSegundos = duracaoSegundos;
    }
}
