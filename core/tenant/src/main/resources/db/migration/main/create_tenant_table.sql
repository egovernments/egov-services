CREATE SEQUENCE seq_tenant;

CREATE TABLE tenant
(
  id bigint NOT NULL,
  code character varying(50) NOT NULL,
  name character varying(100) NOT NULL,
  tenantdetails character varying(256),
  domainurl character varying(128),
  localname character varying(256),
  active boolean,
  version bigint,
  latitude double precision,
  longitude double precision,
  logoid bigint NOT NULL,
  imageid bigint NOT NULL,
  createdby bigint DEFAULT 1,
  createddate timestamp DEFAULT now(),
  lastmodifiedby bigint DEFAULT 1,
  lastmodifieddate timestamp DEFAULT now(),
  grade character varying(50),
  regionname character varying(50),
  recaptchapub character varying(64),
  districtcode character varying(10),
  districtname character varying(50),
  CONSTRAINT tenant_pkey PRIMARY KEY (id),
  CONSTRAINT tenant_code_ukey UNIQUE (code)
);

