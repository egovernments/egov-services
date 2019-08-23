CREATE TABLE egov-pdf-gen
(
  jobid character varying(50) NOT NULL,
  tenantid character varying(20),
  createdtime character varying(20),
  filestoreids json,
  endtime character varying(20),
  CONSTRAINT egov-pdf-gen-pkey PRIMARY KEY (jobid)
)