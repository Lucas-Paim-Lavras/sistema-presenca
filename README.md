# Sistema de Controle de Presença

Um sistema completo para gerenciamento de presenças desenvolvido com Java Spring Boot, PostgreSQL e React, incluindo controle de presença para alunos e mentores do YouX Lab.

## 📋 Visão Geral

Este sistema permite o controle eficiente de presenças em turmas e de mentores, oferecendo funcionalidades completas de CRUD (Create, Read, Update, Delete) para turmas, alunos, mentores e registros de presença através de chamadas, além de um dashboard analítico e sistema de relatórios.

## 🚀 Funcionalidades Principais

### 📚 Gestão de Turmas
- Cadastro, edição e exclusão de turmas
- Código único para cada turma
- Status ativo/inativo
- Busca por nome ou código

### 👨‍🎓 Gestão de Alunos
- Cadastro completo com nome, matrícula e email
- Vinculação a turmas específicas
- Status ativo/inativo
- Busca por nome, matrícula ou email
- Filtros por turma

### 👨‍🏫 Gestão de Mentores
- Cadastro completo com nome, email e tipo de mentor
- Tipos: Mentor, Mentor-trainee, Mentor Coordenador
- Status ativo/inativo
- Busca por nome ou email
- Filtros por tipo de mentor

### ✅ Controle de Chamadas de Aula
- Criação de chamadas por turma e data
- Marcação de presença/falta de múltiplos alunos simultaneamente
- Visualização de chamadas anteriores
- Edição e exclusão de chamadas
- Relatórios por aluno e por chamada

### 🎯 Controle de Chamadas de Mentores
- Criação de chamadas de mentores por data
- Marcação de presença/falta de múltiplos mentores simultaneamente
- Visualização de chamadas anteriores de mentores
- Edição e exclusão de chamadas de mentores
- Relatórios por mentor e por chamada

### 📊 Dashboard Analítico
- Estatísticas gerais do sistema (alunos, turmas, mentores)
- Gráficos de barras (alunos por turma, mentores por tipo)
- Gráficos de pizza (distribuição de presenças)
- Tabela resumo por turma e mentores
- Indicadores de performance

### 📈 Sistema de Relatórios
- Exportação em CSV e Excel
- Relatórios de chamadas de alunos com filtros
- Relatórios de chamadas de mentores com filtros
- Relatórios de alunos por turma
- Relatórios de mentores por tipo
- Preview antes da exportação

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Apache POI** (para exportação Excel)

### Frontend
- **React 18**
- **JavaScript (ES6+)**
- **Vite** (build tool)
- **Tailwind CSS**
- **Shadcn/ui** (componentes)
- **Lucide React** (ícones)
- **Recharts** (gráficos)
- **React Router** (navegação)

### Banco de Dados
- **PostgreSQL 12+**
- **pgAdmin** (administração)

## 📁 Estrutura do Projeto

```
controle-presenca/
├── controle-presenca-backend/          # Backend Spring Boot
│   ├── src/main/java/com/controlepresenca/
│   │   ├── entity/                     # Entidades JPA
│   │   │   ├── Turma.java
│   │   │   ├── Aluno.java
│   │   │   ├── Chamada.java
│   │   │   ├── ChamadaAluno.java
│   │   │   ├── Mentor.java
│   │   │   ├── ChamadaMentor.java
│   │   │   └── ChamadaMentorParticipante.java
│   │   ├── dto/                        # Data Transfer Objects
│   │   ├── repository/                 # Repositórios JPA
│   │   ├── service/                    # Lógica de negócio
│   │   └── controller/                 # Controllers REST
│   ├── src/main/resources/
│   │   └── application.properties      # Configurações
│   └── pom.xml                         # Dependências Maven
├── controle-presenca-frontend/         # Frontend React
│   ├── src/
│   │   ├── components/                 # Componentes React
│   │   │   ├── TurmasPage.jsx
│   │   │   ├── AlunosPage.jsx
│   │   │   ├── MentoresPage.jsx
│   │   │   ├── ChamadasPage.jsx
│   │   │   ├── ChamadasMentoresPage.jsx
│   │   │   └── RelatoriosPage.jsx
│   │   ├── services/                   # Serviços de API
│   │   └── App.jsx                     # Componente principal
│   ├── package.json                    # Dependências npm
│   └── vite.config.js                  # Configuração Vite
├── database_setup.sql                  # Script de criação do banco
└── README.md                           # Este arquivo
```

## 🔧 Configuração e Instalação

### Pré-requisitos

- **Java 17** ou superior
- **Node.js 18** ou superior
- **PostgreSQL 12** ou superior
- **pgAdmin** (opcional, para administração visual)
- **Maven 3.6** ou superior

### 1. Configuração do Banco de Dados

1. Instale e configure o PostgreSQL
2. Crie um banco de dados chamado `controle_presenca`
3. Execute o script SQL fornecido:

```bash
psql -U postgres -d controle_presenca -f database_setup.sql
```

### 2. Configuração do Backend

1. Navegue até a pasta do backend:
```bash
cd controle-presenca-backend
```

2. Configure as credenciais do banco no arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/controle_presenca
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

3. Instale as dependências e execute:
```bash
mvn clean install
mvn spring-boot:run
```

O backend estará disponível em: `http://localhost:8080`

### 3. Configuração do Frontend

1. Navegue até a pasta do frontend:
```bash
cd controle-presenca-frontend
```

2. Instale as dependências:
```bash
npm install --legacy-peer-deps
# ou
pnpm install
```

3. Execute o servidor de desenvolvimento:
```bash
npm run dev
# ou
pnpm run dev
```

O frontend estará disponível em: `http://localhost:5173`

## 🔌 API Endpoints

### Turmas
- `GET /api/turmas` - Listar turmas ativas
- `GET /api/turmas/todas` - Listar todas as turmas
- `GET /api/turmas/{id}` - Buscar turma por ID
- `POST /api/turmas` - Criar nova turma
- `PUT /api/turmas/{id}` - Atualizar turma
- `DELETE /api/turmas/{id}` - Remover turma (soft delete)

### Alunos
- `GET /api/alunos` - Listar alunos ativos
- `GET /api/alunos/turma/{turmaId}` - Listar alunos por turma
- `GET /api/alunos/{id}` - Buscar aluno por ID
- `POST /api/alunos` - Criar novo aluno
- `PUT /api/alunos/{id}` - Atualizar aluno
- `DELETE /api/alunos/{id}` - Remover aluno (soft delete)

### Mentores
- `GET /api/mentores` - Listar mentores ativos
- `GET /api/mentores/todos` - Listar todos os mentores
- `GET /api/mentores/tipo/{tipo}` - Listar mentores por tipo
- `GET /api/mentores/{id}` - Buscar mentor por ID
- `POST /api/mentores` - Criar novo mentor
- `PUT /api/mentores/{id}` - Atualizar mentor
- `DELETE /api/mentores/{id}` - Remover mentor (soft delete)

### Chamadas de Aula
- `GET /api/chamadas` - Listar todas as chamadas
- `GET /api/chamadas/turma/{turmaId}` - Listar chamadas por turma
- `GET /api/chamadas/{id}` - Buscar chamada por ID
- `POST /api/chamadas` - Criar nova chamada
- `PUT /api/chamadas/{id}` - Atualizar chamada
- `DELETE /api/chamadas/{id}` - Remover chamada

### Chamadas de Mentores
- `GET /api/chamadas-mentores` - Listar todas as chamadas de mentores
- `GET /api/chamadas-mentores/data/{data}` - Listar chamadas por data
- `GET /api/chamadas-mentores/{id}` - Buscar chamada por ID
- `POST /api/chamadas-mentores` - Criar nova chamada de mentores
- `PUT /api/chamadas-mentores/{id}` - Atualizar chamada de mentores
- `DELETE /api/chamadas-mentores/{id}` - Remover chamada de mentores

### Relatórios
- `GET /api/relatorios/chamadas/csv` - Exportar chamadas em CSV
- `GET /api/relatorios/chamadas/excel` - Exportar chamadas em Excel
- `GET /api/relatorios/mentores/csv` - Exportar mentores em CSV
- `GET /api/relatorios/mentores/excel` - Exportar mentores em Excel

## 💡 Como Usar

### 1. Primeiro Acesso
1. Acesse o sistema pelo navegador: `http://localhost:5173`
2. Comece cadastrando turmas na seção "Turmas"
3. Cadastre alunos vinculados às turmas na seção "Alunos"
4. Cadastre mentores na seção "Mentores"
5. Crie chamadas de aula na seção "Chamadas"
6. Crie chamadas de mentores na seção "Chamadas de Mentores"

### 2. Registro de Chamadas de Aula
- Acesse "Chamadas" no menu lateral
- Clique em "Nova Chamada"
- Selecione a turma e a data
- Marque presença/falta para cada aluno
- Adicione observações se necessário
- Salve a chamada

### 3. Registro de Chamadas de Mentores
- Acesse "Chamadas de Mentores" no menu lateral
- Clique em "Nova Chamada"
- Selecione a data
- Marque presença/falta para cada mentor
- Adicione observações se necessário
- Salve a chamada

### 4. Dashboard
- Visualize estatísticas gerais na página inicial
- Acompanhe gráficos de distribuição
- Monitore o resumo por turma e mentores

### 5. Relatórios
- Acesse a seção "Relatórios"
- Use filtros para personalizar os dados
- Visualize preview antes de exportar
- Exporte em CSV ou Excel conforme necessário

## 🔍 Funcionalidades Avançadas

### Busca e Filtros
- Busca textual em todas as listagens
- Filtros por turma, data, tipo de mentor e status
- Combinação de múltiplos filtros

### Validações
- Validação de campos obrigatórios
- Verificação de emails válidos
- Prevenção de matrículas duplicadas
- Validação de datas e horários
- Prevenção de emails duplicados para mentores

### Responsividade
- Interface adaptável para desktop e mobile
- Navegação otimizada para dispositivos móveis
- Tabelas com scroll horizontal em telas pequenas

### Tipos de Mentor
- **Mentor**: Mentor padrão
- **Mentor-trainee**: Mentor em treinamento
- **Mentor Coordenador**: Mentor com função de coordenação

## 🛡️ Segurança e Boas Práticas

### Backend
- Validação de dados de entrada
- Tratamento de exceções
- Soft delete para preservar histórico
- Logs de operações importantes
- Validação de tipos de mentor

### Frontend
- Validação client-side
- Tratamento de erros de API
- Loading states para melhor UX
- Confirmações para operações críticas
- Modais com fundo transparente

## 🐛 Solução de Problemas

### Problemas Comuns

1. **Erro de conexão com banco de dados**
   - Verifique se o PostgreSQL está rodando
   - Confirme as credenciais no `application.properties`
   - Teste a conexão com pgAdmin

2. **Frontend não carrega dados**
   - Verifique se o backend está rodando na porta 8080
   - Confirme se não há erros no console do navegador
   - Teste os endpoints diretamente

3. **Erro ao instalar dependências do frontend**
   - Use `npm install --legacy-peer-deps` para resolver conflitos de dependências
   - Limpe o cache com `npm cache clean --force`

4. **Erro ao exportar relatórios**
   - Verifique se há dados para exportar
   - Confirme se os filtros estão corretos
   - Teste com um conjunto menor de dados

### Logs e Debug

- **Backend**: Logs disponíveis no console da aplicação Spring Boot
- **Frontend**: Use o Developer Tools do navegador (F12)
- **Banco**: Monitore queries com pgAdmin

## 📞 Suporte

Para dúvidas ou problemas:

1. Verifique a seção de solução de problemas
2. Consulte os logs da aplicação
3. Teste os endpoints individualmente
4. Verifique a configuração do banco de dados

## 📄 Licença

Este projeto é fornecido como exemplo educacional e pode ser usado livremente para fins de aprendizado e desenvolvimento.

---

**Desenvolvido com ❤️ usando Java Spring Boot, PostgreSQL e React para o YouX Lab**

