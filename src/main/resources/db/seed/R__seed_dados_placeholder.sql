INSERT INTO paciente (nome, cpf, email, telefone, data_nascimento) VALUES
    ('Ana Beatriz Souza',    '11122233344', 'ana.souza@example.com',    '11988887777', '1990-05-12'),
    ('Bruno Carvalho Lima',  '22233344455', 'bruno.lima@example.com',   '11988886666', '1985-11-23'),
    ('Carla Mendes Ferreira','33344455566', 'carla.ferreira@example.com','11988885555', '1998-02-08'),
    ('Diego Alves Pereira',  NULL,          'diego.pereira@example.com','11988884444', '1979-07-30'),
    ('Elisa Ramos Nogueira', '55566677788', 'elisa.nogueira@example.com','11988883333', '2001-09-15');

INSERT INTO dentista (nome, cro, especialidade, ativo) VALUES
    ('Fernanda Torres Duarte', 'SP-123456', 'Ortodontia',      TRUE),
    ('Gustavo Henrique Rocha', 'SP-234567', 'Endodontia',      TRUE),
    ('Helena Cristina Barros', 'RJ-345678', 'Clínico Geral',   FALSE);

INSERT INTO procedimento (nome, duracao_minutos, valor) VALUES
    ('Consulta de Avaliação', 30,  80.00),
    ('Limpeza',               45, 150.00),
    ('Canal',                120, 900.00),
    ('Extração',              60, 300.00),
    ('Manutenção de Aparelho',30, 120.00);

INSERT INTO agendamento (paciente_id, dentista_id, procedimento_id, inicio, fim, status, observacao) VALUES
    (1, 1, 1, '2026-09-14 09:00', '2026-09-14 09:30', 'AGENDADO',  'Primeira consulta'),
    (2, 1, 2, '2026-09-14 10:00', '2026-09-14 10:45', 'CONFIRMADO', NULL),
    (3, 2, 3, '2026-09-15 14:00', '2026-09-15 16:00', 'AGENDADO',  'Trazer exames anteriores'),
    (4, 2, 4, '2026-09-16 08:30', '2026-09-16 09:30', 'REALIZADO', NULL),
    (5, 1, 5, '2026-09-17 11:00', '2026-09-17 11:30', 'AGENDADO',  NULL),
    (1, 2, 2, '2026-09-18 08:00', '2026-09-18 08:45', 'CANCELADO', NULL);

UPDATE agendamento SET motivo_cancelamento = 'Paciente remarcou por motivo pessoal'
    WHERE status = 'CANCELADO';
