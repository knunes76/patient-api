-- Create patients table
CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
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
CREATE INDEX idx_patients_name ON patients(name);
CREATE INDEX idx_patients_cpf ON patients(cpf);
CREATE INDEX idx_patients_email ON patients(email);
CREATE INDEX idx_patients_birth_date ON patients(birth_date);
CREATE INDEX idx_patients_city ON patients(city);
CREATE INDEX idx_patients_state ON patients(state);

-- Add comments for documentation
COMMENT ON TABLE patients IS 'Table for storing patient information';
COMMENT ON COLUMN patients.id IS 'Unique identifier for the patient';
COMMENT ON COLUMN patients.name IS 'Full name of the patient';
COMMENT ON COLUMN patients.cpf IS 'CPF (Brazilian tax ID) - 11 digits, unique';
COMMENT ON COLUMN patients.email IS 'Email address - unique';
COMMENT ON COLUMN patients.phone IS 'Phone number - 10 or 11 digits';
COMMENT ON COLUMN patients.birth_date IS 'Date of birth';
COMMENT ON COLUMN patients.gender IS 'Gender (Male, Female, Other)';
COMMENT ON COLUMN patients.address IS 'Street address';
COMMENT ON COLUMN patients.city IS 'City name';
COMMENT ON COLUMN patients.state IS 'State abbreviation (2 characters)';
COMMENT ON COLUMN patients.zip_code IS 'Postal/ZIP code';
COMMENT ON COLUMN patients.blood_type IS 'Blood type (A+, A-, B+, B-, AB+, AB-, O+, O-)';
COMMENT ON COLUMN patients.allergies IS 'Known allergies';
COMMENT ON COLUMN patients.medical_history IS 'Medical history notes';
COMMENT ON COLUMN patients.emergency_contact IS 'Emergency contact name';
COMMENT ON COLUMN patients.emergency_phone IS 'Emergency contact phone';
COMMENT ON COLUMN patients.created_at IS 'Record creation timestamp';
COMMENT ON COLUMN patients.updated_at IS 'Record last update timestamp';
