# 🚀 Guia Completo de Instalação - Sistema de Controle de Presença

Este guia fornece instruções detalhadas para configurar e executar o Sistema de Controle de Presença em seu ambiente local.

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter os seguintes softwares instalados:

### 1. Java Development Kit (JDK) 17+
```bash
# Verificar se o Java está instalado
java -version
javac -version

# Se não estiver instalado, baixe do site oficial:
# https://www.oracle.com/java/technologies/downloads/
```

### 2. Node.js 18+
```bash
# Verificar se o Node.js está instalado
node --version
npm --version

# Se não estiver instalado, baixe do site oficial:
# https://nodejs.org/
```

### 3. PostgreSQL 12+
```bash
# Verificar se o PostgreSQL está instalado
psql --version

# Se não estiver instalado:
# Windows: https://www.postgresql.org/download/windows/
# macOS: brew install postgresql
# Ubuntu: sudo apt-get install postgresql postgresql-contrib
```

### 4. Maven 3.6+
```bash
# Verificar se o Maven está instalado
mvn --version

# Se não estiver instalado:
# https://maven.apache.org/download.cgi
```

### 5. pgAdmin (Opcional)
- Interface gráfica para administração do PostgreSQL
- Download: https://www.pgadmin.org/download/

## 🗄️ Configuração do Banco de Dados

### Passo 1: Iniciar o PostgreSQL

**Windows:**
```cmd
# Iniciar o serviço do PostgreSQL
net start postgresql-x64-14
```

**macOS:**
```bash
# Iniciar o PostgreSQL
brew services start postgresql
```

**Linux (Ubuntu):**
```bash
# Iniciar o PostgreSQL
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### Passo 2: Criar o Banco de Dados

1. **Conectar ao PostgreSQL:**
```bash
# Conectar como usuário postgres
sudo -u postgres psql

# Ou no Windows:
psql -U postgres
```

2. **Criar o banco de dados:**
```sql
-- Criar o banco de dados
CREATE DATABASE controle_presenca;

-- Criar um usuário específico (opcional)
CREATE USER controle_user WITH PASSWORD 'controle123';

-- Conceder privilégios
GRANT ALL PRIVILEGES ON DATABASE controle_presenca TO controle_user;

-- Sair do psql
\q
```

### Passo 3: Executar o Script de Criação das Tabelas

1. **Localizar o arquivo `database_setup.sql`** na raiz do projeto

2. **Executar o script:**
```bash
# Opção 1: Usando psql
psql -U postgres -d controle_presenca -f database_setup.sql

# Opção 2: Usando o usuário criado
psql -U controle_user -d controle_presenca -f database_setup.sql
```

3. **Verificar se as tabelas foram criadas:**
```bash
# Conectar ao banco
psql -U postgres -d controle_presenca

# Listar tabelas
\dt

# Você deve ver: turmas, alunos, presencas
```

## ⚙️ Configuração do Backend (Spring Boot)

### Passo 1: Navegar para o Diretório do Backend
```bash
cd controle-presenca-backend
```

### Passo 2: Configurar as Propriedades da Aplicação

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Configurações do Banco de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/controle_presenca
spring.datasource.username=postgres
spring.datasource.password=sua_senha_aqui

# Configurações do JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Configurações do Servidor
server.port=8080

# Configurações CORS (para permitir acesso do frontend)
spring.web.cors.allowed-origins=http://localhost:5173
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
```

### Passo 3: Instalar Dependências e Compilar

```bash
# Limpar e instalar dependências
mvn clean install

# Se houver problemas, tente:
mvn clean install -U
```

### Passo 4: Executar o Backend

```bash
# Executar a aplicação
mvn spring-boot:run

# Ou compilar e executar o JAR
mvn clean package
java -jar target/controle-presenca-0.0.1-SNAPSHOT.jar
```

### Passo 5: Verificar se o Backend Está Funcionando

1. **Verificar no terminal:** Deve aparecer uma mensagem similar a:
```
Started ControlePresencaApplication in X.XXX seconds
```

2. **Testar no navegador:** Acesse `http://localhost:8080/api/turmas`
   - Deve retornar uma lista vazia `[]` (normal, pois não há dados ainda)

3. **Verificar logs:** Não deve haver erros de conexão com o banco

## 🎨 Configuração do Frontend (React)

### Passo 1: Navegar para o Diretório do Frontend
```bash
cd controle-presenca-frontend
```

### Passo 2: Instalar Dependências

```bash
# Usando npm
npm install

# Ou usando pnpm (mais rápido)
npm install -g pnpm
pnpm install

# Ou usando yarn
npm install -g yarn
yarn install
```

### Passo 3: Verificar Configurações da API

Verifique se o arquivo `src/services/api.js` está configurado corretamente:

```javascript
// Configuração da API
const API_BASE_URL = 'http://localhost:8080/api'
```

### Passo 4: Executar o Frontend

```bash
# Usando npm
npm run dev

# Usando pnpm
pnpm run dev

# Usando yarn
yarn dev
```

### Passo 5: Verificar se o Frontend Está Funcionando

1. **Verificar no terminal:** Deve aparecer uma mensagem similar a:
```
Local:   http://localhost:5173/
Network: http://192.168.x.x:5173/
```

2. **Abrir no navegador:** Acesse `http://localhost:5173`
   - Deve carregar a interface do sistema
   - Pode haver mensagens de "Nenhuma turma cadastrada" (normal)

## 🧪 Testando o Sistema Completo

### Passo 1: Verificar Conexão Backend-Frontend

1. **Abrir o Developer Tools** do navegador (F12)
2. **Ir para a aba Console**
3. **Navegar pelas páginas** do sistema
4. **Verificar se há erros** relacionados à API

### Passo 2: Teste Básico de Funcionalidades

1. **Criar uma Turma:**
   - Ir para "Turmas" → "Nova Turma"
   - Preencher: Nome: "Matemática", Código: "MAT001"
   - Salvar

2. **Criar um Aluno:**
   - Ir para "Alunos" → "Novo Aluno"
   - Preencher os dados e selecionar a turma criada
   - Salvar

3. **Registrar Presença:**
   - Ir para "Presenças" → "Nova Presença"
   - Selecionar turma e aluno
   - Registrar

4. **Verificar Dashboard:**
   - Voltar ao Dashboard
   - Verificar se as estatísticas foram atualizadas

## 🔧 Solução de Problemas Comuns

### Problema: Erro de Conexão com o Banco

**Sintomas:**
```
org.postgresql.util.PSQLException: Connection refused
```

**Soluções:**
1. Verificar se o PostgreSQL está rodando
2. Confirmar porta (padrão: 5432)
3. Verificar credenciais no `application.properties`
4. Testar conexão manual: `psql -U postgres -h localhost`

### Problema: Backend Não Inicia

**Sintomas:**
```
Port 8080 was already in use
```

**Soluções:**
1. Matar processo na porta 8080:
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/macOS
lsof -ti:8080 | xargs kill -9
```

2. Ou alterar a porta no `application.properties`:
```properties
server.port=8081
```

### Problema: Frontend Não Carrega Dados

**Sintomas:**
- Páginas em branco ou com "Loading..."
- Erros no console do navegador

**Soluções:**
1. Verificar se o backend está rodando
2. Confirmar URL da API no `api.js`
3. Verificar CORS no backend
4. Limpar cache do navegador (Ctrl+F5)

### Problema: Dependências do Maven

**Sintomas:**
```
Could not resolve dependencies
```

**Soluções:**
1. Limpar cache do Maven:
```bash
mvn dependency:purge-local-repository
mvn clean install
```

2. Verificar conexão com internet
3. Configurar proxy se necessário

### Problema: Dependências do Node.js

**Sintomas:**
```
npm ERR! network timeout
```

**Soluções:**
1. Limpar cache do npm:
```bash
npm cache clean --force
```

2. Usar registry diferente:
```bash
npm install --registry https://registry.npmjs.org/
```

3. Deletar `node_modules` e reinstalar:
```bash
rm -rf node_modules package-lock.json
npm install
```

## 📊 Verificação Final

### Checklist de Funcionamento

- [ ] PostgreSQL rodando e acessível
- [ ] Banco `controle_presenca` criado
- [ ] Tabelas criadas (turmas, alunos, presencas)
- [ ] Backend Spring Boot iniciado na porta 8080
- [ ] Endpoints da API respondendo
- [ ] Frontend React iniciado na porta 5173
- [ ] Interface carregando sem erros
- [ ] Possível criar turmas, alunos e presenças
- [ ] Dashboard mostrando estatísticas
- [ ] Relatórios funcionando

### Comandos de Verificação Rápida

```bash
# Verificar se os serviços estão rodando
netstat -an | grep 5432  # PostgreSQL
netstat -an | grep 8080  # Backend
netstat -an | grep 5173  # Frontend

# Testar endpoints da API
curl http://localhost:8080/api/turmas
curl http://localhost:8080/api/alunos
curl http://localhost:8080/api/presencas
```

## 🎯 Próximos Passos

Após a instalação bem-sucedida:

1. **Explore o sistema** criando dados de teste
2. **Teste todas as funcionalidades** (CRUD, filtros, relatórios)
3. **Configure backup** do banco de dados
4. **Personalize** conforme suas necessidades
5. **Considere deployment** em produção se necessário

---

**🎉 Parabéns! Seu Sistema de Controle de Presença está pronto para uso!**

Para dúvidas adicionais, consulte o arquivo `README.md` ou os comentários no código-fonte.

