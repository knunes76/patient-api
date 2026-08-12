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
│   ├── Dockerfile                       # Docker para produção
│   └── pom.xml
├── frontend/                # Aplicação React
│   ├── src/
│   │   ├── components/      # Componentes React
│   │   ├── api/            # Cliente API
│   │   └── App.jsx
│   ├── Dockerfile                       # Docker para produção
│   ├── nginx.conf                       # Configuração Nginx
│   ├── package.json
│   └── vite.config.js
├── k8s/                     # Manifests Kubernetes
│   ├── configmap.yaml                 # Configurações
│   ├── secret.yaml                    # Secrets
│   ├── postgres-deployment.yaml       # Deploy PostgreSQL
│   ├── postgres-service.yaml          # Service PostgreSQL
│   ├── postgres-pvc.yaml              # Persistência PostgreSQL
│   ├── backend-deployment.yaml        # Deploy Backend
│   ├── backend-service.yaml           # Service Backend
│   ├── backend-hpa.yaml               # Auto-scaling Backend
│   ├── frontend-deployment.yaml      # Deploy Frontend
│   ├── frontend-service.yaml         # Service Frontend
│   ├── ingress.yaml                   # Ingress Controller
│   ├── deploy.sh                      # Script de deployment
│   └── cleanup.sh                     # Script de cleanup
├── docker-compose.yml        # Docker Compose para desenvolvimento
└── README.md
```

## 🐳 Docker

### Docker Compose (Desenvolvimento)

Para rodar toda a aplicação com Docker Compose:

```bash
# Build e iniciar todos os serviços
docker-compose up -d

# Verificar status dos serviços
docker-compose ps

# Ver logs
docker-compose logs -f

# Parar serviços
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

Serviços disponíveis:
- **PostgreSQL**: Porta 5432
- **Backend**: Porta 8081
- **Frontend**: Porta 5173

### Build de Imagens Docker

Para construir as imagens Docker manualmente:

```bash
# Build do backend
cd backend
docker build -t patient-api:latest .

# Build do frontend
cd frontend
docker build -t patient-api-frontend:latest .
```

### Docker Registry

Para push para um registry:

```bash
# Tag da imagem
docker tag patient-api:latest seu-registry/patient-api:latest
docker tag patient-api-frontend:latest seu-registry/patient-api-frontend:latest

# Push
docker push seu-registry/patient-api:latest
docker push seu-registry/patient-api-frontend:latest
```

## ☸️ Kubernetes

### Pré-requisitos

- **kubectl** instalado e configurado
- **Cluster Kubernetes** (minikube, kind, k3s, ou cloud provider)
- **Ingress Controller** (nginx, traefik, etc)

### Deployment

O projeto inclui scripts automatizados para deployment no Kubernetes:

```bash
# Deploy completo
./k8s/deploy.sh

# Verificar status
kubectl get all -l app=patient-api
kubectl get ingress

# Ver logs
kubectl logs -l app=patient-api,component=backend -f
kubectl logs -l app=patient-api,component=frontend -f
```

### Recursos Kubernetes

**ConfigMap e Secret:**
- `patient-api-config`: Configurações da aplicação
- `patient-api-secret`: Senhas e dados sensíveis

**PostgreSQL:**
- **Deployment**: 1 réplica com PersistentVolumeClaim
- **Service**: ClusterIP
- **PVC**: 5GB de armazenamento persistente
- **Health Checks**: Liveness e readiness probes

**Backend:**
- **Deployment**: 3 réplicas (configurável)
- **Service**: ClusterIP
- **HPA**: Auto-scaling de 2-10 réplicas baseado em CPU/Memória
- **Resources**: 512Mi-1Gi RAM, 500m-1000m CPU
- **Health Checks**: Liveness e readiness probes
- **Strategy**: RollingUpdate com zero downtime

**Frontend:**
- **Deployment**: 2 réplicas
- **Service**: LoadBalancer
- **Resources**: 128Mi-256Mi RAM, 100m-200m CPU
- **Health Checks**: Liveness e readiness probes

**Ingress:**
- **Host**: patient-api.local
- **CORS**: Configurado para permitir requests cross-origin
- **Routes**: `/api` → backend, `/` → frontend

### Auto-scaling

O HorizontalPodAutoscaler (HPA) configura automaticamente o número de réplicas:

```bash
# Verificar status do HPA
kubectl get hpa

# Ajustar limites manualmente
kubectl edit hpa patient-api-hpa
```

### Monitoring e Debugging

```bash
# Ver pods
kubectl get pods -l app=patient-api

# Descrever pod
kubectl describe pod <pod-name>

# Executar comando no pod
kubectl exec -it <pod-name> -- /bin/sh

# Port forwarding para debug local
kubectl port-forward service/patient-api-service 8081:8081
kubectl port-forward service/patient-api-frontend-service 5173:80
```

### Cleanup

Para remover todos os recursos do Kubernetes:

```bash
./k8s/cleanup.sh
```

Ou manualmente:

```bash
kubectl delete -f k8s/
```

### Configurações Personalizadas

Edite os arquivos em `k8s/` para personalizar:

- **ConfigMap**: Alterar variáveis de ambiente
- **Secret**: Atualizar senhas
- **Deployments**: Ajustar recursos e réplicas
- **HPA**: Modificar parâmetros de auto-scaling
- **Ingress**: Alterar domínio e rotas

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

   O backend será iniciado em `http://localhost:8081` (ou outra porta se a 8081 estiver em uso)

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

- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8081/api-docs`

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
curl -X POST http://localhost:8081/api/patients \
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
curl http://localhost:8081/api/patients
```

**Atualizar um paciente:**
```bash
curl -X PUT http://localhost:8081/api/patients/1 \
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
- Acesso ao Console H2: `http://localhost:8081/h2-console`
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
- ✅ **Implementado**: Configuração CORS para desenvolvimento
- ✅ **Implementado**: Secrets Kubernetes para dados sensíveis
- ✅ **Implementado**: Não-root user em containers Docker
- ✅ **Implementado**: Health checks e liveness probes
- ✅ **Implementado**: Security headers no Nginx
- Proteção CSRF (pode ser habilitada para produção)

### Recomendações para Produção
- Implementar autenticação e autorização (JWT, OAuth2)
- Adicionar limitação de taxa (rate limiting)
- Habilitar HTTPS
- Implementar autenticação por chave de API
- Criptografar dados sensíveis em repouso
- Implementar logging de auditoria
- Adicionar headers de segurança adicionais
- Implementar network policies no Kubernetes
- Usar image scanner para vulnerabilidades
- Implementar pod security policies
- Configurar RBAC no Kubernetes
- Adicionar secrets management (Vault)

## 📈 Considerações de Escalabilidade

### Arquitetura Atual
- Design de API RESTful
- Suporte a paginação
- Indexação de banco de dados em campos frequentemente consultados
- Design de serviço sem estado
- **Docker containerization** para portabilidade
- **Kubernetes orchestration** para escalabilidade automática
- **HorizontalPodAutoscaler** para auto-scaling baseado em métricas
- **Load balancing** via Kubernetes Services
- **Health checks** para monitoring e auto-recovery
- **Rolling updates** para zero downtime deployments

### Recomendações para Escalonamento
- ✅ **Implementado**: Auto-scaling automático via HPA (2-10 réplicas)
- ✅ **Implementado**: Load balancing via Kubernetes Services
- ✅ **Implementado**: Health checks e readiness probes
- ✅ **Implementado**: Rolling updates para zero downtime
- ✅ **Implementado**: ConfigMap e Secret para gerenciamento de configurações
- ✅ **Implementado**: PersistentVolume para dados PostgreSQL
- Implementar cache (Redis)
- Adicionar réplicas de leitura do banco de dados
- Implementar API gateway
- Adicionar monitoramento e alertas (Prometheus, Grafana)
- Considerar arquitetura de microsserviços para maior escala
- Implementar CDN para ativos estáticos
- Adicionar service mesh (Istio, Linkerd)
- Implementar rate limiting no Ingress
- Configurar autoscaling do PostgreSQL

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
