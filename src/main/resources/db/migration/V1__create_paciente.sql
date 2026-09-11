CREATE TABLE paciente (
      id          BIGSERIAL PRIMARY KEY,
      nome        VARCHAR(120) NOT NULL,
      email       VARCHAR(150) NOT NULL UNIQUE,
      telefone    VARCHAR(20),
      data_nascimento DATE NOT NULL,
      criado_em   TIMESTAMP NOT NULL DEFAULT NOW()
);