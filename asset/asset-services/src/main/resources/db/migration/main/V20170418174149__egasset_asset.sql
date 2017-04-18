CREATE SEQUENCE seq_egasset_asset;
CREATE SEQUENCE seq_egasset_assetcode;

CREATE TABLE egasset_asset
(
  id bigint NOT NULL,
  assetcategory bigint NOT NULL,
  name character varying(250) NOT NULL,
  code character varying(100) NOT NULL,
  department bigint ,
  assetdetails character varying(4000),
  description character varying(1024),
  dateofcreation timestamp without time zone,-- capture through the screen
  remarks character varying(250),
  length character varying(250),
  width character varying(250),
  totalarea character varying(250),
  modeofacquisition character varying(250),
  status character varying(250) NOT NULL,--Todo phash-2 
  tenantid character varying(250) NOT NULL,
  zone bigint,
  revenueward bigint,
  street bigint,
  electionward bigint,
  doorno bigint,
  pincode bigint,
  locality bigint,
  block bigint,
  properties character varying(5000),
  createdby character varying(64) NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifiedby character varying(64),
  lastmodifieddate timestamp without time zone,
  grossvalue numeric,
  accumulateddepreciation numeric,
  CONSTRAINT pk_egasset_asset PRIMARY KEY (id,tenantid)
);
