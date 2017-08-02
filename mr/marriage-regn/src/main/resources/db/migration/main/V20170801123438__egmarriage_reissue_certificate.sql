
CREATE TABLE egmr_reissuecertificate
(
  id character varying NOT NULL,
  regnno character varying,
  applicantname character varying,
  applicantaddress character varying(250),
  applicantmobileno character varying(250),
  applicantfee numeric,
  applicantaadhaar character varying(250),
  applicationnumber character varying,
  reissueapplstatus character varying,
  stateid character varying,
  approvaldepartment integer,
  approvaldesignation integer,
  approvalassignee integer,
  approvalaction character varying(250),
  approvalstatus character varying(250),
  approvalcomments character varying(250),
  demands character varying(250),
  rejectionreason character varying(250),
  remarks character varying(250),
  isactive boolean,
  createdby character varying(250) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(250),
  lastmodifiedtime bigint,
  tenantid character varying(250) NOT NULL,
  CONSTRAINT pk_egmr_reissuecertificate PRIMARY KEY (id, tenantid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.egmr_reissuecertificate
  OWNER TO postgres;





