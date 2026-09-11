CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE agendamento (
     id          BIGSERIAL PRIMARY KEY,
     paciente_id BIGINT      NOT NULL REFERENCES paciente(id),
     dentista_id BIGINT      NOT NULL REFERENCES dentista(id),
     inicio      TIMESTAMP   NOT NULL,
     fim         TIMESTAMP   NOT NULL,
     status      VARCHAR(20) NOT NULL DEFAULT 'AGENDADO',
     observacao  VARCHAR(500),
     criado_em   TIMESTAMP   NOT NULL DEFAULT NOW(),

     CONSTRAINT chk_agendamento_periodo
         CHECK (fim > inicio),

     CONSTRAINT chk_agendamento_status
         CHECK (status IN ('AGENDADO', 'CONFIRMADO', 'REALIZADO', 'CANCELADO')),

     CONSTRAINT uk_agendamento_dentista_horario
         EXCLUDE USING gist (
         dentista_id WITH =,
         tsrange(inicio, fim, '[)') WITH &&
         ) WHERE (status <> 'CANCELADO')
);

CREATE INDEX idx_agendamento_paciente ON agendamento (paciente_id);
CREATE INDEX idx_agendamento_inicio   ON agendamento (inicio);