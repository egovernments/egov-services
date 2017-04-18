CREATE SEQUENCE seq_eglams_notice;
CREATE SEQUENCE seq_eglams_noticeno;

CREATE TABLE eglams_notice
(
  id bigint NOT NULL,
  noticeno character varying(64),
  noticedate timestamp without time zone,
  agreementno character varying(64),
  assetcategory bigint NOT NULL,
  acknowledgementnumber character varying(64),
  assetno bigint NOT NULL,
  allotteename character varying(256),
  allotteeaddress character varying(1024),
  allotteemobilenumber character varying(64),
  agreementperiod character varying(32),
  commencementdate timestamp without time zone,
  templateversion character varying(64),
  expirydate timestamp without time zone,
  rent numeric(12,2),
  securitydeposit numeric(12,2),
  commissionername character varying(256),
  zone bigint,
  ward bigint,
  street bigint,
  electionward bigint,
  locality bigint,
  block bigint,
  createdby character varying(64),
  lastmodifiedby character varying(64),
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  tenantid character varying NOT NULL,
  CONSTRAINT pk_eglams_notice PRIMARY KEY (id, tenantid)
);
