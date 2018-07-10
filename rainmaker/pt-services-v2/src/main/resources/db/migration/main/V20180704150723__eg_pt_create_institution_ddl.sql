ALTER TABLE eg_pt_propertydetail_v2
ADD COLUMN adhocExemption numeric,
ADD COLUMN adhocPenalty numeric,
ADD COLUMN accountId character varying(64);

ALTER TABLE eg_pt_owner_v2
ADD COLUMN institutionId character varying(64),
ADD COLUMN relationship character varying(64);

ALTER TABLE eg_pt_document_v2
RENAME COLUMN propertydetail TO foreignkeyid;

ALTER TABLE eg_pt_document_v2
ADD COLUMN documentuid  character varying(64);

ALTER TABLE eg_pt_document_v2
DROP CONSTRAINT fk_eg_pt_document_v2;

ALTER TABLE eg_pt_property_v2
DROP COLUMN accountId;

CREATE TABLE eg_pt_institution_v2 (
  tenantId character varying(256),
  id character varying(64),
  propertydetail character varying(128),
  name character varying(64),
  type character varying(64),
  designation character varying(64),
  createdby character varying(64),
  createdtime bigint,
  lastmodifiedby character varying(64),
  lastmodifiedtime bigint,

  CONSTRAINT pk_eg_pt_institution_v2 PRIMARY KEY (id),
  CONSTRAINT fk_eg_pt_institution_v2 FOREIGN KEY (propertydetail) REFERENCES eg_pt_propertydetail_v2 (assessmentNumber)
);