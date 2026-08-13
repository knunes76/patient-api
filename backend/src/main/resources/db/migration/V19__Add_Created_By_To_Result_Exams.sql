ALTER TABLE result_exams ADD COLUMN created_by BIGINT;
ALTER TABLE result_exams ADD CONSTRAINT fk_result_exams_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL;