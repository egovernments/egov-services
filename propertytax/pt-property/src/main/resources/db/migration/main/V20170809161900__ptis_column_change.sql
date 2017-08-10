ALTER TABLE egpt_mstr_usage ALTER COLUMN parent DROP NOT NULL;
ALTER TABLE egpt_mstr_usage ALTER parent type character varying(64);