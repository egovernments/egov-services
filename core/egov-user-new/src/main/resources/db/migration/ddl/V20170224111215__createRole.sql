CREATE TABLE eg_role
(
  id bigint NOT NULL,
  name character varying(32) NOT NULL,
  code character varying(50) NOT NULL,
  description character varying(128),
  createddate bigint,
  createdby bigint,
  lastmodifiedby bigint,
  lastmodifieddate bigint,
  tenantid character varying(256) NOT NULL,
  CONSTRAINT eg_role_pk PRIMARY KEY (id, tenantid),
  CONSTRAINT eg_roles_code_tenant UNIQUE (code, tenantid)
)