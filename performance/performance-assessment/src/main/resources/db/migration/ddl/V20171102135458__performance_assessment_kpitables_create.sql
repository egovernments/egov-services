CREATE TABLE egpa_kpi_master
(
  id character varying NOT NULL,
  name character varying NOT NULL,
  code character varying NOT NULL,
  finyear character varying NOT NULL,
  targetvalue bigint,
  instructions character varying,
  active boolean DEFAULT true, 
  tenantid character varying, 
  createdby character varying NOT NULL,
  lastmodifiedby character varying,
  createddate bigint,
  lastmodifieddate bigint,
  CONSTRAINT egpa_kpi_master_pk PRIMARY KEY (id),
  CONSTRAINT kpi_master_unq UNIQUE (code),
  CONSTRAINT kpi_master_code_unq UNIQUE (code, finyear),
  CONSTRAINT kpi_master_name_unq UNIQUE (name, finyear))
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egpa_kpi_master;

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


CREATE TABLE egpa_kpi_master_targets
(
  id character varying NOT NULL,
  kpicode character varying NOT NULL,
  targetvalue bigint NOT NULL,
  targetdescription character varying NOT NULL,
  instructions character varying NOT NULL,
  createdby character varying NOT NULL,
  lastmodifiedby character varying,
  createddate bigint,
  lastmodifieddate bigint,
  CONSTRAINT egpa_kpi_master_targets_pk PRIMARY KEY (id), 
  FOREIGN KEY (kpicode) references egpa_kpi_master (code) ON DELETE CASCADE ON UPDATE CASCADE) 
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egpa_kpi_master_targets;


CREATE TABLE egpa_kpi_value
(
  id character varying NOT NULL,
  kpicode character varying NOT NULL,
  tenantid character varying NOT NULL,
  actualvalue bigint NOT NULL,
  createdby character varying NOT NULL,
  lastmodifiedby character varying,
  createddate bigint,
  lastmodifieddate bigint,
  CONSTRAINT egpa_kpi_value_pk PRIMARY KEY (id),
  CONSTRAINT kpi_value_code_tenant_unq UNIQUE (kpicode, tenantid),
  FOREIGN KEY (kpicode) references egpa_kpi_master (code) ON DELETE CASCADE ON UPDATE CASCADE) 
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egpa_kpi_value;



