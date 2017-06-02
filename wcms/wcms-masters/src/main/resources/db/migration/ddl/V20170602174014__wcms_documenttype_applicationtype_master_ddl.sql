CREATE TABLE egwtr_documenttype_applicationtype
(
  id SERIAL,
  documenttypeid bigint NOT NULL,
  applicationtype character varying(100) NOT NULL,
  mandatory boolean NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  tenantid character varying(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_document PRIMARY KEY (id, tenantid),
  CONSTRAINT fk_document_type FOREIGN KEY (documenttypeid, tenantid)
      REFERENCES public.egwtr_document_type (id, tenantid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT unq_document_application_type UNIQUE (applicationtype, documenttypeid, tenantid)
);


CREATE SEQUENCE seq_egwtr_documenttype_applicationtyp;