CREATE TABLE eg_pt_drafts_v2(

  id character varying(64),
  tenantId character varying(256),
  userId character varying(64),
  draft JSONB,

  CONSTRAINT pk_eg_pt_drafts_v2 PRIMARY KEY (id,tenantid),
  CONSTRAINT uk_eg_pt_drafts_v2 UNIQUE (id)
);