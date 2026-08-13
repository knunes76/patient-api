ALTER TABLE patients ADD COLUMN created_by BIGINT;
ALTER TABLE patients ADD CONSTRAINT fk_patients_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL;