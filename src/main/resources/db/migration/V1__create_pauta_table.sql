CREATE TABLE pauta (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(200) NOT NULL,
    descricao VARCHAR(1000),
    data_criacao TIMESTAMP NOT NULL DEFAULT now()
);