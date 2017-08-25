ALTER TABLE egtl_support_document
ALTER COLUMN comments DROP NOT NULL;

ALTER TABLE egtl_license
ALTER COLUMN adhaarNumber TYPE character varying(20),
ALTER COLUMN propertyAssesmentNo TYPE character varying(20);