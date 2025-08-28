# 📚 Documentação da API - Sistema de Controle de Presença

Esta documentação descreve todos os endpoints disponíveis na API REST do Sistema de Controle de Presença.

## 🔗 Base URL

```
http://localhost:8080/api
```

## 📋 Formato de Resposta

Todas as respostas da API seguem o padrão JSON. Em caso de erro, a estrutura de resposta é:

```json
{
  "error": "Mensagem de erro",
  "status": 400,
  "timestamp": "2024-01-01T10:00:00Z"
}
```

## 🏫 Endpoints - Turmas

### Listar Turmas Ativas

**GET** `/turmas`

Lista todas as turmas com status ativo.

**Resposta de Sucesso (200):**
```json
[
  {
    "id": 1,
    "nome": "Matemática Básica",
    "codigo": "MAT001",
    "descricao": "Turma de matemática para iniciantes",
    "ativa": true,
    "dataCriacao": "2024-01-01T10:00:00Z",
    "totalAlunos": 25,
    "totalPresencas": 150
  }
]
```

### Listar Todas as Turmas

**GET** `/turmas/todas`

Lista todas as turmas, incluindo inativas.

### Buscar Turma por ID

**GET** `/turmas/{id}`

**Parâmetros:**
- `id` (path): ID da turma

**Resposta de Sucesso (200):**
```json
{
  "id": 1,
  "nome": "Matemática Básica",
  "codigo": "MAT001",
  "descricao": "Turma de matemática para iniciantes",
  "ativa": true,
  "dataCriacao": "2024-01-01T10:00:00Z",
  "totalAlunos": 25,
  "totalPresencas": 150
}
```

**Resposta de Erro (404):**
```json
{
  "error": "Turma não encontrada",
  "status": 404
}
```

### Buscar Turma por Código

**GET** `/turmas/codigo/{codigo}`

**Parâmetros:**
- `codigo` (path): Código da turma

### Buscar Turmas por Nome

**GET** `/turmas/buscar?nome={nome}`

**Parâmetros de Query:**
- `nome` (string): Nome ou parte do nome da turma

### Criar Nova Turma

**POST** `/turmas`

**Corpo da Requisição:**
```json
{
  "nome": "Matemática Básica",
  "codigo": "MAT001",
  "descricao": "Turma de matemática para iniciantes",
  "ativa": true
}
```

**Campos Obrigatórios:**
- `nome`: Nome da turma (máx. 100 caracteres)
- `codigo`: Código único da turma (máx. 20 caracteres)

**Campos Opcionais:**
- `descricao`: Descrição da turma (máx. 500 caracteres)
- `ativa`: Status da turma (padrão: true)

**Resposta de Sucesso (201):**
```json
{
  "id": 1,
  "nome": "Matemática Básica",
  "codigo": "MAT001",
  "descricao": "Turma de matemática para iniciantes",
  "ativa": true,
  "dataCriacao": "2024-01-01T10:00:00Z"
}
```

### Atualizar Turma

**PUT** `/turmas/{id}`

**Parâmetros:**
- `id` (path): ID da turma

**Corpo da Requisição:** (mesmo formato do POST)

### Remover Turma (Soft Delete)

**DELETE** `/turmas/{id}`

**Parâmetros:**
- `id` (path): ID da turma

**Resposta de Sucesso (204):** Sem conteúdo

### Excluir Turma Permanentemente

**DELETE** `/turmas/{id}/permanente`

**⚠️ Atenção:** Esta operação remove permanentemente a turma e todos os dados relacionados.

## 👨‍🎓 Endpoints - Alunos

### Listar Alunos Ativos

**GET** `/alunos`

**Resposta de Sucesso (200):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "matricula": "2024001",
    "email": "joao.silva@email.com",
    "turmaId": 1,
    "turmaNome": "Matemática Básica",
    "turmaCodigo": "MAT001",
    "ativo": true,
    "dataCadastro": "2024-01-01T10:00:00Z",
    "totalPresencas": 8
  }
]
```

### Listar Todos os Alunos

**GET** `/alunos/todos`

### Listar Alunos por Turma

**GET** `/alunos/turma/{turmaId}`

**Parâmetros:**
- `turmaId` (path): ID da turma

### Buscar Aluno por ID

**GET** `/alunos/{id}`

### Buscar Aluno por Matrícula

**GET** `/alunos/matricula/{matricula}`

### Buscar Aluno por Email

**GET** `/alunos/email/{email}`

### Buscar Alunos por Nome

**GET** `/alunos/buscar?nome={nome}`

### Buscar Alunos por Turma e Nome

**GET** `/alunos/turma/{turmaId}/buscar?nome={nome}`

### Criar Novo Aluno

**POST** `/alunos`

**Corpo da Requisição:**
```json
{
  "nome": "João Silva",
  "matricula": "2024001",
  "email": "joao.silva@email.com",
  "turmaId": 1,
  "ativo": true
}
```

**Campos Obrigatórios:**
- `nome`: Nome completo (máx. 100 caracteres)
- `matricula`: Matrícula única (máx. 20 caracteres)
- `email`: Email válido (máx. 100 caracteres)
- `turmaId`: ID da turma existente

**Campos Opcionais:**
- `ativo`: Status do aluno (padrão: true)

**Validações:**
- Email deve ter formato válido
- Matrícula deve ser única
- Turma deve existir e estar ativa

### Atualizar Aluno

**PUT** `/alunos/{id}`

### Remover Aluno (Soft Delete)

**DELETE** `/alunos/{id}`

### Excluir Aluno Permanentemente

**DELETE** `/alunos/{id}/permanente`

## ✅ Endpoints - Presenças

### Listar Todas as Presenças

**GET** `/presencas`

**Resposta de Sucesso (200):**
```json
[
  {
    "id": 1,
    "alunoId": 1,
    "alunoNome": "João Silva",
    "alunoMatricula": "2024001",
    "turmaId": 1,
    "turmaNome": "Matemática Básica",
    "turmaCodigo": "MAT001",
    "dataPresenca": "2024-01-15",
    "horaPresenca": "08:30:00",
    "observacoes": "Presente na aula",
    "dataRegistro": "2024-01-15T08:30:00Z"
  }
]
```

### Listar Presenças por Turma

**GET** `/presencas/turma/{turmaId}`

### Listar Presenças por Aluno

**GET** `/presencas/aluno/{alunoId}`

### Listar Presenças por Data

**GET** `/presencas/data/{data}`

**Parâmetros:**
- `data` (path): Data no formato YYYY-MM-DD

### Listar Presenças por Turma e Data

**GET** `/presencas/turma/{turmaId}/data/{data}`

### Listar Presenças por Período

**GET** `/presencas/periodo?dataInicio={dataInicio}&dataFim={dataFim}`

**Parâmetros de Query:**
- `dataInicio` (string): Data inicial (YYYY-MM-DD)
- `dataFim` (string): Data final (YYYY-MM-DD)

### Buscar Presença por ID

**GET** `/presencas/{id}`

### Registrar Nova Presença

**POST** `/presencas`

**Corpo da Requisição:**
```json
{
  "alunoId": 1,
  "turmaId": 1,
  "dataPresenca": "2024-01-15",
  "horaPresenca": "08:30:00",
  "observacoes": "Presente na aula"
}
```

**Campos Obrigatórios:**
- `alunoId`: ID do aluno existente
- `turmaId`: ID da turma existente
- `dataPresenca`: Data da presença (YYYY-MM-DD)
- `horaPresenca`: Hora da presença (HH:MM:SS)

**Campos Opcionais:**
- `observacoes`: Observações sobre a presença (máx. 500 caracteres)

**Validações:**
- Aluno deve existir e estar ativo
- Turma deve existir e estar ativa
- Aluno deve pertencer à turma especificada
- Data não pode ser futura
- Não pode haver duplicata (mesmo aluno, turma e data)

### Registrar Presença Rápida

**POST** `/presencas/rapida`

Registra presença com data e hora atuais.

**Corpo da Requisição:**
```json
{
  "alunoId": 1,
  "turmaId": 1
}
```

### Atualizar Presença

**PUT** `/presencas/{id}`

### Remover Presença

**DELETE** `/presencas/{id}`

### Gerar Relatório de Presenças

**GET** `/presencas/relatorio?turmaId={turmaId}&dataInicio={dataInicio}&dataFim={dataFim}`

**Parâmetros de Query (todos opcionais):**
- `turmaId` (number): ID da turma
- `dataInicio` (string): Data inicial (YYYY-MM-DD)
- `dataFim` (string): Data final (YYYY-MM-DD)

### Contar Presenças por Turma

**GET** `/presencas/turma/{turmaId}/contar`

**Resposta:**
```json
{
  "turmaId": 1,
  "totalPresencas": 150,
  "totalAlunos": 25,
  "mediaPresencas": 6.0
}
```

### Contar Presenças por Aluno

**GET** `/presencas/aluno/{alunoId}/contar`

## 📊 Endpoints - Relatórios

### Exportar Presenças em CSV

**GET** `/relatorios/presencas/csv?turmaId={turmaId}&dataInicio={dataInicio}&dataFim={dataFim}`

**Resposta:** Arquivo CSV para download

**Headers da Resposta:**
```
Content-Type: text/csv
Content-Disposition: attachment; filename="relatorio-presencas-YYYY-MM-DD.csv"
```

### Exportar Presenças em Excel

**GET** `/relatorios/presencas/excel?turmaId={turmaId}&dataInicio={dataInicio}&dataFim={dataFim}`

**Resposta:** Arquivo Excel (.xlsx) para download

**Headers da Resposta:**
```
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
Content-Disposition: attachment; filename="relatorio-presencas-YYYY-MM-DD.xlsx"
```

### Exportar Alunos em CSV

**GET** `/relatorios/alunos/csv?turmaId={turmaId}`

### Exportar Alunos em Excel

**GET** `/relatorios/alunos/excel?turmaId={turmaId}`

### Exportar Turmas em CSV

**GET** `/relatorios/turmas/csv`

### Exportar Turmas em Excel

**GET** `/relatorios/turmas/excel`

## 🔒 Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado com sucesso |
| 204 | No Content - Operação bem-sucedida sem conteúdo |
| 400 | Bad Request - Dados inválidos |
| 404 | Not Found - Recurso não encontrado |
| 409 | Conflict - Conflito (ex: matrícula duplicada) |
| 500 | Internal Server Error - Erro interno do servidor |

## 🛡️ Validações e Regras de Negócio

### Turmas
- Nome é obrigatório (máx. 100 caracteres)
- Código é obrigatório e único (máx. 20 caracteres)
- Descrição é opcional (máx. 500 caracteres)
- Turmas inativas não podem receber novos alunos ou presenças

### Alunos
- Nome é obrigatório (máx. 100 caracteres)
- Matrícula é obrigatória e única (máx. 20 caracteres)
- Email é obrigatório e deve ter formato válido (máx. 100 caracteres)
- Deve pertencer a uma turma ativa
- Alunos inativos não podem ter presenças registradas

### Presenças
- Aluno e turma são obrigatórios
- Aluno deve pertencer à turma especificada
- Data e hora são obrigatórias
- Data não pode ser futura
- Não pode haver duplicatas (mesmo aluno, turma e data)
- Observações são opcionais (máx. 500 caracteres)

## 📝 Exemplos de Uso

### Criar uma Turma e Aluno

```bash
# 1. Criar turma
curl -X POST http://localhost:8080/api/turmas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Matemática Básica",
    "codigo": "MAT001",
    "descricao": "Turma de matemática para iniciantes"
  }'

# 2. Criar aluno
curl -X POST http://localhost:8080/api/alunos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "matricula": "2024001",
    "email": "joao.silva@email.com",
    "turmaId": 1
  }'

# 3. Registrar presença
curl -X POST http://localhost:8080/api/presencas \
  -H "Content-Type: application/json" \
  -d '{
    "alunoId": 1,
    "turmaId": 1,
    "dataPresenca": "2024-01-15",
    "horaPresenca": "08:30:00"
  }'
```

### Buscar Dados com Filtros

```bash
# Buscar alunos por turma
curl http://localhost:8080/api/alunos/turma/1

# Buscar presenças por período
curl "http://localhost:8080/api/presencas/periodo?dataInicio=2024-01-01&dataFim=2024-01-31"

# Exportar relatório CSV
curl -o relatorio.csv "http://localhost:8080/api/relatorios/presencas/csv?turmaId=1"
```

## 🔧 Configurações CORS

A API está configurada para aceitar requisições do frontend na porta 5173:

```properties
spring.web.cors.allowed-origins=http://localhost:5173
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
```

Para integração com outros frontends, ajuste essas configurações no `application.properties`.

## 📚 Recursos Adicionais

- **Swagger/OpenAPI**: Considere adicionar documentação interativa
- **Paginação**: Para grandes volumes de dados
- **Autenticação**: Para ambientes de produção
- **Rate Limiting**: Para controle de acesso
- **Logs**: Monitoramento de uso da API

---

**📖 Esta documentação cobre todos os endpoints disponíveis na API. Para dúvidas específicas, consulte o código-fonte ou entre em contato com a equipe de desenvolvimento.**



## 👨‍🏫 Endpoints - Mentores

### Listar Mentores Ativos

**GET** `/mentores`

Lista todos os mentores com status ativo.

**Resposta de Sucesso (200):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao.silva@youxlab.com",
    "tipo": "MENTOR_COORDENADOR",
    "ativo": true,
    "dataCriacao": "2024-01-01T10:00:00Z"
  }
]
```

### Listar Todos os Mentores

**GET** `/mentores/todos`

Lista todos os mentores, incluindo os inativos.

**Resposta de Sucesso (200):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao.silva@youxlab.com",
    "tipo": "MENTOR_COORDENADOR",
    "ativo": true,
    "dataCriacao": "2024-01-01T10:00:00Z"
  },
  {
    "id": 2,
    "nome": "Maria Santos",
    "email": "maria.santos@youxlab.com",
    "tipo": "MENTOR",
    "ativo": false,
    "dataCriacao": "2024-01-01T11:00:00Z"
  }
]
```

### Listar Mentores por Tipo

**GET** `/mentores/tipo/{tipo}`

Lista mentores filtrados por tipo.

**Parâmetros:**
- `tipo` (string): Tipo do mentor (MENTOR, MENTOR_TRAINEE, MENTOR_COORDENADOR)

**Resposta de Sucesso (200):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao.silva@youxlab.com",
    "tipo": "MENTOR_COORDENADOR",
    "ativo": true,
    "dataCriacao": "2024-01-01T10:00:00Z"
  }
]
```

### Buscar Mentor por ID

**GET** `/mentores/{id}`

Busca um mentor específico pelo ID.

**Parâmetros:**
- `id` (integer): ID do mentor

**Resposta de Sucesso (200):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@youxlab.com",
  "tipo": "MENTOR_COORDENADOR",
  "ativo": true,
  "dataCriacao": "2024-01-01T10:00:00Z"
}
```

**Resposta de Erro (404):**
```json
{
  "error": "Mentor não encontrado",
  "status": 404,
  "timestamp": "2024-01-01T10:00:00Z"
}
```

### Buscar Mentor por Email

**GET** `/mentores/email/{email}`

Busca um mentor específico pelo email.

**Parâmetros:**
- `email` (string): Email do mentor

**Resposta de Sucesso (200):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@youxlab.com",
  "tipo": "MENTOR_COORDENADOR",
  "ativo": true,
  "dataCriacao": "2024-01-01T10:00:00Z"
}
```

### Criar Novo Mentor

**POST** `/mentores`

Cria um novo mentor.

**Corpo da Requisição:**
```json
{
  "nome": "João Silva",
  "email": "joao.silva@youxlab.com",
  "tipo": "MENTOR_COORDENADOR"
}
```

**Resposta de Sucesso (201):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@youxlab.com",
  "tipo": "MENTOR_COORDENADOR",
  "ativo": true,
  "dataCriacao": "2024-01-01T10:00:00Z"
}
```

**Resposta de Erro (400):**
```json
{
  "error": "Email já está em uso",
  "status": 400,
  "timestamp": "2024-01-01T10:00:00Z"
}
```

### Atualizar Mentor

**PUT** `/mentores/{id}`

Atualiza os dados de um mentor existente.

**Parâmetros:**
- `id` (integer): ID do mentor

**Corpo da Requisição:**
```json
{
  "nome": "João Silva Santos",
  "email": "joao.santos@youxlab.com",
  "tipo": "MENTOR_COORDENADOR"
}
```

**Resposta de Sucesso (200):**
```json
{
  "id": 1,
  "nome": "João Silva Santos",
  "email": "joao.santos@youxlab.com",
  "tipo": "MENTOR_COORDENADOR",
  "ativo": true,
  "dataCriacao": "2024-01-01T10:00:00Z"
}
```

### Remover Mentor

**DELETE** `/mentores/{id}`

Remove um mentor (soft delete - marca como inativo).

**Parâmetros:**
- `id` (integer): ID do mentor

**Resposta de Sucesso (204):**
Sem conteúdo.

**Resposta de Erro (404):**
```json
{
  "error": "Mentor não encontrado",
  "status": 404,
  "timestamp": "2024-01-01T10:00:00Z"
}
```

## 🎯 Endpoints - Chamadas de Mentores

### Listar Chamadas de Mentores

**GET** `/chamadas-mentores`

Lista todas as chamadas de mentores.

**Parâmetros de Query (opcionais):**
- `dataInicio` (string): Data de início no formato YYYY-MM-DD
- `dataFim` (string): Data de fim no formato YYYY-MM-DD

**Resposta de Sucesso (200):**
```json
[
  {
    "id": 1,
    "data": "2024-01-01",
    "observacoes": "Reunião de planejamento",
    "totalMentores": 5,
    "totalPresentes": 4,
    "totalAusentes": 1,
    "porcentagemPresenca": 80.0,
    "participantes": [
      {
        "id": 1,
        "mentorId": 1,
        "mentorNome": "João Silva",
        "mentorTipo": "MENTOR_COORDENADOR",
        "presente": true,
        "observacoes": null
      }
    ]
  }
]
```

### Buscar Chamada de Mentores por ID

**GET** `/chamadas-mentores/{id}`

Busca uma chamada específica pelo ID.

**Parâmetros:**
- `id` (integer): ID da chamada

**Resposta de Sucesso (200):**
```json
{
  "id": 1,
  "data": "2024-01-01",
  "observacoes": "Reunião de planejamento",
  "totalMentores": 5,
  "totalPresentes": 4,
  "totalAusentes": 1,
  "porcentagemPresenca": 80.0,
  "participantes": [
    {
      "id": 1,
      "mentorId": 1,
      "mentorNome": "João Silva",
      "mentorTipo": "MENTOR_COORDENADOR",
      "presente": true,
      "observacoes": null
    }
  ]
}
```

### Buscar Chamada por Data

**GET** `/chamadas-mentores/data/{data}`

Busca uma chamada específica pela data.

**Parâmetros:**
- `data` (string): Data no formato YYYY-MM-DD

**Resposta de Sucesso (200):**
```json
{
  "id": 1,
  "data": "2024-01-01",
  "observacoes": "Reunião de planejamento",
  "totalMentores": 5,
  "totalPresentes": 4,
  "totalAusentes": 1,
  "porcentagemPresenca": 80.0,
  "participantes": [
    {
      "id": 1,
      "mentorId": 1,
      "mentorNome": "João Silva",
      "mentorTipo": "MENTOR_COORDENADOR",
      "presente": true,
      "observacoes": null
    }
  ]
}
```

### Listar Chamadas por Período

**GET** `/chamadas-mentores/periodo`

Lista chamadas de mentores em um período específico.

**Parâmetros de Query:**
- `dataInicio` (string): Data de início no formato YYYY-MM-DD
- `dataFim` (string): Data de fim no formato YYYY-MM-DD

**Resposta de Sucesso (200):**
```json
[
  {
    "id": 1,
    "data": "2024-01-01",
    "observacoes": "Reunião de planejamento",
    "totalMentores": 5,
    "totalPresentes": 4,
    "totalAusentes": 1,
    "porcentagemPresenca": 80.0
  }
]
```

### Criar Nova Chamada de Mentores

**POST** `/chamadas-mentores`

Cria uma nova chamada de mentores.

**Corpo da Requisição:**
```json
{
  "data": "2024-01-01",
  "observacoes": "Reunião de planejamento",
  "participantes": [
    {
      "mentorId": 1,
      "presente": true,
      "observacoes": null
    },
    {
      "mentorId": 2,
      "presente": false,
      "observacoes": "Ausente por motivo pessoal"
    }
  ]
}
```

**Resposta de Sucesso (201):**
```json
{
  "id": 1,
  "data": "2024-01-01",
  "observacoes": "Reunião de planejamento",
  "totalMentores": 2,
  "totalPresentes": 1,
  "totalAusentes": 1,
  "porcentagemPresenca": 50.0,
  "participantes": [
    {
      "id": 1,
      "mentorId": 1,
      "mentorNome": "João Silva",
      "mentorTipo": "MENTOR_COORDENADOR",
      "presente": true,
      "observacoes": null
    },
    {
      "id": 2,
      "mentorId": 2,
      "mentorNome": "Maria Santos",
      "mentorTipo": "MENTOR",
      "presente": false,
      "observacoes": "Ausente por motivo pessoal"
    }
  ]
}
```

**Resposta de Erro (400):**
```json
{
  "error": "Já existe uma chamada para esta data",
  "status": 400,
  "timestamp": "2024-01-01T10:00:00Z"
}
```

### Atualizar Chamada de Mentores

**PUT** `/chamadas-mentores/{id}`

Atualiza uma chamada de mentores existente.

**Parâmetros:**
- `id` (integer): ID da chamada

**Corpo da Requisição:**
```json
{
  "data": "2024-01-01",
  "observacoes": "Reunião de planejamento - atualizada",
  "participantes": [
    {
      "mentorId": 1,
      "presente": true,
      "observacoes": null
    },
    {
      "mentorId": 2,
      "presente": true,
      "observacoes": "Chegou atrasado"
    }
  ]
}
```

**Resposta de Sucesso (200):**
```json
{
  "id": 1,
  "data": "2024-01-01",
  "observacoes": "Reunião de planejamento - atualizada",
  "totalMentores": 2,
  "totalPresentes": 2,
  "totalAusentes": 0,
  "porcentagemPresenca": 100.0,
  "participantes": [
    {
      "id": 1,
      "mentorId": 1,
      "mentorNome": "João Silva",
      "mentorTipo": "MENTOR_COORDENADOR",
      "presente": true,
      "observacoes": null
    },
    {
      "id": 2,
      "mentorId": 2,
      "mentorNome": "Maria Santos",
      "mentorTipo": "MENTOR",
      "presente": true,
      "observacoes": "Chegou atrasado"
    }
  ]
}
```

### Remover Chamada de Mentores

**DELETE** `/chamadas-mentores/{id}`

Remove uma chamada de mentores.

**Parâmetros:**
- `id` (integer): ID da chamada

**Resposta de Sucesso (204):**
Sem conteúdo.

**Resposta de Erro (404):**
```json
{
  "error": "Chamada não encontrada",
  "status": 404,
  "timestamp": "2024-01-01T10:00:00Z"
}
```

### Obter Estatísticas de Chamadas de Mentores

**GET** `/chamadas-mentores/estatisticas`

Obtém estatísticas gerais das chamadas de mentores.

**Resposta de Sucesso (200):**
```json
{
  "totalChamadas": 10,
  "totalMentoresUnicos": 5,
  "mediaPresencaPorChamada": 4.2,
  "porcentagemPresencaGeral": 84.0,
  "estatisticasPorTipo": {
    "MENTOR": {
      "total": 3,
      "presencas": 25,
      "faltas": 5,
      "porcentagemPresenca": 83.3
    },
    "MENTOR_TRAINEE": {
      "total": 1,
      "presencas": 8,
      "faltas": 2,
      "porcentagemPresenca": 80.0
    },
    "MENTOR_COORDENADOR": {
      "total": 1,
      "presencas": 10,
      "faltas": 0,
      "porcentagemPresenca": 100.0
    }
  }
}
```

## 📊 Endpoints - Relatórios (Atualizados)

### Exportar Mentores em CSV

**GET** `/relatorios/mentores/csv`

Exporta a lista de mentores em formato CSV.

**Parâmetros de Query (opcionais):**
- `tipo` (string): Filtrar por tipo de mentor
- `ativo` (boolean): Filtrar por status ativo/inativo

**Resposta de Sucesso (200):**
```
Content-Type: text/csv
Content-Disposition: attachment; filename="mentores.csv"

ID,Nome,Email,Tipo,Ativo,Data de Criação
1,João Silva,joao.silva@youxlab.com,MENTOR_COORDENADOR,true,2024-01-01T10:00:00Z
```

### Exportar Mentores em Excel

**GET** `/relatorios/mentores/excel`

Exporta a lista de mentores em formato Excel.

**Parâmetros de Query (opcionais):**
- `tipo` (string): Filtrar por tipo de mentor
- `ativo` (boolean): Filtrar por status ativo/inativo

**Resposta de Sucesso (200):**
```
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
Content-Disposition: attachment; filename="mentores.xlsx"
```

### Exportar Chamadas de Mentores em CSV

**GET** `/relatorios/chamadas-mentores/csv`

Exporta as chamadas de mentores em formato CSV.

**Parâmetros de Query (opcionais):**
- `dataInicio` (string): Data de início no formato YYYY-MM-DD
- `dataFim` (string): Data de fim no formato YYYY-MM-DD
- `tipo` (string): Filtrar por tipo de mentor

**Resposta de Sucesso (200):**
```
Content-Type: text/csv
Content-Disposition: attachment; filename="chamadas-mentores.csv"

Data,Mentor,Tipo,Presente,Observações
2024-01-01,João Silva,MENTOR_COORDENADOR,Sim,
2024-01-01,Maria Santos,MENTOR,Não,Ausente por motivo pessoal
```

### Exportar Chamadas de Mentores em Excel

**GET** `/relatorios/chamadas-mentores/excel`

Exporta as chamadas de mentores em formato Excel.

**Parâmetros de Query (opcionais):**
- `dataInicio` (string): Data de início no formato YYYY-MM-DD
- `dataFim` (string): Data de fim no formato YYYY-MM-DD
- `tipo` (string): Filtrar por tipo de mentor

**Resposta de Sucesso (200):**
```
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
Content-Disposition: attachment; filename="chamadas-mentores.xlsx"
```

## 🔍 Códigos de Status HTTP

- **200 OK**: Requisição bem-sucedida
- **201 Created**: Recurso criado com sucesso
- **204 No Content**: Operação bem-sucedida sem conteúdo de retorno
- **400 Bad Request**: Dados inválidos ou requisição malformada
- **404 Not Found**: Recurso não encontrado
- **409 Conflict**: Conflito de dados (ex: email duplicado)
- **500 Internal Server Error**: Erro interno do servidor

## 📝 Tipos de Mentor

Os tipos de mentor disponíveis são:

- **MENTOR**: Mentor padrão
- **MENTOR_TRAINEE**: Mentor em treinamento
- **MENTOR_COORDENADOR**: Mentor com função de coordenação

## 🔒 Validações

### Mentores
- **Nome**: Obrigatório, mínimo 2 caracteres
- **Email**: Obrigatório, formato válido, único no sistema
- **Tipo**: Obrigatório, deve ser um dos tipos válidos

### Chamadas de Mentores
- **Data**: Obrigatória, não pode ser futura
- **Participantes**: Pelo menos um mentor deve ser incluído
- **Mentor ID**: Deve existir e estar ativo

## 📚 Exemplos de Uso

### Criar um Mentor Coordenador
```bash
curl -X POST http://localhost:8080/api/mentores \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao.silva@youxlab.com",
    "tipo": "MENTOR_COORDENADOR"
  }'
```

### Criar uma Chamada de Mentores
```bash
curl -X POST http://localhost:8080/api/chamadas-mentores \
  -H "Content-Type: application/json" \
  -d '{
    "data": "2024-01-01",
    "observacoes": "Reunião de planejamento",
    "participantes": [
      {
        "mentorId": 1,
        "presente": true
      },
      {
        "mentorId": 2,
        "presente": false,
        "observacoes": "Ausente por motivo pessoal"
      }
    ]
  }'
```

### Buscar Mentores por Tipo
```bash
curl -X GET http://localhost:8080/api/mentores/tipo/MENTOR_COORDENADOR
```

### Exportar Chamadas de Mentores em CSV
```bash
curl -X GET "http://localhost:8080/api/relatorios/chamadas-mentores/csv?dataInicio=2024-01-01&dataFim=2024-01-31" \
  -o chamadas-mentores.csv
```

