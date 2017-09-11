CREATE SEQUENCE seq_egmr_marriageregn_application_number;
CREATE SEQUENCE seq_egmr_regn_number;
CREATE SEQUENCE seq_egmr_marriageregn_reg_number;

CREATE TABLE egmr_marriage_regn
( 

  id character varying(250) NOT NULL,
  regnunitid integer,
  marriagedate bigint NOT NULL,
  venue character varying(250) NOT NULL,
  street character varying(250) NOT NULL,
  placeofmarriage character varying(250),
  locality character varying(250) NOT NULL,
  city character varying(250) NOT NULL,
  marriagephoto character varying(250) NOT NULL,
  bridegroomid bigint,
  brideid bigint,
  priestname character varying(250),
  priestreligion bigint,
  priestaddress character varying(250),
  priestaadhaar character varying(250),
  priestmobileno character varying(250),
  priestemail character varying(250),
  serialno character varying(250),
  volumeno character varying(250),
  applicationnumber character varying(250) NOT NULL,
  regnnumber character varying(250),
  regndate bigint,
  status character varying(250),
  source character varying(250),
  stateid character varying(250),
  isactive boolean,
  tenantid character varying(250) NOT NULL,
  feeid character varying,
  demandid character varying,
  approvaldepartment bigint,
  approvaldesignation bigint,
  approvalassignee bigint,
  approvalaction character varying(250),
  approvalstatus character varying(250),
  approvalcomments character varying(250),
  createdby character varying(250) NOT NULL,
  lastmodifiedby character varying(250) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedtime bigint NOT NULL,

  CONSTRAINT pk_egmr_marriage_regn PRIMARY KEY (id, tenantid),
  CONSTRAINT uk_egmr_applicationnumber UNIQUE (applicationnumber),
  CONSTRAINT uk_egmr_bridegroomid UNIQUE (bridegroomid),
  CONSTRAINT uk_egmr_brideid UNIQUE (brideid),
  CONSTRAINT uk_egmr_regnnumber UNIQUE (regnnumber)
)





