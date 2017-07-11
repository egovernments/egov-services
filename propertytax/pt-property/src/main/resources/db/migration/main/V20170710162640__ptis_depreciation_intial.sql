CREATE TABLE egpt_depreciation(
id bigint NOT NULL,
tenantId character varying NOT NULL,
code character varying NOT NULL,
data jsonb,
createdby character varying,
lastmodifiedby character varying,
createdtime bigint,
lastmodifiedtime bigint,
CONSTRAINT pk_egpt_depreciation PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egpt_depreciation;

ALTER TABLE egpt_depreciation ALTER COLUMN id SET DEFAULT nextval('seq_egpt_depreciation');
