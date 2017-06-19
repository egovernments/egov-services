DROP TABLE egpgr_complainttype_category;

CREATE TABLE egpgr_complainttype_category
(
  id bigint NOT NULL,
  code character varying(100) NOT NULL,
  name character varying(100),
  description character varying(250),
  version numeric DEFAULT 0,
  tenantid character varying(256) NOT NULL,
  createdby bigint DEFAULT NULL,
  createddate timestamp DEFAULT NULL,
  lastmodifiedby bigint DEFAULT NULL,
  lastmodifieddate timestamp DEFAULT NULL,
  CONSTRAINT egpgr_complainttype_category_pkey PRIMARY KEY (id),
  CONSTRAINT egpgr_complainttype_category_name_key UNIQUE (code, tenantid)
);
