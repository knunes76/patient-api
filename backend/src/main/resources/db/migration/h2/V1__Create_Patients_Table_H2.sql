-- Create patients table for H2 database
CREATE TABLE IF NOT EXISTS patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(11) NOT NULL,
    birth_date DATE NOT NULL,
    gender VARCHAR(20),
    address VARCHAR(200),
    city VARCHAR(100),
    state VARCHAR(2),
    zip_code VARCHAR(10),
    blood_type VARCHAR(20),
    allergies VARCHAR(500),
    medical_history VARCHAR(500),
    emergency_contact VARCHAR(20),
    emergency_phone VARCHAR(11),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_patients_name ON patients(name);
CREATE INDEX IF NOT EXISTS idx_patients_cpf ON patients(cpf);
CREATE INDEX IF NOT EXISTS idx_patients_email ON patients(email);
CREATE INDEX IF NOT EXISTS idx_patients_birth_date ON patients(birth_date);
CREATE INDEX IF NOT EXISTS idx_patients_city ON patients(city);
CREATE INDEX IF NOT EXISTS idx_patients_state ON patients(state);

-- Insert sample patient data for testing
INSERT INTO patients (name, cpf, email, phone, birth_date, gender, address, city, state, zip_code, blood_type, allergies, medical_history, emergency_contact, emergency_phone, created_at, updated_at) VALUES
('João Silva', '12345678901', 'joao.silva@example.com', '11987654321', '1985-03-15', 'Male', 'Rua das Flores, 123', 'São Paulo', 'SP', '01234-567', 'O+', 'Penicillin', 'Diabetes Type 2', 'Maria Silva', '11912345678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Maria Santos', '98765432109', 'maria.santos@example.com', '21987654321', '1990-07-22', 'Female', 'Av. Brasil, 456', 'Rio de Janeiro', 'RJ', '20040-002', 'A+', 'None', 'Hypertension', 'José Santos', '21912345678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pedro Oliveira', '45678912301', 'pedro.oliveira@example.com', '31987654321', '1978-11-08', 'Male', 'Rua Central, 789', 'Belo Horizonte', 'MG', '30130-010', 'B-', 'Aspirin', 'None', 'Ana Oliveira', '31912345678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ana Costa', '78912345601', 'ana.costa@example.com', '41987654321', '1995-05-30', 'Female', 'Rua do Sol, 321', 'Salvador', 'BA', '40060-010', 'AB+', 'Dust, Pollen', 'Asthma', 'Carlos Costa', '41912345678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Carlos Ferreira', '32165498701', 'carlos.ferreira@example.com', '51987654321', '1982-09-12', 'Male', 'Av. Paulista, 1000', 'São Paulo', 'SP', '01310-100', 'O-', 'None', 'None', 'Fernanda Ferreira', '51912345678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
