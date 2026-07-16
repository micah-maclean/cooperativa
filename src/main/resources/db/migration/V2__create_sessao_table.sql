CREATE TABLE sessao(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pauta_id UUID NOT NULL UNIQUE REFERENCES pauta(id),
    data_abertura TIMESTAMP NOT NULL DEFAULT now(),
    duracao_segundos INTEGER NOT NULL DEFAULT 60
);