 ---Gapcode
CREATE TABLE egwtr_gapcode
(
  id bigint NOT NULL,
  code character varying(20) NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(50),
  active boolean NOT NULL,
  outSideUlb boolean,
  noOfLastMonths character varying(50),
  logic character varying(50),
  createdDate bigint,
  lastModifiedDate bigint,
  createdBy bigint NOT NULL,
  lastModifiedBy bigint NOT NULL,
  tenantid CHARACTER VARYING(250) NOT NULL,  
  CONSTRAINT pk_gapcode PRIMARY KEY (id,tenantid),
  CONSTRAINT unq_gapcode_code UNIQUE (code,tenantid),
  CONSTRAINT unq_gapcode_name UNIQUE (name,tenantid)
  
);
CREATE SEQUENCE seq_egwtr_gapcode;


