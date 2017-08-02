CREATE TABLE egmr_documents
(
  id character varying NOT NULL,
  reissuecertificateid character varying NOT NULL,
  documenttypecode character varying(64) NOT NULL,
  location character varying NOT NULL,
  createdby character varying(250) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(250),
  lastmodifiedtime bigint,
  tenantid character varying(250) NOT NULL,
  CONSTRAINT pk_egmr_documents PRIMARY KEY (id, tenantid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.egmr_documents
  OWNER TO postgres;
