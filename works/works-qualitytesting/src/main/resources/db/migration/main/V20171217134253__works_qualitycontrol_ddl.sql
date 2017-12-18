CREATE TABLE egw_qualitytesting
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  letterOfAcceptanceEstimate character varying(50),
  remarks character varying(1024) NOT NULL,
  status character varying(100) NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  deleted boolean DEFAULT false,
  CONSTRAINT qualitycontrol_pkey PRIMARY KEY(id,tenantId)
);

CREATE TABLE egw_qualitytesting_details
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  qualityTesting character varying(50) NOT NULL,
  materialName character varying(100) NOT NULL,
  testName character varying(100) NOT NULL,
  resultUnit character varying(100) NOT NULL,
  minimumValue double PRECISION NOT NULL,
  maximumValue double PRECISION NOT NULL,
  hodRemarks character varying(1024) NOT NULL,
  coRemarks character varying(1024) NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  deleted boolean DEFAULT false,
  CONSTRAINT qualitycontrol_details_pkey PRIMARY KEY(id,tenantId)
);