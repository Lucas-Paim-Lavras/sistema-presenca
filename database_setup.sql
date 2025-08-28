-- Script SQL para criação do banco de dados do Sistema de Controle de Presença
-- Execute este script no pgAdmin ou psql

-- Conectar ao banco controle_presenca antes de executar os comandos abaixo

-- Remover tabelas existentes se elas existirem para garantir um estado limpo
DROP TABLE IF EXISTS chamada_mentores_participantes CASCADE;
DROP TABLE IF EXISTS chamadas_mentores CASCADE;
DROP TABLE IF EXISTS presencas_mentores CASCADE; -- Removendo a tabela antiga de presenças de mentores
DROP TABLE IF EXISTS mentores CASCADE;
DROP TABLE IF EXISTS chamada_alunos CASCADE;
DROP TABLE IF EXISTS chamadas CASCADE;
DROP TABLE IF EXISTS alunos CASCADE;
DROP TABLE IF EXISTS turmas CASCADE;

-- Tabela de Turmas
CREATE TABLE turmas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    descricao TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativa BOOLEAN DEFAULT TRUE
);

-- Tabela de Alunos
CREATE TABLE alunos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    matricula VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    turma_id BIGINT NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_aluno_turma FOREIGN KEY (turma_id) REFERENCES turmas(id) ON DELETE CASCADE
);

-- Tabela de Chamadas de Aula
CREATE TABLE chamadas (
    id BIGSERIAL PRIMARY KEY,
    turma_id BIGINT NOT NULL REFERENCES turmas(id) ON DELETE CASCADE,
    data_chamada DATE NOT NULL,
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (turma_id, data_chamada) -- Garante que só há uma chamada por turma por dia
);

-- Tabela para o status de presença/falta dos alunos em uma Chamada
CREATE TABLE chamada_alunos (
    id BIGSERIAL PRIMARY KEY,
    chamada_id BIGINT NOT NULL REFERENCES chamadas(id) ON DELETE CASCADE,
    aluno_id BIGINT NOT NULL REFERENCES alunos(id) ON DELETE CASCADE,
    presente BOOLEAN NOT NULL DEFAULT FALSE,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (chamada_id, aluno_id) -- Garante que só há um registro por aluno por chamada
);

-- Tabela de Mentores
CREATE TABLE mentores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    tipo_mentor VARCHAR(50) NOT NULL, -- ENUM: 'MENTOR', 'MENTOR_TRAINEE', 'MENTOR_COORDENADOR'
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Chamadas de Mentores
CREATE TABLE chamadas_mentores (
    id BIGSERIAL PRIMARY KEY,
    data_chamada DATE NOT NULL,
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (data_chamada) -- Garante que só há uma chamada de mentor por dia
);

-- Tabela para o status de presença/falta dos mentores em uma Chamada de Mentor
CREATE TABLE chamada_mentores_participantes (
    id BIGSERIAL PRIMARY KEY,
    chamada_mentor_id BIGINT NOT NULL REFERENCES chamadas_mentores(id) ON DELETE CASCADE,
    mentor_id BIGINT NOT NULL REFERENCES mentores(id) ON DELETE CASCADE,
    presente BOOLEAN NOT NULL DEFAULT FALSE,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (chamada_mentor_id, mentor_id) -- Garante que só há um registro por mentor por chamada
);

-- Índices para melhor performance
CREATE INDEX idx_alunos_turma_id ON alunos(turma_id);
CREATE INDEX idx_chamadas_turma_id ON chamadas(turma_id);
CREATE INDEX idx_chamadas_data_chamada ON chamadas(data_chamada);
CREATE INDEX idx_chamada_alunos_chamada_id ON chamada_alunos(chamada_id);
CREATE INDEX idx_chamada_alunos_aluno_id ON chamada_alunos(aluno_id);
CREATE INDEX idx_mentores_email ON mentores(email);
CREATE INDEX idx_chamadas_mentores_data_chamada ON chamadas_mentores(data_chamada);
CREATE INDEX idx_chamada_mentores_participantes_chamada_id ON chamada_mentores_participantes(chamada_mentor_id);
CREATE INDEX idx_chamada_mentores_participantes_mentor_id ON chamada_mentores_participantes(mentor_id);

-- Dados de exemplo para teste
INSERT INTO turmas (nome, codigo, descricao) VALUES 
("Matemática Básica", "MAT001", "Turma de matemática para iniciantes"),
("Português Avançado", "POR002", "Turma avançada de língua portuguesa"),
("História Geral", "HIS003", "Curso de história geral");

INSERT INTO alunos (nome, matricula, email, turma_id) VALUES 
("João Silva", "2024001", "joao.silva@email.com", 1),
("Maria Santos", "2024002", "maria.santos@email.com", 1),
("Pedro Oliveira", "2024003", "pedro.oliveira@email.com", 2),
("Ana Costa", "2024004", "ana.costa@email.com", 2),
("Carlos Ferreira", "2024005", "carlos.ferreira@email.com", 3);

INSERT INTO mentores (nome, email, tipo_mentor) VALUES
("Mentor Alpha", "alpha@youxlab.com", "MENTOR"),
("Mentor Trainee Beta", "beta@youxlab.com", "MENTOR_TRAINEE"),
("Coordenador Gama", "gama@youxlab.com", "MENTOR_COORDENADOR");

-- Views para relatórios (adaptadas para o novo modelo)
-- View para relatórios de presença por aluno (baseado em chamada_alunos)
CREATE OR REPLACE VIEW vw_relatorio_presenca_aluno AS
SELECT 
    a.id as aluno_id,
    a.nome as aluno_nome,
    a.matricula as aluno_matricula,
    t.id as turma_id,
    t.nome as turma_nome,
    t.codigo as turma_codigo,
    COUNT(CASE WHEN ca.presente = TRUE THEN 1 END) as total_presencas,
    COUNT(CASE WHEN ca.presente = FALSE THEN 1 END) as total_faltas,
    COUNT(ca.id) as total_chamadas_registradas
FROM alunos a
JOIN turmas t ON a.turma_id = t.id
LEFT JOIN chamada_alunos ca ON a.id = ca.aluno_id
GROUP BY a.id, a.nome, a.matricula, t.id, t.nome, t.codigo
ORDER BY t.nome, a.nome;

-- View para relatórios de chamada por data/turma
CREATE OR REPLACE VIEW vw_relatorio_chamada_detalhe AS
SELECT
    c.id as chamada_id,
    c.data_chamada,
    c.observacoes as chamada_observacoes,
    t.id as turma_id,
    t.nome as turma_nome,
    t.codigo as turma_codigo,
    a.id as aluno_id,
    a.nome as aluno_nome,
    a.matricula as aluno_matricula,
    ca.presente
FROM chamadas c
JOIN turmas t ON c.turma_id = t.id
JOIN chamada_alunos ca ON c.id = ca.chamada_id
JOIN alunos a ON ca.aluno_id = a.id
ORDER BY c.data_chamada DESC, t.nome, a.nome;

-- View para estatísticas por turma (adaptada para o novo modelo)
CREATE OR REPLACE VIEW vw_estatisticas_turma AS
SELECT 
    t.id as turma_id,
    t.nome as turma_nome,
    t.codigo as turma_codigo,
    COUNT(DISTINCT a.id) as total_alunos,
    COUNT(DISTINCT c.id) as total_chamadas_realizadas,
    COUNT(CASE WHEN ca.presente = TRUE THEN 1 END) as total_presencas_registradas,
    ROUND(
        CASE 
            WHEN COUNT(DISTINCT a.id) * COUNT(DISTINCT c.id) > 0 THEN 
                (COUNT(CASE WHEN ca.presente = TRUE THEN 1 END)::decimal / (COUNT(DISTINCT a.id) * COUNT(DISTINCT c.id))) * 100 
            ELSE 0 
        END, 2
    ) as porcentagem_presenca_geral
FROM turmas t
LEFT JOIN alunos a ON t.id = a.turma_id AND a.ativo = TRUE
LEFT JOIN chamadas c ON t.id = c.turma_id
LEFT JOIN chamada_alunos ca ON c.id = ca.chamada_id AND a.id = ca.aluno_id
WHERE t.ativa = TRUE
GROUP BY t.id, t.nome, t.codigo
ORDER BY t.nome;

-- View para relatórios de presença por mentor (baseado em chamada_mentores_participantes)
CREATE OR REPLACE VIEW vw_relatorio_presenca_mentor AS
SELECT 
    m.id as mentor_id,
    m.nome as mentor_nome,
    m.email as mentor_email,
    m.tipo_mentor as mentor_tipo,
    COUNT(CASE WHEN cmp.presente = TRUE THEN 1 END) as total_presencas,
    COUNT(CASE WHEN cmp.presente = FALSE THEN 1 END) as total_faltas,
    COUNT(cmp.id) as total_chamadas_registradas
FROM mentores m
LEFT JOIN chamada_mentores_participantes cmp ON m.id = cmp.mentor_id
GROUP BY m.id, m.nome, m.email, m.tipo_mentor
ORDER BY m.nome;

-- View para relatórios de chamada de mentor por data
CREATE OR REPLACE VIEW vw_relatorio_chamada_mentor_detalhe AS
SELECT
    cm.id as chamada_mentor_id,
    cm.data_chamada,
    cm.observacoes as chamada_mentor_observacoes,
    m.id as mentor_id,
    m.nome as mentor_nome,
    m.email as mentor_email,
    m.tipo_mentor as mentor_tipo,
    cmp.presente
FROM chamadas_mentores cm
JOIN chamada_mentores_participantes cmp ON cm.id = cmp.chamada_mentor_id
JOIN mentores m ON cmp.mentor_id = m.id
ORDER BY cm.data_chamada DESC, m.nome;

-- View para estatísticas de mentores (adaptada para o novo modelo)
CREATE OR REPLACE VIEW vw_estatisticas_mentor AS
SELECT 
    m.tipo_mentor as mentor_tipo,
    COUNT(DISTINCT m.id) as total_mentores_ativos,
    COUNT(DISTINCT cm.id) as total_chamadas_realizadas,
    COUNT(CASE WHEN cmp.presente = TRUE THEN 1 END) as total_presencas_registradas,
    ROUND(
        CASE 
            WHEN COUNT(DISTINCT m.id) * COUNT(DISTINCT cm.id) > 0 THEN 
                (COUNT(CASE WHEN cmp.presente = TRUE THEN 1 END)::decimal / (COUNT(DISTINCT m.id) * COUNT(DISTINCT cm.id))) * 100 
            ELSE 0 
        END, 2
    ) as porcentagem_presenca_geral
FROM mentores m
LEFT JOIN chamada_mentores_participantes cmp ON m.id = cmp.mentor_id
LEFT JOIN chamadas_mentores cm ON cmp.chamada_mentor_id = cm.id
WHERE m.ativo = TRUE
GROUP BY m.tipo_mentor
ORDER BY m.tipo_mentor;


