DROP TABLE egpa_kpi_value_documents;

DROP SEQUENCE seq_egpa_kpi_value_documents; 

DROP TABLE egpa_kpi_value; 

DROP SEQUENCE seq_egpa_kpi_value;

DROP TABLE egpa_kpi_master_targets;

DROP SEQUENCE seq_egpa_kpi_master_targets; 

DROP TABLE egpa_kpi_master_document; 

DROP SEQUENCE seq_egpa_kpi_master_document; 

DROP TABLE egpa_kpi_master; 

DROP SEQUENCE seq_egpa_kpi_master; 


CREATE TABLE egpa_kpi_master
(
  id character varying NOT NULL,
  name character varying NOT NULL,
  code character varying NOT NULL,
  department bigint,
  finyear character varying NOT NULL,
  instructions character varying,
  periodicity character varying,
  targettype character varying,
  active boolean DEFAULT true,
  createdby character varying NOT NULL,
  lastmodifiedby character varying,
  createddate bigint,
  lastmodifieddate bigint,
  CONSTRAINT egpa_kpi_master_pk PRIMARY KEY (id),
  CONSTRAINT kpi_master_code_unq UNIQUE (code, finyear),
  CONSTRAINT kpi_master_name_unq UNIQUE (name, finyear),
  CONSTRAINT kpi_master_unq UNIQUE (code)
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egpa_kpi_master;

CREATE TABLE egpa_kpi_master_target
(
  id character varying NOT NULL,
  kpicode character varying NOT NULL,
  targetvalue character varying,
  tenantid character varying, 
  createdby character varying NOT NULL,
  lastmodifiedby character varying,
  createddate bigint,
  lastmodifieddate bigint,
  CONSTRAINT egpa_kpi_master_target_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egpa_kpi_master_target;


CREATE TABLE egpa_kpi_value
(
  id character varying NOT NULL,
  kpicode character varying NOT NULL,
  tenantid character varying NOT NULL,
  createdby character varying NOT NULL,
  lastmodifiedby character varying,
  createddate bigint,
  lastmodifieddate bigint,
  CONSTRAINT egpa_kpi_value_pk PRIMARY KEY (id),
  CONSTRAINT egpa_kpi_value_kpicode_fkey FOREIGN KEY (kpicode)
      REFERENCES public.egpa_kpi_master (code) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT kpi_value_code_tenant_unq UNIQUE (kpicode, tenantid)
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egpa_kpi_value;

CREATE TABLE egpa_kpi_value_detail
(
  id character varying NOT NULL,
  valueid character varying NOT NULL,
  period character varying ,
  value character varying ,
  createdby character varying NOT NULL,
  lastmodifiedby character varying,
  createddate bigint,
  lastmodifieddate bigint,
  CONSTRAINT egpa_kpi_value_detail_pk PRIMARY KEY (id),
  CONSTRAINT egpa_kpi_value_fkey FOREIGN KEY (valueid)
      REFERENCES public.egpa_kpi_value (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT kpi_value_attrib_unq UNIQUE (valueid, period)
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egpa_kpi_value_detail;

CREATE TABLE egpa_kpi_master_document
(
  id character varying NOT NULL,
  kpicode character varying NOT NULL,
  documentcode character varying NOT NULL,
  documentname character varying NOT NULL,
  mandatoryflag boolean, 
  createdby character varying NOT NULL,
  lastmodifiedby character varying,
  createddate bigint,
  lastmodifieddate bigint,
  CONSTRAINT egpa_kpi_master_document_pk PRIMARY KEY (id), 
  FOREIGN KEY (kpicode) references egpa_kpi_master (code) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT kpi_master_document_code_unq UNIQUE (kpicode, documentcode),
  CONSTRAINT kpi_master_document_name_unq UNIQUE (kpicode, documentname)) 
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egpa_kpi_master_document;


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




