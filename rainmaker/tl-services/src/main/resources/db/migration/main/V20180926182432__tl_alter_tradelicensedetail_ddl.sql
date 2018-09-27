ALTER TABLE eg_tl_tradelicensedetail
ADD COLUMN operationalArea FLOAT,
ADD COLUMN noOfEmployees INTEGER,
ADD COLUMN structureType character varying(64),
ADD COLUMN adhocExemption numeric(12,2),
ADD COLUMN adhocPenalty numeric(12,2),
ADD COLUMN adhocExemptionReason character varying(1024),
ADD COLUMN adhocPenaltyReason character varying(1024);


CREATE TABLE eg_tl_institution (
  tenantId character varying(256),
  id character varying(64),
  tradelicenseId character varying(64),
  name character varying(64),
  type character varying(64),
  designation character varying(64),
  createdby character varying(64),
  createdtime bigint,
  lastmodifiedby character varying(64),
  lastmodifiedtime bigint,

  CONSTRAINT pk_eg_pt_institution PRIMARY KEY (id),
  CONSTRAINT fk_eg_pt_institution FOREIGN KEY (tradelicenseId) REFERENCES eg_tl_TradeLicense (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE
);
