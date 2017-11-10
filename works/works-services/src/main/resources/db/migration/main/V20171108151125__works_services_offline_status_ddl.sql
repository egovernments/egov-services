CREATE TABLE egw_offlinestatus
(
  id character varying(256) NOT NULL,
  objecttype character varying(128) NOT NULL,
  objectnumber character varying(256) NOT NULL,  
  remarks character varying(256),
  statusdate bigint NOT NULL,
  status character varying(256) NOT NULL,
  createdby character varying(256) NOT NULL,
  lastmodifiedby character varying(256),
  createdtime bigint NOT NULL,
  lastmodifiedtime bigint,
  tenantId character varying(250) NOT NULL,
  CONSTRAINT offlinestatus_pkey PRIMARY  KEY (id,tenantId)
);