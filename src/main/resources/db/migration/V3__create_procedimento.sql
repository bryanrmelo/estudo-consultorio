CREATE TABLE procedimento (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(120)  NOT NULL,
    duracao_minutos INTEGER       NOT NULL,
    valor           NUMERIC(10,2) NOT NULL,
    criado_em       TIMESTAMP     NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_procedimento_duracao CHECK (duracao_minutos > 0),
    CONSTRAINT chk_procedimento_valor CHECK (valor >= 0)
);
