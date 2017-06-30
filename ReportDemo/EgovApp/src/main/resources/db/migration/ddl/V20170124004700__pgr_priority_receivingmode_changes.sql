--Receiving Mode Type Master

CREATE TABLE egpgr_receivingmode
(
  id SERIAL,
  name character varying(150),
  code character varying(50),
  description character varying(50) null,
  visible boolean,
  active boolean DEFAULT true,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  version bigint DEFAULT 0,
  tenantid character varying(256) NOT NULL,
  CONSTRAINT egpgr_receivingmode_pkey PRIMARY KEY (id),
  CONSTRAINT uk_receivingmode_code_tenant UNIQUE (code, tenantid)
);



create sequence seq_egpgr_receivingmode;
