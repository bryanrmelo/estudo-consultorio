INSERT INTO paciente (nome, email, telefone, data_nascimento) VALUES
    ('Ana Souza',       'ana.souza@example.com',      '11987654321', '1990-05-14'),
    ('Bruno Lima',      'bruno.lima@example.com',     '11976543210', '1985-11-02'),
    ('Carla Nogueira',  'carla.nogueira@example.com', '11965432109', '2000-02-28');

INSERT INTO dentista (nome, cro, especialidade) VALUES
    ('Dr. Felipe Rocha',  'CRO-SP-11111', 'Ortodontia'),
    ('Dra. Marina Alves', 'CRO-SP-22222', 'Endodontia');

INSERT INTO procedimento (nome, duracao_minutos, valor) VALUES
    ('Limpeza',      30, 120.00),
    ('Restauração',  60, 250.00),
    ('Canal',        90, 600.00),
    ('Avaliação',    30,  80.00);

INSERT INTO agendamento (paciente_id, dentista_id, procedimento_id, inicio, fim, status, observacao, motivo_cancelamento)
SELECT p.id, d.id, pr.id, '2026-09-16 09:00', '2026-09-16 09:30', 'AGENDADO', NULL, NULL
FROM paciente p, dentista d, procedimento pr
WHERE p.email = 'ana.souza@example.com' AND d.cro = 'CRO-SP-11111' AND pr.nome = 'Limpeza';

INSERT INTO agendamento (paciente_id, dentista_id, procedimento_id, inicio, fim, status, observacao, motivo_cancelamento)
SELECT p.id, d.id, pr.id, '2026-09-16 10:00', '2026-09-16 11:00', 'CONFIRMADO', 'Primeira sessão de restauração', NULL
FROM paciente p, dentista d, procedimento pr
WHERE p.email = 'bruno.lima@example.com' AND d.cro = 'CRO-SP-22222' AND pr.nome = 'Restauração';

INSERT INTO agendamento (paciente_id, dentista_id, procedimento_id, inicio, fim, status, observacao, motivo_cancelamento)
SELECT p.id, d.id, pr.id, '2026-09-17 14:00', '2026-09-17 15:30', 'REALIZADO', NULL, NULL
FROM paciente p, dentista d, procedimento pr
WHERE p.email = 'carla.nogueira@example.com' AND d.cro = 'CRO-SP-11111' AND pr.nome = 'Canal';

INSERT INTO agendamento (paciente_id, dentista_id, procedimento_id, inicio, fim, status, observacao, motivo_cancelamento)
SELECT p.id, d.id, pr.id, '2026-09-18 08:00', '2026-09-18 08:30', 'CANCELADO', NULL, 'Paciente remarcou para outra data'
FROM paciente p, dentista d, procedimento pr
WHERE p.email = 'ana.souza@example.com' AND d.cro = 'CRO-SP-22222' AND pr.nome = 'Avaliação';

INSERT INTO agendamento (paciente_id, dentista_id, procedimento_id, inicio, fim, status, observacao, motivo_cancelamento)
SELECT p.id, d.id, pr.id, '2026-09-19 09:00', '2026-09-19 09:30', 'FALTOU', NULL, NULL
FROM paciente p, dentista d, procedimento pr
WHERE p.email = 'bruno.lima@example.com' AND d.cro = 'CRO-SP-11111' AND pr.nome = 'Avaliação';
