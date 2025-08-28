-- Tabela de Mentores
CREATE TABLE mentores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    tipo_mentor VARCHAR(50) NOT NULL, -- ENUM: 'MENTOR', 'MENTOR_TRAINEE', 'MENTOR_COORDENADOR'
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Presenças de Mentores
CREATE TABLE presencas_mentores (
    id BIGSERIAL PRIMARY KEY,
    mentor_id BIGINT NOT NULL REFERENCES mentores(id) ON DELETE CASCADE,
    data_presenca DATE NOT NULL,
    hora_presenca TIME NOT NULL,
    observacoes TEXT,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (mentor_id, data_presenca) -- Um mentor só pode ter uma presença por dia
);

-- Índices para melhor performance
CREATE INDEX idx_mentores_email ON mentores(email);
CREATE INDEX idx_presencas_mentores_mentor_id ON presencas_mentores(mentor_id);
CREATE INDEX idx_presencas_mentores_data ON presencas_mentores(data_presenca);


