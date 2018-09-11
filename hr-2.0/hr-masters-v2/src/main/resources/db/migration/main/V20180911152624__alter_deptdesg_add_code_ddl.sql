alter table egeis_departmentDesignation add column departmentCode varchar(250) NOT NULL DEFAULT 'dept';
alter table egeis_departmentDesignation add column designationCode varchar(250) NOT NULL DEFAULT 'desg';

ALTER TABLE egeis_departmentDesignation ALTER COLUMN departmentId DROP NOT NULL;
ALTER TABLE egeis_departmentDesignation ALTER COLUMN designationId DROP NOT NULL;

