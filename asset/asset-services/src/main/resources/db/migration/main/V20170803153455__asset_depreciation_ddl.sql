CREATE SEQUENCE seq_egasset_depreciation;

CREATE TABLE egasset_depreciation (
id bigint NOT NULL,
assetid bigint NOT NULL,
financialyear character varying(16) ,
fromdate bigint,
todate bigint,
voucherreference bigint,
status character varying(64) NOT NULL,
depreciationrate numeric(12,2) NOT NULL,
valuebeforedepreciation numeric(12,2) NOT NULL,
depreciationvalue  numeric(12,2) ,
valueafterdepreciation numeric(12,2) ,
tenantid character varying(64) NOT NULL,
createdby character varying(64) NOT NULL,
createdtime bigint NOT NULL,
lastmodifiedby character varying(64),
lastmodifiedtime  bigint,
reasonforfailure character varying(256),
CONSTRAINT pk_depreciation PRIMARY KEY(id,tenantid)
);
