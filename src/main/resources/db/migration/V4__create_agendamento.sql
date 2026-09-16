CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE agendamento (
     id                  BIGSERIAL PRIMARY KEY,
     paciente_id         BIGINT      NOT NULL REFERENCES paciente(id),
     dentista_id         BIGINT      NOT NULL REFERENCES dentista(id),
     procedimento_id     BIGINT      REFERENCES procedimento(id),
     inicio              TIMESTAMP   NOT NULL,
     fim                 TIMESTAMP   NOT NULL,
     status              VARCHAR(20) NOT NULL DEFAULT 'AGENDADO',
     observacao          VARCHAR(500),
     motivo_cancelamento TEXT,
     criado_em           TIMESTAMP   NOT NULL DEFAULT NOW(),

     CONSTRAINT chk_agendamento_periodo
         CHECK (fim > inicio),

     CONSTRAINT chk_agendamento_status
         CHECK (status IN ('AGENDADO', 'CONFIRMADO', 'REALIZADO', 'CANCELADO', 'FALTOU')),

     -- segunda a sexta 08:00-18:00, sábado 08:00-12:00, domingo não permitido.
     -- exige inicio/fim no mesmo dia para a janela do dia da semana fazer sentido.
     CONSTRAINT chk_agendamento_horario_comercial
         CHECK (
             inicio::date = fim::date
             AND (
                 (EXTRACT(DOW FROM inicio) BETWEEN 1 AND 5
                     AND inicio::time >= TIME '08:00' AND fim::time <= TIME '18:00')
                 OR
                 (EXTRACT(DOW FROM inicio) = 6
                     AND inicio::time >= TIME '08:00' AND fim::time <= TIME '12:00')
             )
         ),

     CONSTRAINT uk_agendamento_dentista_horario
         EXCLUDE USING gist (
         dentista_id WITH =,
         tsrange(inicio, fim, '[)') WITH &&
         ) WHERE (status <> 'CANCELADO')
);

CREATE INDEX idx_agendamento_paciente     ON agendamento (paciente_id);
CREATE INDEX idx_agendamento_inicio       ON agendamento (inicio);
CREATE INDEX idx_agendamento_procedimento ON agendamento (procedimento_id);
