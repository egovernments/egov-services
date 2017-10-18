CREATE TABLE egpt_demolition(
    id bigserial NOT NULL,   
    tenantId character varying(128) NOT NULL,
    upicNumber character varying(128) NOT NULL,
    applicationno character varying(64),
    propertySubType character varying,
    usageType	  character varying ,
    usageSubType character varying,
    totalArea numeric,
    sequenceNo integer,
    isLegal BOOLEAN,	 
    demolitionReason character varying,
    comments character varying(256),
    stateId character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    CONSTRAINT pk_egpt_demolition PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egpt_demolition;
ALTER TABLE egpt_demolition ALTER COLUMN id SET DEFAULT nextval('seq_egpt_demolition');

CREATE TABLE egpt_demolition_document(
    id bigserial NOT NULL,
    filestore character varying,
    documenttype character varying,
    demolition bigint, 
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    CONSTRAINT pk_egpt_demolition_document PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_demolition_document;
ALTER TABLE egpt_demolition ALTER COLUMN id SET DEFAULT nextval('seq_egpt_demolition_document');


ALTER TABLE  egpt_demolition_document ADD CONSTRAINT fk_egpt_demolition_document FOREIGN KEY(demolition) REFERENCES egpt_demolition(id);


