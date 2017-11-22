CREATE TABLE egw_measurementbook
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  mbRefNo character varying(50) NOT NULL,
  contractorComments character varying(1024),
  mbDate bigint NOT NULL,
  mbIssuedDate bigint,
  mbAbstract character varying(1024) NOT NULL,
  fromPageNo numeric NOT NULL,
  toPageNo numeric,
  letterOfAcceptanceEstimate character varying(256) NOT NULL,
  status character varying(256) NOT NULL,
  isLegacyMB boolean DEFAULT false,
  mbAmount double precision NOT NULL,
  approvedDate bigint,
  stateId character varying(50),
  cancellationreason character varying(512),
  cancellationremarks character varying(512),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  CONSTRAINT measurementbook_pkey PRIMARY KEY(id,tenantId)
  );
  
CREATE TABLE egw_measurementbook_details
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  measurementBook character varying(256) NOT NULL,
  loaActivity character varying(256) NOT NULL,
  quantity double precision NOT NULL,
  rate double precision NOT NULL,
  remarks character varying(1024),
  amount double precision,
  partRate double precision,
  reducedRate double precision,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  CONSTRAINT measurementbook_details_pkey PRIMARY KEY(id,tenantId)
);

  CREATE TABLE egw_mb_contractor_bills
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  measurementBook character varying(256),
  contractorBill character varying(256) NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  CONSTRAINT mb_contractor_bills_pkey PRIMARY KEY(id,tenantId)
);

CREATE TABLE egw_mb_measurementsheet
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  remarks character varying(1024),
  number bigint,
  length bigint,
  width bigint,
  depthOrHeight bigint,
  quantity bigint NOT NULL,
  measurementBookDetail character varying(256) NOT NULL,
  loaMeasurementSheet character varying(256) NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  CONSTRAINT mb_measurementsheet_pkey PRIMARY KEY(id,tenantId)
);
