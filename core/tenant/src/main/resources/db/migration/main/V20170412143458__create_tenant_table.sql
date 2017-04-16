CREATE SEQUENCE seq_tenant;

CREATE TABLE tenant (
  id bigint NOT NULL,
  code varchar(256) NOT NULL,
  description varchar(300),
  domainurl varchar(128),
  logoid varchar(36) NOT NULL,
  imageid varchar(36) NOT NULL,
  createdby bigint DEFAULT 1,
  createddate timestamp DEFAULT now(),
  lastmodifiedby bigint DEFAULT 1,
  lastmodifieddate timestamp DEFAULT now(),
  CONSTRAINT tenant_pkey PRIMARY KEY (id),
  CONSTRAINT tenant_code_ukey UNIQUE (code)
);

