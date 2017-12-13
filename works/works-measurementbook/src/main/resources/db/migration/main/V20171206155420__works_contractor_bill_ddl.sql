
------------------START------------------
CREATE TABLE egw_contractorbill
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  BILLSEQUENCENUMBER character varying(50),
  APPROVEDDATE timestamp without time zone,
  approvedby character varying(256),
  CANCELLATIONREASON character varying(100),
  CANCELLATIONREMARKS character varying(512),
  letterOfAcceptanceEstimate character varying(256) NOT NULL,
  stateId character varying(50),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  deleted boolean DEFAULT false,
  CONSTRAINT egw_contractor_bill_pkey PRIMARY KEY(id,tenantId)
);
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_contractorbill_assets
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  asset character varying(256) NOT NULL,
  chartofaccounts character varying(256) ,
  description character varying(1024),  
  amount double precision NOT NULL,
  contractorbill character varying(256) NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  deleted boolean DEFAULT false,
  CONSTRAINT egw_contractorbill_assets_pk PRIMARY KEY (id,tenantId)
);
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_contractorbill_mb
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  measurementBook character varying(256),
  contractorBill character varying(256) NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  deleted boolean DEFAULT false,
  CONSTRAINT egw_contractorbill_mb_pkey PRIMARY KEY(id,tenantId)
);
-------------------END-------------------

