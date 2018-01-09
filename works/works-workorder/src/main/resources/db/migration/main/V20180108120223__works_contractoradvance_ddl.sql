create table egw_contractoradvance
(
  id character varying(256) not null,
  tenantid character varying(256) not null,
  letterofacceptanceestimate character varying(256) not null,
  stateId character varying(256),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  deleted boolean DEFAULT false,
  constraint pk_egw_contractoradvance PRIMARY KEY(id, tenantId)
  );