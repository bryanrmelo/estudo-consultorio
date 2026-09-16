CREATE TABLE procedimento (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(120)  NOT NULL,
    duracao_minutos INTEGER       NOT NULL,
    valor           NUMERIC(10,2) NOT NULL,
    criado_em       TIMESTAMP     NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_procedimento_duracao CHECK (duracao_minutos > 0),
    CONSTRAINT chk_procedimento_valor CHECK (valor >= 0)
);

ALTER TABLE agendamento
    ADD COLUMN procedimento_id     BIGINT REFERENCES procedimento(id),
    ADD COLUMN motivo_cancelamento TEXT;

CREATE INDEX idx_agendamento_procedimento ON agendamento (procedimento_id);

ALTER TABLE agendamento
    DROP CONSTRAINT chk_agendamento_status;

ALTER TABLE agendamento
    ADD CONSTRAINT chk_agendamento_status
        CHECK (status IN ('AGENDADO', 'CONFIRMADO', 'REALIZADO', 'CANCELADO', 'FALTOU'));

-- segunda a sexta 08:00-18:00, sábado 08:00-12:00, domingo não permitido.
-- exige inicio/fim no mesmo dia para a janela do dia da semana fazer sentido.
ALTER TABLE agendamento
    ADD CONSTRAINT chk_agendamento_horario_comercial
        CHECK (
            inicio::date = fim::date
            AND (
                (EXTRACT(DOW FROM inicio) BETWEEN 1 AND 5
                    AND inicio::time >= TIME '08:00' AND fim::time <= TIME '18:00')
                OR
                (EXTRACT(DOW FROM inicio) = 6
                    AND inicio::time >= TIME '08:00' AND fim::time <= TIME '12:00')
            )
        );
