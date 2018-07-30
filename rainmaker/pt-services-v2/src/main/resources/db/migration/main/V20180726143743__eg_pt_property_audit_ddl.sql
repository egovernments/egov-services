CREATE TABLE eg_pt_property_audit_v2(

  PropertyId character varying(64),
  tenantId character varying(256),
  acknowldgementNumber character varying(64),
  status character varying(64),
  oldPropertyId character varying(256),
  creationReason character varying(256),
  occupancyDate bigint,
  createdby character varying(64),
  createdtime bigint,
  lastmodifiedby character varying(64),
  lastmodifiedtime bigint
);

CREATE TABLE eg_pt_address_audit_v2 (
  tenantId character varying(256),
  id character varying(64),
  property character varying(64),
  latitude numeric(9,6),
  longitude numeric(10,7),
  addressid character varying(64),
  addressnumber character varying(64),
  doorNo character varying(64),
  type character varying(64),
  addressline1 character varying(1024),
  addressline2 character varying(1024),
  landmark character varying(1024),
  city character varying(1024),
  pincode character varying(6),
  detail character varying(2048),
  buildingName character varying(1024),
  street character varying(1024),
  locality character varying(64),
  createdby character varying(64),
  createdtime bigint,
  lastmodifiedby character varying(64),
  lastmodifiedtime bigint
);