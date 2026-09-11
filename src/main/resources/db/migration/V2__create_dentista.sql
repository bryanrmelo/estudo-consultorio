CREATE TABLE dentista (
      id            BIGSERIAL PRIMARY KEY,
      nome          VARCHAR(120) NOT NULL,
      cro           VARCHAR(20)  NOT NULL UNIQUE,
      especialidade VARCHAR(60),
      ativo         BOOLEAN      NOT NULL DEFAULT TRUE,
      criado_em     TIMESTAMP    NOT NULL DEFAULT NOW()
);