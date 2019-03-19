
CREATE TABLE egbs_demand_audit
(
    id character varying(64) NOT NULL,
	demandid character varying(64) NOT NULL,
    consumercode character varying(250)  NOT NULL,
    consumertype character varying(250) NOT NULL,
    businessservice character varying(250)  NOT NULL,
    payer character varying(250) ,
    taxperiodfrom bigint NOT NULL,
    taxperiodto bigint NOT NULL,
    createdby character varying(256)  NOT NULL,
    createdtime bigint NOT NULL,
    tenantid character varying(250) NOT NULL,
    minimumamountpayable numeric(12,2),
    status character varying(64) ,
    additionaldetails json,
    CONSTRAINT pk_egbs_demand_audit PRIMARY KEY (id, tenantid)
);


CREATE TABLE egbs_demanddetail_audit
(
    id character varying(64)  NOT NULL,
    demandid character varying(64)  NOT NULL,
	demanddetailid character varying(64)  NOT NULL,
    taxheadcode character varying(250)  NOT NULL,
    taxamount numeric(12,2) NOT NULL,
    collectionamount numeric(12,2) NOT NULL,
    createdby character varying(256)  NOT NULL,
    createdtime bigint NOT NULL,
    tenantid character varying(250)  NOT NULL,
    additionaldetails json,
    CONSTRAINT pk_egbs_demanddetail_audit PRIMARY KEY (id, tenantid)
);