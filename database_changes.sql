-- Adicionar tabela para Chamadas de Aula
CREATE TABLE chamadas (
    id SERIAL PRIMARY KEY,
    turma_id INTEGER NOT NULL REFERENCES turmas(id),
    data_chamada DATE NOT NULL,
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (turma_id, data_chamada) -- Garante que só há uma chamada por turma por dia
);

-- Adicionar tabela para o status de presença/falta dos alunos em uma Chamada
CREATE TABLE chamada_alunos (
    id SERIAL PRIMARY KEY,
    chamada_id INTEGER NOT NULL REFERENCES chamadas(id) ON DELETE CASCADE,
    aluno_id INTEGER NOT NULL REFERENCES alunos(id) ON DELETE CASCADE,
    presente BOOLEAN NOT NULL DEFAULT FALSE,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (chamada_id, aluno_id) -- Garante que só há um registro por aluno por chamada
);

-- Opcional: Migrar dados de presenças individuais para o novo modelo de chamadas
-- Esta parte é mais complexa e pode ser feita em um script de migração separado ou manualmente.
-- Por enquanto, vamos focar na estrutura.

-- Ajustar a tabela presencas (se necessário, para manter compatibilidade ou remover)
-- Por enquanto, vamos manter a tabela presencas, mas ela será usada de forma diferente ou descontinuada.
-- Para este projeto, vamos considerar que a tabela 'presencas' será substituída por 'chamadas' e 'chamada_alunos'.
-- No entanto, para evitar quebrar o sistema existente, vamos desativar a criação de novas presenças individuais
-- e focar na nova funcionalidade.

-- Se a intenção é substituir completamente, o ideal seria:
-- DROP TABLE presencas;

-- Para este exercício, vamos manter a tabela presencas, mas o foco será nas novas tabelas.


