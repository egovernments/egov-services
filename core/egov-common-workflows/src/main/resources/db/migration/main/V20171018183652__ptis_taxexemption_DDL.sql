CREATE TABLE egpt_taxexemption(
    id bigserial NOT NULL,   
    tenantId character varying(128) NOT NULL,
    upicNumber character varying(128) NOT NULL,
    applicationno character varying(64),
    exemptionReason character varying NOT NULL,
    exemptionPercentage numeric,
    comments character varying(256),
    stateId character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    CONSTRAINT pk_egpt_taxexemption PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egpt_taxexemption;
ALTER TABLE egpt_taxexemption ALTER COLUMN id SET DEFAULT nextval('seq_egpt_taxexemption');


CREATE TABLE egpt_taxexemption_document(
    id bigserial NOT NULL,
    filestore character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    taxexemption bigint,
    documenttype character varying,    
    CONSTRAINT pk_egpt_taxexemption_document PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_taxexemption_document;
ALTER TABLE egpt_taxexemption_document ALTER COLUMN id SET DEFAULT nextval('seq_egpt_taxexemption_document');
ALTER TABLE  egpt_taxexemption_document ADD CONSTRAINT fk_egpt_taxexemption_document FOREIGN KEY(taxexemption) REFERENCES egpt_taxexemption(id);
