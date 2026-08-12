# API de Gerenciamento de Pacientes

Uma aplicação CRUD completa para gerenciar registros de pacientes, construída com backend Java/Spring Boot e frontend React.

## 🚀 Visão Geral do Projeto

Este projeto implementa um Sistema de Gerenciamento de Pacientes abrangente com operações CRUD completas, API RESTful, frontend React moderno e integração com banco de dados. Inclui testes unitários abrangentes, documentação de API com Swagger/OpenAPI e scripts de migração de banco de dados.

## 🛠 Stack Tecnológico

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA** - Abstração de banco de dados
- **Spring Validation** - Validação de entrada
- **H2 Database** - Desenvolvimento/testes (em memória)
- **PostgreSQL** - Banco de dados de produção
- **Flyway** - Migrações de banco de dados
- **SpringDoc OpenAPI** - Documentação de API
- **JUnit 5 & Mockito** - Testes unitários
- **Maven** - Ferramenta de build

### Frontend
- **React 19** - Framework de UI
- **Vite** - Ferramenta de build e servidor de desenvolvimento
- **Axios** - Cliente HTTP
- **React Router DOM** - Roteamento no lado do cliente
- **CSS3** - Estilização

## 📋 Pré-requisitos

Antes de executar esta aplicação, certifique-se de ter instalado:

- **Java 17** ou superior
- **Maven 3.6+** 
- **Node.js 18+** e **npm**
- **PostgreSQL 14+** (para ambiente de produção)
- **Git** (para controle de versão)

## 🏗 Estrutura do Projeto

```
ebserh/
├── backend/                 # Aplicação Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/ebserh/patientapi/
│   │   │   │       ├── controller/      # Controladores REST
│   │   │   │       ├── service/         # Lógica de negócio
│   │   │   │       ├── repository/      # Camada de acesso a dados
│   │   │   │       ├── model/           # Modelos de entidade e DTOs
│   │   │   │       ├── config/          # Classes de configuração
│   │   │   │       └── exception/       # Tratamento de exceções
│   │   │   └── resources/
│   │   │       ├── db/migration/        # Migrações de banco de dados
│   │   │       └── application.properties
│   │   └── test/                        # Testes unitários
│   └── pom.xml
├── frontend/                # Aplicação React
│   ├── src/
│   │   ├── components/      # Componentes React
│   │   ├── api/            # Cliente API
│   │   └── App.jsx
│   ├── package.json
│   └── vite.config.js
└── README.md
```

## 🔧 Instruções de Configuração

### Configuração do Backend

1. **Navegue até o diretório backend:**
   ```bash
   cd backend
   ```

2. **Instale as dependências e construa o projeto:**
   ```bash
   mvn clean install
   ```

3. **Configure o banco de dados:**
   
   Para **desenvolvimento** (banco de dados H2 em memória):
   - Nenhuma configuração adicional necessária
   - O banco de dados é criado automaticamente ao iniciar
   
   Para **produção** (PostgreSQL):
   - Crie um banco de dados PostgreSQL:
     ```sql
     CREATE DATABASE patientdb;
     ```
   - Atualize `src/main/resources/application-prod.properties` com suas credenciais do banco de dados
   - Certifique-se de que o PostgreSQL está rodando e acessível

4. **Execute a aplicação:**
   ```bash
   # Modo desenvolvimento (banco de dados H2)
   mvn spring-boot:run
   
   # Modo produção (PostgreSQL)
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

   O backend será iniciado em `http://localhost:8080`

### Configuração do Frontend

1. **Navegue até o diretório frontend:**
   ```bash
   cd frontend
   ```

2. **Instale as dependências:**
   ```bash
   npm install
   ```

3. **Inicie o servidor de desenvolvimento:**
   ```bash
   npm run dev
   ```

   O frontend será iniciado em `http://localhost:5173`

## 📚 Documentação da API

Uma vez que o backend esteja rodando, acesse a documentação interativa da API:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

### Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-------------|
| POST | `/api/patients` | Criar um novo paciente |
| GET | `/api/patients/{id}` | Obter paciente por ID |
| GET | `/api/patients/cpf/{cpf}` | Obter paciente por CPF |
| GET | `/api/patients` | Obter todos os pacientes (paginado) |
| GET | `/api/patients/search?name={name}` | Buscar pacientes por nome |
| PUT | `/api/patients/{id}` | Atualizar paciente |
| DELETE | `/api/patients/{id}` | Deletar paciente |

### Exemplos de Uso da API

**Criar um paciente:**
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "cpf": "12345678901",
    "email": "joao.silva@example.com",
    "phone": "11987654321",
    "birthDate": "1990-01-01",
    "gender": "Masculino",
    "bloodType": "O+"
  }'
```

**Obter todos os pacientes:**
```bash
curl http://localhost:8080/api/patients?page=0&size=10&sort=name&direction=asc
```

**Atualizar um paciente:**
```bash
curl -X PUT http://localhost:8080/api/patients/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva Atualizado",
    "cpf": "12345678901",
    "email": "joao.atualizado@example.com",
    "phone": "11987654321",
    "birthDate": "1990-01-01"
  }'
```

## 🧪 Testes

### Testes do Backend

Execute os testes unitários:
```bash
cd backend
mvn test
```

A suíte de testes inclui:
- Testes da camada de serviço com Mockito
- Testes de controlador com MockMvc
- Testes de tratamento de exceções
- Testes de validação

### Testes do Frontend

O frontend pode ser testado manualmente através da interface ou adicionando testes automatizados. Atualmente, a aplicação foi projetada para testes manuais através da interface React.

## 🗄️ Banco de Dados

### Desenvolvimento (H2)
- Banco de dados em memória para desenvolvimento e testes
- Criado automaticamente e populado com dados de exemplo
- Acesso ao Console H2: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:patientdb`
  - Usuário: `sa`
  - Senha: (vazia)

### Produção (PostgreSQL)
- Banco de dados persistente para uso em produção
- Migrações Flyway gerenciam o esquema do banco
- Dados de exemplo podem ser carregados via scripts de migração

### Esquema do Banco de Dados

A tabela `patients` inclui:
- Informações básicas: nome, CPF, email, telefone, data de nascimento, gênero
- Endereço: rua, cidade, estado, CEP
- Médico: tipo sanguíneo, alergias, histórico médico
- Emergência: nome do contato e telefone
- Timestamps: created_at, updated_at

## 🔐 Considerações de Segurança

### Implementação Atual
- Validação de entrada em todos os endpoints
- Prevenção de injeção SQL via JPA/Hibernate
- Proteção XSS no frontend React
- Proteção CSRF (pode ser habilitada para produção)

### Recomendações para Produção
- Implementar autenticação e autorização (JWT, OAuth2)
- Adicionar limitação de taxa (rate limiting)
- Habilitar HTTPS
- Implementar configuração CORS
- Adicionar autenticação por chave de API
- Criptografar dados sensíveis em repouso
- Implementar logging de auditoria
- Adicionar headers de segurança

## 📈 Considerações de Escalabilidade

### Arquitetura Atual
- Design de API RESTful
- Suporte a paginação
- Indexação de banco de dados em campos frequentemente consultados
- Design de serviço sem estado

### Recomendações para Escalonamento
- Implementar cache (Redis)
- Adicionar balanceamento de carga
- Implementar réplicas de leitura do banco de dados
- Usar pool de conexões
- Implementar API gateway
- Adicionar monitoramento e alertas
- Considerar arquitetura de microsserviços para maior escala
- Implementar CDN para ativos estáticos

## 🔧 Manutenção

### Qualidade do Código
- Formatação de código consistente
- Testes unitários abrangentes
- Documentação via Swagger/OpenAPI
- Separação clara de responsabilidades

### Recomendações
- Implementar pipeline CI/CD
- Adicionar testes de integração
- Implementar estratégia de logging
- Adicionar monitoramento de performance
- Atualizações regulares de dependências
- Processo de revisão de código
- Atualizações de documentação

## 🐛 Solução de Problemas

### Problemas do Backend

**Porta já em uso:**
```bash
# Altere a porta em application.properties
server.port=8081
```

**Problemas de conexão com banco de dados:**
- Verifique se o PostgreSQL está rodando
- Verifique a string de conexão em application-prod.properties
- Certifique-se de que o banco de dados existe

**Falhas no build:**
```bash
mvn clean install -U
```

### Problemas do Frontend

**Problemas de dependências:**
```bash
rm -rf node_modules package-lock.json
npm install
```

**Conflitos de porta:**
```bash
npm run dev -- --port 3000
```

**Problemas de conexão com API:**
- Verifique se o backend está rodando
- Verifique a configuração de proxy em vite.config.js
- Certifique-se de que o CORS está configurado corretamente

## 📝 Licença

Este projeto foi criado para fins de avaliação técnica.

## 👥 Autores

- Desenvolvido como avaliação técnica para EBSERH

## 🤝 Contribuindo

Este é um projeto de avaliação técnica. Para uso em produção, considere implementar recursos adicionais como:
- Autenticação e autorização de usuários
- Busca e filtragem avançadas
- Upload de arquivos para documentos médicos
- Agendamento de consultas
- Integração com sistemas médicos externos
- Suporte a aplicativo móvel
