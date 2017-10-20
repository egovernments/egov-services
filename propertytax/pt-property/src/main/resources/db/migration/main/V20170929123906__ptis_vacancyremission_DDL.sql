CREATE TABLE egpt_vacancyremission(
    id bigserial NOT NULL,
    upicNo character varying NOT NULL,
    tenantId character varying(128) NOT NULL,
    applicationNo character varying,
    fromDate timestamp without time zone NOT NULL,
    toDate timestamp without time zone NOT NULL,
    percentage numeric NOT NULL,
    reason character varying NOT NULL,
    requestDate timestamp without time zone,
    approvedDate timestamp without time zone,
    isApproved boolean default false,
    remarks character varying,
    stateid character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    CONSTRAINT pk_egpt_vacancyremission PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egpt_vacancyremission;
ALTER TABLE egpt_vacancyremission ALTER COLUMN id SET DEFAULT nextval('seq_egpt_vacancyremission');


CREATE TABLE egpt_vacancyremission_document(
    id bigserial NOT NULL,
    filestore character varying,
    createdby character varying,
    lastmodifiedby character varying,
    createdtime bigint,
    lastmodifiedtime bigint,
    documenttype character varying,
    vacancyremission bigint,
    CONSTRAINT pk_egpt_vacancyremission_document PRIMARY KEY(id)
);

CREATE SEQUENCE seq_egpt_vacancyremission_document;
ALTER TABLE egpt_vacancyremission_document ALTER COLUMN id SET DEFAULT nextval('seq_egpt_vacancyremission_document');
ALTER TABLE  egpt_vacancyremission_document ADD CONSTRAINT fk_egpt_vacancyremission_document FOREIGN KEY(vacancyremission) REFERENCES egpt_vacancyremission(id);
