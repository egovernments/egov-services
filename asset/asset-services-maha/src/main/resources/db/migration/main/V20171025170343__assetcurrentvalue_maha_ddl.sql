CREATE SEQUENCE seq_egasset_current_value;

CREATE TABLE egasset_current_value( 
id bigint NOT NULL,
tenantid character varying(64) NOT NULL,
assetid bigint NOT NULL,
currentamount numeric(12,2) NOT NULL,
createdby character varying(16) NOT NULL,
createdtime bigint NOT NULL,
lastmodifiedby character varying(16),
lastmodifiedtime bigint,
assettrantype character varying(64) NOT NULL,
CONSTRAINT pk_asset_current_value PRIMARY KEY (id, tenantid)
);
