create table egw_remarks
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  typeOfDocument character varying(100) NOT NULL,
  remarksType character varying(100) NOT NULL,
  deleted boolean DEFAULT false,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_remarks PRIMARY  KEY(id,tenantId)
  );

  create table egw_remarks_detail
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  remarks character varying(256) NOT NULL,
  remarksDescription character varying(100) NOT NULL,
  deleted boolean DEFAULT false,
  editable boolean DEFAULT true NOT NULL,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_remarks_detail PRIMARY  KEY(id,tenantId)
  );