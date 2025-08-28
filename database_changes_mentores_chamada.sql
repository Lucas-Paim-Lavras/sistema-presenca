-- Remover tabelas antigas de presença de mentores (se existirem)
DROP TABLE IF EXISTS presencas_mentores CASCADE;

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
CREATE INDEX idx_chamadas_mentores_data_chamada ON chamadas_mentores(data_chamada);
CREATE INDEX idx_chamada_mentores_participantes_chamada_id ON chamada_mentores_participantes(chamada_mentor_id);
CREATE INDEX idx_chamada_mentores_participantes_mentor_id ON chamada_mentores_participantes(mentor_id);


