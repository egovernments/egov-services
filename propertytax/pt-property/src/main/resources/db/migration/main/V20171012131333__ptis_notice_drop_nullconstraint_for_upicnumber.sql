ALTER TABLE egpt_notice ALTER COLUMN upicnumber DROP NOT NULL;

Alter TABLE egpt_notice DROP CONSTRAINT uk_egpt_notice;

Alter TABLE egpt_notice ADD CONSTRAINT uk_egpt_notice UNIQUE (tenantid, noticetype);
