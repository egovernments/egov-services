CREATE SEQUENCE seq_tenant;

CREATE TABLE tenant
(
  id bigint NOT NULL,
  code character varying(256) NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(300),
  domainurl character varying(128),
  localname character varying(256),
  active boolean,
  version bigint,
  latitude double precision,
  longitude double precision,
  logoid character varying(36) NOT NULL,
  imageid character varying(36) NOT NULL,
  createdby bigint DEFAULT 1,
  createddate timestamp DEFAULT now(),
  lastmodifiedby bigint DEFAULT 1,
  lastmodifieddate timestamp DEFAULT now(),
  grade character varying(50),
  regionname character varying(50),
  districtcode character varying(50),
  districtname character varying(50),
  CONSTRAINT tenant_pkey PRIMARY KEY (id),
  CONSTRAINT tenant_code_ukey UNIQUE (code)
);

