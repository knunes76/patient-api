-- Distribuir pacientes entre as duas unidades (metade para cada)
UPDATE patients SET unity_id = 1 WHERE id % 2 = 1;
UPDATE patients SET unity_id = 2 WHERE id % 2 = 0;