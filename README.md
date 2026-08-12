# Patient Management API

A complete CRUD application for managing patient records, built with Java/Spring Boot backend and React frontend.

## 🚀 Project Overview

This project implements a comprehensive Patient Management System with full CRUD operations, RESTful API, modern React frontend, and database integration. It includes comprehensive unit tests, API documentation with Swagger/OpenAPI, and database migration scripts.

## 🛠 Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA** - Database abstraction
- **Spring Validation** - Input validation
- **H2 Database** - Development/testing (in-memory)
- **PostgreSQL** - Production database
- **Flyway** - Database migrations
- **SpringDoc OpenAPI** - API documentation
- **JUnit 5 & Mockito** - Unit testing
- **Maven** - Build tool

### Frontend
- **React 19** - UI framework
- **Vite** - Build tool and dev server
- **Axios** - HTTP client
- **React Router DOM** - Client-side routing
- **CSS3** - Styling

## 📋 Prerequisites

Before running this application, ensure you have installed:

- **Java 17** or higher
- **Maven 3.6+** 
- **Node.js 18+** and **npm**
- **PostgreSQL 14+** (for production environment)
- **Git** (for version control)

## 🏗 Project Structure

```
ebserh/
├── backend/                 # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/ebserh/patientapi/
│   │   │   │       ├── controller/      # REST controllers
│   │   │   │       ├── service/         # Business logic
│   │   │   │       ├── repository/      # Data access layer
│   │   │   │       ├── model/           # Entity models and DTOs
│   │   │   │       ├── config/          # Configuration classes
│   │   │   │       └── exception/       # Exception handling
│   │   │   └── resources/
│   │   │       ├── db/migration/        # Database migrations
│   │   │       └── application.properties
│   │   └── test/                        # Unit tests
│   └── pom.xml
├── frontend/                # React application
│   ├── src/
│   │   ├── components/      # React components
│   │   ├── api/            # API client
│   │   └── App.jsx
│   ├── package.json
│   └── vite.config.js
└── README.md
```

## 🔧 Setup Instructions

### Backend Setup

1. **Navigate to the backend directory:**
   ```bash
   cd backend
   ```

2. **Install dependencies and build the project:**
   ```bash
   mvn clean install
   ```

3. **Configure the database:**
   
   For **development** (H2 in-memory database):
   - No additional setup required
   - Database is created automatically on startup
   
   For **production** (PostgreSQL):
   - Create a PostgreSQL database:
     ```sql
     CREATE DATABASE patientdb;
     ```
   - Update `src/main/resources/application-prod.properties` with your database credentials
   - Ensure PostgreSQL is running and accessible

4. **Run the application:**
   ```bash
   # Development mode (H2 database)
   mvn spring-boot:run
   
   # Production mode (PostgreSQL)
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

   The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to the frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:5173`

## 📚 API Documentation

Once the backend is running, access the interactive API documentation:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/patients` | Create a new patient |
| GET | `/api/patients/{id}` | Get patient by ID |
| GET | `/api/patients/cpf/{cpf}` | Get patient by CPF |
| GET | `/api/patients` | Get all patients (paginated) |
| GET | `/api/patients/search?name={name}` | Search patients by name |
| PUT | `/api/patients/{id}` | Update patient |
| DELETE | `/api/patients/{id}` | Delete patient |

### Example API Usage

**Create a patient:**
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "cpf": "12345678901",
    "email": "john.doe@example.com",
    "phone": "11987654321",
    "birthDate": "1990-01-01",
    "gender": "Male",
    "bloodType": "O+"
  }'
```

**Get all patients:**
```bash
curl http://localhost:8080/api/patients?page=0&size=10&sort=name&direction=asc
```

**Update a patient:**
```bash
curl -X PUT http://localhost:8080/api/patients/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Updated",
    "cpf": "12345678901",
    "email": "john.updated@example.com",
    "phone": "11987654321",
    "birthDate": "1990-01-01"
  }'
```

## 🧪 Testing

### Backend Tests

Run the unit tests:
```bash
cd backend
mvn test
```

The test suite includes:
- Service layer tests with Mockito
- Controller tests with MockMvc
- Exception handling tests
- Validation tests

### Frontend Tests

The frontend can be tested manually through the UI or by adding automated tests. Currently, the application is designed for manual testing through the React interface.

## 🗄️ Database

### Development (H2)
- In-memory database for development and testing
- Automatically created and populated with sample data
- Access H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:patientdb`
  - Username: `sa`
  - Password: (empty)

### Production (PostgreSQL)
- Persistent database for production use
- Flyway migrations handle schema management
- Sample data can be loaded via migration scripts

### Database Schema

The `patients` table includes:
- Basic information: name, CPF, email, phone, birth date, gender
- Address: street, city, state, ZIP code
- Medical: blood type, allergies, medical history
- Emergency: contact name and phone
- Timestamps: created_at, updated_at

## 🔐 Security Considerations

### Current Implementation
- Input validation on all endpoints
- SQL injection prevention via JPA/Hibernate
- XSS protection in React frontend
- CSRF protection (can be enabled for production)

### Recommendations for Production
- Implement authentication and authorization (JWT, OAuth2)
- Add rate limiting
- Enable HTTPS
- Implement CORS configuration
- Add API key authentication
- Encrypt sensitive data at rest
- Implement audit logging
- Add security headers

## 📈 Scalability Considerations

### Current Architecture
- RESTful API design
- Pagination support
- Database indexing on frequently queried fields
- Stateless service design

### Recommendations for Scaling
- Implement caching (Redis)
- Add load balancing
- Implement database read replicas
- Use connection pooling
- Implement API gateway
- Add monitoring and alerting
- Consider microservices architecture for larger scale
- Implement CDN for static assets

## 🔧 Maintenance

### Code Quality
- Consistent code formatting
- Comprehensive unit tests
- Documentation via Swagger/OpenAPI
- Clear separation of concerns

### Recommendations
- Implement CI/CD pipeline
- Add integration tests
- Implement logging strategy
- Add performance monitoring
- Regular dependency updates
- Code review process
- Documentation updates

## 🐛 Troubleshooting

### Backend Issues

**Port already in use:**
```bash
# Change port in application.properties
server.port=8081
```

**Database connection issues:**
- Verify PostgreSQL is running
- Check connection string in application-prod.properties
- Ensure database exists

**Build failures:**
```bash
mvn clean install -U
```

### Frontend Issues

**Dependencies issues:**
```bash
rm -rf node_modules package-lock.json
npm install
```

**Port conflicts:**
```bash
npm run dev -- --port 3000
```

**API connection issues:**
- Verify backend is running
- Check proxy configuration in vite.config.js
- Ensure CORS is properly configured

## 📝 License

This project is created for technical assessment purposes.

## 👥 Authors

- Developed as a technical assessment for EBSERH

## 🤝 Contributing

This is a technical assessment project. For production use, consider implementing additional features like:
- User authentication and authorization
- Advanced search and filtering
- File upload for medical documents
- Appointment scheduling
- Integration with external medical systems
- Mobile application support
