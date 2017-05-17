---Pipe Size Master

CREATE TABLE egwtr_pipesize
(
  id bigint NOT NULL,
  code character varying(20) NOT NULL,
  sizeininch double precision NOT NULL,
  sizeinmilimeter double precision NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_pipesize PRIMARY KEY (id,tenantid),
  CONSTRAINT unq_pipesize_code UNIQUE (code,tenantid),
  CONSTRAINT unq_pipesize_sizeinmilimeter UNIQUE (sizeinmilimeter,tenantid)
);

CREATE SEQUENCE seq_egwtr_pipesize;
