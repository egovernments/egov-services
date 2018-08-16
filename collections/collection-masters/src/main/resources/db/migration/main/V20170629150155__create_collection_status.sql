CREATE SEQUENCE SEQ_EGCL_STATUS
 START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE egcl_status
(
  id bigint NOT NULL,
  code character varying(30) NOT NULL,
  objecttype character varying(50) NOT NULL,
  description character varying(50),
  tenantid character varying(252) NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT cons_primary_id PRIMARY KEY(id,tenantid),
  CONSTRAINT cons_unq_code_objtype UNIQUE(code,objecttype,tenantid)
);