# Documentação do Sistema de Gestão de Pacientes

## Visão Geral

Este documento descreve a arquitetura do sistema de gestão de pacientes desenvolvido com Spring Boot (backend) e React (frontend).

## Tecnologias Utilizadas

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security
- JWT (JJWT 0.11.5)
- H2 Database (desenvolvimento)
- PostgreSQL 15 (produção)
- Flyway (migrations)
- SpringDoc OpenAPI 2.3.0
- Maven

### Frontend
- React 19
- Vite 8.2.x
- Axios
- React Router DOM 7.18.2
- Tailwind CSS 4.3.3
- Lucide React (ícones)

## Arquitetura em Camadas

### 1. Camada de Domínio (Model)
Contém as entidades principais do sistema:

- **Patient**: Informações do paciente (nome, CPF, contato, histórico médico, unidade hospitalar)
- **Unity**: Unidades hospitalares (Hospital Univ. de BH, Hospital Univ. de Contorno)
- **User**: Usuários do sistema (autenticação)
- **Exam**: Tipos de exames disponíveis
- **ExamResult**: Resultados de exames associados a pacientes
- **Medication**: Medicamentos disponíveis
- **Doctor**: Médicos responsáveis por prescrições
- **CurrentMedication**: Medicamentos em uso por pacientes
- **CID10**: Classificação Internacional de Doenças
- **Specialty**: Especialidades médicas
- **ClinicalEvolution**: Evoluções clínicas de pacientes

### 2. Camada de DTO (Data Transfer Objects)
Objetos para transferência de dados entre camadas:

- **PatientRequestDTO**: Criação/edição de pacientes
- **PatientResponseDTO**: Resposta de pacientes (inclui unidade)
- **UnityRequestDTO**: Criação/edição de unidades hospitalares
- **UnityResponseDTO**: Resposta de unidades hospitalares
- **UserDTO**: Dados de usuários
- **ExamRequestDTO**: Criação/edição de exames
- **ExamResultRequestDTO**: Criação/edição de resultados
- **ExamResultResponseDTO**: Resposta de resultados (evita problemas de serialização)
- **MedicationRequestDTO**: Criação/edição de medicamentos
- **DoctorRequestDTO**: Criação/edição de médicos
- **CurrentMedicationRequestDTO**: Criação/edição de medicamentos em uso
- **CurrentMedicationResponseDTO**: Resposta de medicamentos em uso
- **CID10RequestDTO**: Criação/edição de códigos CID10
- **CID10ResponseDTO**: Resposta de códigos CID10
- **SpecialtyRequestDTO**: Criação/edição de especialidades
- **SpecialtyResponseDTO**: Resposta de especialidades
- **ClinicalEvolutionRequestDTO**: Criação/edição de evoluções clínicas
- **ClinicalEvolutionResponseDTO**: Resposta de evoluções clínicas

### 3. Camada de Repositório (Data Access)
Interfaces JPA para acesso ao banco de dados:

- **PatientRepository**: Operações CRUD de pacientes e filtro por unidade
- **UnityRepository**: Operações CRUD de unidades hospitalares
- **UserRepository**: Operações CRUD de usuários
- **ExamRepository**: Operações CRUD de exames
- **ExamResultRepository**: Operações CRUD de resultados (com filtros por paciente)
- **MedicationRepository**: Operações CRUD de medicamentos
- **DoctorRepository**: Operações CRUD de médicos
- **CurrentMedicationRepository**: Operações CRUD de medicamentos em uso (com filtros por paciente)
- **CID10Repository**: Operações CRUD de códigos CID10
- **SpecialtyRepository**: Operações CRUD de especialidades
- **ClinicalEvolutionRepository**: Operações CRUD de evoluções clínicas (com filtros por paciente)

### 4. Camada de Controller (API Layer)
REST controllers que expõem os endpoints da API:

- **PatientController**: `/api/patients/**` (inclui filtro por unidade)
- **UnityController**: `/api/unity/**`
- **AuthController**: `/api/auth/**`
- **ExamController**: `/api/exams/**`
- **ExamResultController**: `/api/exam-results/**`
- **MedicationController**: `/api/medications/**`
- **DoctorController**: `/api/doctors/**`
- **CurrentMedicationController**: `/api/current-medication/**`
- **CID10Controller**: `/api/cid10/**`
- **SpecialtyController**: `/api/specialty/**`
- **ClinicalEvolutionController**: `/api/clinical-evolution/**`

### 5. Camada de Serviço (Business Logic)
Lógica de negócio do sistema:

- **AuthService**: Autenticação e geração de tokens JWT
- **JwtUtil**: Utilitários para manipulação de tokens JWT

### 6. Camada de Segurança (Authentication)
Configuração de segurança e autenticação:

- **SecurityConfig**: Configuração do Spring Security
- **JwtAuthenticationFilter**: Filtro para validação de tokens JWT
- **CustomUserDetailsService**: Carregamento de detalhes do usuário

## Relacionamentos entre Entidades

### Patient
- N:1 com Unity (pertence a uma unidade hospitalar)
- 1:N com ExamResult (um paciente pode ter vários resultados de exames)
- 1:N com CurrentMedication (um paciente pode ter vários medicamentos em uso)
- 1:N com ClinicalEvolution (um paciente pode ter várias evoluções clínicas)

### Unity
- 1:N com Patient (uma unidade pode ter vários pacientes)

### Exam
- 1:N com ExamResult (um tipo de exame pode ter vários resultados)

### ExamResult
- N:1 com Patient (pertence a um paciente)
- N:1 com Exam (pertence a um tipo de exame)

### Medication
- 1:N com CurrentMedication (um medicamento pode estar em uso por vários pacientes)

### Doctor
- 1:N com CurrentMedication (um médico pode prescrever vários medicamentos)
- 1:N com ClinicalEvolution (um médico pode registrar várias evoluções)

### CurrentMedication
- N:1 com Patient (pertence a um paciente)
- N:1 com Medication (é um tipo de medicamento)
- N:1 com Doctor (opcional - prescrito por um médico)

### CID10
- 1:N com ClinicalEvolution (um código pode ser usado em várias evoluções)

### Specialty
- 1:N com ClinicalEvolution (uma especialidade pode estar relacionada a várias evoluções)

### User
- 1:N com ClinicalEvolution (um usuário pode criar várias evoluções)

### Relacionamentos entre Entidades (DER)

#### Tabela Principal: patients
- **1:N** com result_exams (um paciente pode ter vários resultados de exames)
- **1:N** com current_medication (um paciente pode ter vários medicamentos em uso)

#### Tabela Principal: exams
- **1:N** com result_exams (um tipo de exame pode ter vários resultados)

#### Tabela Principal: medications
- **1:N** com current_medication (um medicamento pode estar em uso por vários pacientes)

#### Tabela Principal: doctors
- **1:N** com current_medication (um médico pode prescrever vários medicamentos)

#### Tabela Principal: users
- **1:N** com patients (relação conceitual de gerenciamento)

#### Tabelas Relacionais:
- **result_exams**: Tabela que conecta patients com exams (relacionamento N:1 patients, N:1 exams)
- **current_medication**: Tabela que conecta patients com medications e doctors (relacionamento N:1 patients, N:1 medications, N:1 doctors opcional)

#### Regras de Integridade:
- **ON DELETE CASCADE**: Se um paciente for excluído, seus resultados de exames e medicamentos em uso são excluídos automaticamente
- **ON DELETE CASCADE**: Se um exame for excluído, seus resultados são excluídos automaticamente
- **ON DELETE CASCADE**: Se um medicamento for excluído, os registros de medicamentos em uso são excluídos automaticamente
- **ON DELETE SET NULL**: Se um médico for excluído, os registros de medicamentos em uso têm o campo doctor_id definido como NULL

## Database Migrations

O sistema usa Flyway para versionamento do banco de dados:

- **V1**: Create Patients Table
- **V2**: Insert Sample Data (pacientes)
- **V3**: Create Users Table
- **V4**: Insert Admin User
- **V5**: Create Exams Table
- **V6**: Create Result Exams Table
- **V7**: Insert Sample Exams (10 exames)
- **V8**: Create Medications Table
- **V9**: Create Doctors Table
- **V10**: Create Current Medication Table
- **V11**: Insert Sample Medications (10 medicamentos)
- **V12**: Insert Sample Doctors (5 médicos)
- **V13**: Create CID10 Table
- **V14**: Create Specialty Table
- **V15**: Create Clinical Evolution Table
- **V16**: Insert Sample CID10
- **V17**: Insert Sample Specialty
- **V18**: Add Created By To Patients
- **V19**: Add Created By To Result Exams
- **V20**: Add Created By To Current Medication
- **V21**: Create Unity Table
- **V22**: Insert Sample Unity (2 unidades hospitalares)
- **V23**: Add Unity To Patients
- **V24**: Update Patients Unity Distribution

## API Endpoints

### Autenticação
- `POST /api/auth/login` - Login e geração de token JWT

### Unidades Hospitalares
- `GET /api/unity` - Listar todas as unidades
- `GET /api/unity/{id}` - Buscar unidade por ID
- `POST /api/unity` - Criar nova unidade
- `PUT /api/unity/{id}` - Atualizar unidade
- `DELETE /api/unity/{id}` - Excluir unidade

### Pacientes
- `GET /api/patients` - Listar todos os pacientes
- `GET /api/patients/{id}` - Buscar paciente por ID
- `GET /api/patients/unity/{unityId}` - Listar pacientes por unidade
- `POST /api/patients` - Criar novo paciente
- `PUT /api/patients/{id}` - Atualizar paciente
- `DELETE /api/patients/{id}` - Excluir paciente

### Exames
- `GET /api/exams` - Listar todos os tipos de exames
- `GET /api/exams/{id}` - Buscar exame por ID
- `POST /api/exams` - Criar novo tipo de exame
- `PUT /api/exams/{id}` - Atualizar exame
- `DELETE /api/exams/{id}` - Excluir exame

### Resultados de Exames
- `GET /api/exam-results/patient/{patientId}` - Listar resultados por paciente
- `GET /api/exam-results/{id}` - Buscar resultado por ID
- `POST /api/exam-results` - Criar novo resultado
- `PUT /api/exam-results/{id}` - Atualizar resultado
- `DELETE /api/exam-results/{id}` - Excluir resultado

### Medicamentos
- `GET /api/medications` - Listar todos os medicamentos
- `GET /api/medications/{id}` - Buscar medicamento por ID
- `POST /api/medications` - Criar novo medicamento
- `PUT /api/medications/{id}` - Atualizar medicamento
- `DELETE /api/medications/{id}` - Excluir medicamento

### Médicos
- `GET /api/doctors` - Listar todos os médicos
- `GET /api/doctors/{id}` - Buscar médico por ID
- `POST /api/doctors` - Criar novo médico
- `PUT /api/doctors/{id}` - Atualizar médico
- `DELETE /api/doctors/{id}` - Excluir médico

### Medicamentos em Uso
- `GET /api/current-medication/patient/{patientId}` - Listar medicamentos por paciente
- `GET /api/current-medication/{id}` - Buscar medicamento em uso por ID
- `POST /api/current-medication` - Criar novo medicamento em uso
- `PUT /api/current-medication/{id}` - Atualizar medicamento em uso
- `DELETE /api/current-medication/{id}` - Excluir medicamento em uso

### CID10
- `GET /api/cid10` - Listar todos os códigos CID10
- `GET /api/cid10/{id}` - Buscar código CID10 por ID
- `POST /api/cid10` - Criar novo código CID10
- `PUT /api/cid10/{id}` - Atualizar código CID10
- `DELETE /api/cid10/{id}` - Excluir código CID10

### Especialidades
- `GET /api/specialty` - Listar todas as especialidades
- `GET /api/specialty/{id}` - Buscar especialidade por ID
- `POST /api/specialty` - Criar nova especialidade
- `PUT /api/specialty/{id}` - Atualizar especialidade
- `DELETE /api/specialty/{id}` - Excluir especialidade

### Evoluções Clínicas
- `GET /api/clinical-evolution/patient/{patientId}` - Listar evoluções por paciente
- `GET /api/clinical-evolution/{id}` - Buscar evolução por ID
- `POST /api/clinical-evolution` - Criar nova evolução
- `PUT /api/clinical-evolution/{id}` - Atualizar evolução
- `DELETE /api/clinical-evolution/{id}` - Excluir evolução

## Frontend Components

### Componentes UI Reutilizáveis
- **Button**: Botões com diferentes variantes
- **Card**: Cards com header, content e footer
- **Input**: Inputs com validação
- **Label**: Labels para formulários
- **Table**: Tabelas com header e body
- **Badge**: Badges com diferentes variantes
- **Box**: Container flexível

### Componentes de Navegação
- **Layout**: Layout principal com sidebar
- **Login**: Tela de login
- **PatientList**: Lista de pacientes com ações

### Componentes de Funcionalidades
- **Exams**: Gestão de exames por paciente
- **Medications**: Gestão de medicamentos por paciente
- **ClinicalEvolution**: Gestão de evoluções clínicas por paciente
- **Tests**: Tela de testes
- **UnitySelector**: Seletor de unidade hospitalar no sidebar

## Diagramas

### Diagrama de Classes
Mostra a estrutura das classes Java, pacotes e relacionamentos entre componentes do sistema, incluindo:
- Camadas de arquitetura (Model, DTO, Repository, Controller, Service, Security)
- Relacionamentos entre classes
- Herança e composição
- Métodos e atributos principais

### Diagrama de Entidade e Relacionamento (DER)
Mostra a estrutura do banco de dados e relacionamentos entre tabelas, incluindo:
- Tabelas principais (patients, users, exams, result_exams, medications, doctors, current_medication, unity, cid10, specialty, clinical_evolution)
- Chaves primárias (PK) e estrangeiras (FK)
- Relacionamentos 1:N e N:1
- Restrições de NOT NULL e UNIQUE
- Campos e tipos de dados

## Sistema de Unidades Hospitalares

### Funcionalidades
- **Cadastro de Unidades**: Cadastramento de unidades hospitalares (Hospital Univ. de BH, Hospital Univ. de Contorno)
- **Filtro por Unidade**: Listagem de pacientes filtrada por unidade hospitalar
- **Seletor no Sidebar**: Componente no sidebar para seleção de unidade ativa
- **Unidade Obrigatória**: Todo paciente deve pertencer a uma unidade hospitalar
- **Distribuição de Dados**: Pacientes de teste distribuídos igualmente entre as unidades

### Implementação
- **Backend**: Entidade Unity, migrations V21-V24, UnityRepository, UnityController
- **Frontend**: UnityContext, unityApi.js, seletor no Layout.jsx, filtro automático no PatientList.jsx
- **Testes**: Testes unitários para UnityController e integração com PatientService

### Unidades Disponíveis
1. **Hospital Univ. de BH** (ID: 1)
2. **Hospital Univ. de Contorno** (ID: 2)

### Comportamento
- Unidade 1 é selecionada por padrão ao carregar a aplicação
- Mudança de unidade no sidebar atualiza automaticamente a lista de pacientes
- Formulário de paciente requer seleção de unidade obrigatória
- Lista de pacientes mostra apenas pacientes da unidade selecionada

## Como Visualizar os Diagramas

### Diagrama de Classes

![Diagrama de Classes](diagrama-classes.svg)

### Diagrama de Entidade e Relacionamento (DER)

![Diagrama de Entidade e Relacionamento](diagrama-entidade-relacionamento.svg)

### Opção 1: Online (PlantUML)
1. Acesse https://plantuml.com/online
2. Cole o conteúdo do arquivo `diagrama-classes.puml` ou `diagrama-entidade-relacionamento.puml`
3. Clique em "Generate"

### Opção 2: Visual Studio Code
1. Instale a extensão "PlantUML"
2. Abra o arquivo `diagrama-classes.puml` ou `diagrama-entidade-relacionamento.puml`
3. Pressione `Alt+D` para gerar o diagrama

### Opção 3: IntelliJ IDEA
1. Instale o plugin "PlantUML integration"
2. Abra o arquivo `diagrama-classes.puml` ou `diagrama-entidade-relacionamento.puml`
3. Clique com botão direito e selecione "Show Diagram"

## Estrutura de Diretórios

```
ebserh/
├── backend/
│   ├── src/main/java/com/ebserh/patientapi/
│   │   ├── config/          # Configurações
│   │   ├── controller/      # REST Controllers
│   │   ├── model/          # Entidades e DTOs
│   │   ├── repository/     # JPA Repositories
│   │   ├── security/       # Configuração de segurança
│   │   ├── service/        # Serviços de negócio
│   │   └── exception/      # Tratamento de exceções
│   └── src/main/resources/
│       ├── db/migration/   # Migrations Flyway
│       └── application.properties
├── frontend/
│   ├── src/
│   │   ├── api/           # Integração com API
│   │   ├── components/    # Componentes React
│   │   ├── context/       # Contextos (Auth)
│   │   └── lib/           # Utilitários
│   └── public/
├── docs/                  # Documentação
├── k8s/                   # Manifests Kubernetes
└── docker-compose.yml     # Docker Compose
```

## Próximos Passos

1. ✅ Adicionar testes unitários para controllers
2. Implementar validações mais robustas
3. Adicionar tratamento de exceções específico
4. Implementar caching para consultas frequentes
5. Adicionar auditoria de operações
6. Implementar backup e restore do banco de dados

## Testes Unitários

### Cobertura de Testes
O sistema possui 58 testes unitários distribuídos entre controllers e services:

#### Controllers (44 testes)
- **PatientControllerTest**: 8 testes - CRUD completo, busca por CPF e nome, validação de campos
- **ExamResultControllerTest**: 10 testes - CRUD completo, tratamento de erros, filtros por paciente
- **CurrentMedicationControllerTest**: 12 testes - CRUD completo, campos opcionais, tratamento de erros
- **ClinicalEvolutionControllerTest**: 14 testes - CRUD completo, múltiplas dependências, tratamento de erros

#### Services (14 testes)
- **PatientServiceTest**: 14 testes - Lógica de negócio, validações, integração com UnityRepository

### Configuração dos Testes
- **Spring Security Test**: Dependência adicionada para facilitar testes de controllers
- **MockMvc**: Usado para testar endpoints REST sem inicializar contexto completo
- **@AutoConfigureMockMvc(addFilters = false)**: Desabilita filtros de segurança para evitar problemas de autenticação
- **MockBean**: Repositories e componentes mockados para isolar testes
- **@WebMvcTest**: Testes de camada de controller focados em endpoints específicos

### Execução dos Testes
```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=PatientControllerTest
mvn test -Dtest=ExamResultControllerTest
mvn test -Dtest=CurrentMedicationControllerTest
mvn test -Dtest=ClinicalEvolutionControllerTest
mvn test -Dtest=PatientServiceTest
```

### Resultado Atual
- **Total de testes**: 58
- **Testes passando**: 58 (100%)
- **Testes falhando**: 0