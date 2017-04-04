ALTER TABLE egeis_attendance DROP CONSTRAINT pk_egeis_attendance;
ALTER TABLE egeis_attendance ADD CONSTRAINT pk_egeis_attendance primary key (id, tenantid);
ALTER TABLE egeis_attendance RENAME COLUMN date TO attendancedate;
ALTER TABLE egeis_attendance ADD CONSTRAINT uk_egeis_attendance UNIQUE (attendancedate, employee, tenantid);
ALTER TABLE egeis_attendance ALTER COLUMN month TYPE BIGINT USING month::bigint;