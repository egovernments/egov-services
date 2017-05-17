ALTER TABLE egpgr_complaintstatus RENAME TO service_status;
ALTER SEQUENCE seq_egpgr_complaintstatus RENAME TO seq_service_status;
ALTER SEQUENCE seq_service_status RESTART WITH 10;

ALTER TABLE service_status ADD COLUMN tenantid character varying(256) NOT NULL DEFAULT 'default';
ALTER TABLE service_status ALTER COLUMN tenantid DROP DEFAULT;

ALTER TABLE service_status ADD COLUMN code character varying(20) NOT NULL DEFAULT '0001';
UPDATE service_status SET code = name;
ALTER TABLE service_status ALTER COLUMN code DROP DEFAULT;

ALTER TABLE service_status ADD CONSTRAINT unique_servicestatus_code_tenant UNIQUE (code, tenantid);
