ALTER TABLE egeis_attendance RENAME COLUMN date TO attendancedate;
ALTER TABLE egeis_attendance ADD CONSTRAINT uk_egeis_attendance UNIQUE (attendancedate, employee, tenantid);
ALTER TABLE egeis_attendance ALTER COLUMN month TYPE BIGINT USING month::bigint;
