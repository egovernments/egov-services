delete from egeis_leaveapplication;
ALTER TABLE egeis_leaveapplication ALTER COLUMN status TYPE BIGINT USING (status::BIGINT);