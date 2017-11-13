CREATE TABLE egpa_kpi_value_documents
(
  id character varying NOT NULL,
  documentcode character varying NOT NULL,
  kpicode character varying NOT NULL,
  valueid bigint NOT NULL,
  filestoreid character varying, 
  createdby character varying NOT NULL,
  lastmodifiedby character varying,
  createddate bigint,
  lastmodifieddate bigint,
  CONSTRAINT egpa_kpi_value_doc_pk PRIMARY KEY (id))
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egpa_kpi_value_documents;
