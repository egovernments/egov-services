CREATE TABLE egw_detailedestimate
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  estimatenumber character varying(50) NOT NULL,
  estimatedate bigint NOT NULL,
  nameofwork character varying(1024) NOT NULL,
  description character varying(1024) NOT NULL,
  department character varying(50),
  adminSanctionNumber character varying(50),
  adminSanctionDate bigint,
  adminSanctionBy character varying(256),
  status character varying(50) NOT NULL,
  workValue double PRECISION ,
  estimateValue double PRECISION ,
  projectCode character varying(50),
  parent character varying(256),
  copiedFrom character varying(256),
  approvedDate bigint,
  approvedBy character varying(256),
  copiedEstimate boolean,
  beneficiary character varying(50),
  modeOfAllotment character varying(50),
  worksType character varying(50),
  worksSubtype character varying(50),
  natureofwork character varying(50),
  ward character varying(50),
  location character varying(50),
  LONGITUDE bigint,
  LATITUDE bigint,
  workCategory character varying(50),
  locality character varying(50),
  councilResolutionNumber character varying(50),
  councilResolutionDate bigint,
  workOrderCreated boolean,
  billsCreated boolean,
  spillOverFlag boolean,
  grossAmountBilled double precision,
  cancellationReason character varying(100),
  cancellationRemarks character varying(512),
  totalIncludingRE double precision,
  abstractEstimateDetail character varying(256),
  stateId character varying(50),
  fund character varying(50),
  function character varying(50),
  functionary character varying(50),
  scheme character varying(50),
  subScheme character varying(50),
  budgetGroup character varying(50),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  CONSTRAINT detailed_estimate_pkey PRIMARY  KEY(id,tenantId),
  CONSTRAINT detailed_estimate_unique_key UNIQUE (tenantId, estimatenumber)
);


CREATE TABLE egw_estimate_assets
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  asset character varying(256) NOT NULL,
  detailedEstimate character varying(256) NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  CONSTRAINT assets_estimate_pkey PRIMARY  KEY(id,tenantId)
);

CREATE TABLE egw_estimate_deductions
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  chartOfAccounts character varying(256) NOT NULL,
  detailedEstimate character varying(256) NOT NULL,
  percentage double precision,
  amount double precision,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  CONSTRAINT estimate_deductions_pkey PRIMARY  KEY(id,tenantId)
);


CREATE TABLE egw_estimate_activity
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  scheduleOfRate character varying(256),
  nonSor character varying(256),
  uom character varying(256) NOT NULL,
  unitRate double precision NOT NULL,
  estimateRate double precision NOT NULL,
  quantity double precision,
  servicetaxperc double precision,
  revisionType character varying(50),
  parent character varying(256),
  detailedEstimate character varying(256) NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  CONSTRAINT estimate_activity_pkey PRIMARY  KEY(id,tenantId)
);

CREATE TABLE egw_multiyear_estimate
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  detailedestimate character varying(256) NOT NULL,
  financialYear character varying(256) NOT NULL,
  percentage double precision NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  CONSTRAINT multiyear_estimate_pkey PRIMARY  KEY(id,tenantId),
  CONSTRAINT multiyear_estimate_unique_key UNIQUE (tenantId, detailedestimate, financialYear)
);

CREATE TABLE egw_estimate_overheads
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  overhead character varying(256),
  amount double precision,
  detailedEstimate character varying(256) NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  CONSTRAINT estimate_overheads_pkey PRIMARY  KEY(id,tenantId),
  CONSTRAINT estimate_overheads_unique_key UNIQUE (tenantId, overhead, detailedEstimate)

);

CREATE TABLE egw_estimate_measurementsheet
(
 id character varying(256),
 tenantId character varying(256) NOT NULL,
 slNo bigint,
 identifier character varying(2),
 remarks character varying(1024),
 no double PRECISION ,
 length double PRECISION,
 width double PRECISION,
 depthOrHeight double PRECISION,
 quantity double PRECISION NOT NULL,
 estimateActivity character varying(256) NOT NULL,
 parent character varying(256),
 createdby character varying(256) NOT NULL,
 createdtime bigint NOT NULL,
 lastmodifiedby character varying(256),
 lastmodifiedtime bigint,
 CONSTRAINT estimate_measurement_pkey PRIMARY  KEY(id,tenantId)
);


CREATE TABLE egw_estimate_technicalsanction
(
 id character varying(256),
 tenantId character varying(256) NOT NULL,
 technicalSanctionNumber character varying(50) NOT NULL,
 detailedEstimate character varying(256) NOT NULL,
 technicalSanctionDate bigint NOT NULL,
 technicalSanctionBy character varying(256) NOT NULL,
 createdby character varying(256) NOT NULL,
 createdtime bigint NOT NULL,
 lastmodifiedby character varying(256),
 lastmodifiedtime bigint,
 CONSTRAINT estimate_technicalsanction_pkey PRIMARY  KEY(id,tenantId),
 CONSTRAINT estimate_technicalsanction_unique_key UNIQUE (tenantId, technicalSanctionNumber)
);
