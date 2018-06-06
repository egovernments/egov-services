CREATE TABLE eg_pt_billingslab_v2(

  id character varying(64),
  tenantId character varying(256),
  propertyType character varying(64),
  propertySubType character varying(64),
  usageCategoryMajor character varying(64),
  usageCategoryMinor character varying(64),
  usageCategorySubMinor character varying(64),
  usageCategoryDetail character varying(64),
  ownerShipCategory character varying(64),
  subOwnerShipCategory character varying(256),
  fromFloor character varying(64),
  toFloor character varying(64),
  area character varying(64),
  fromPlotSize character varying(64),
  toPlotSize character varying(64),
  unitRate character varying(64),
  createdby character varying(64),
  createdtime bigint,
  lastmodifiedby character varying(64),
  lastmodifiedtime bigint,

  CONSTRAINT pk_eg_pt_billingslab_v2 PRIMARY KEY (id, tenantid),
  CONSTRAINT uk_eg_pt_billingslab_v2 UNIQUE (id)
);