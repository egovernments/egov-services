create sequence seq_disposal;

create table disposal (
id character varying(50) NOT NULL,
tenantId character varying(128) NOT NULL,
store character varying(50) NOT NULL,
disposalnumber character varying(50) NOT NULL,
disposaldate bigint NOT NULL,
handoverto character varying(50),
auctionnumber character varying(50),
description character varying(200),
disposalstatus character varying(50),
stateid bigint,
totaldisposalvalue numeric(13,2),
createdby character varying(50) NOT NULL,
createdtime bigint NOT NULL,
lastmodifiedby character varying(50),
lastmodifiedtime bigint
);

create sequence seq_disposaldetail;

create table disposaldetail (
id character varying(50) NOT NULL,
tenantId character varying(128) NOT NULL,
userdisposalquantity numeric(13,2) NOT NULL,
disposalquantity numeric(13,2) NOT NULL,
material character varying(100),
uom character varying(100),
disposalvalue numeric(13,2) NOT NULL,
disposalnumber character varying(50) NOT NULL,
scrapdetailid character varying(50) NOT NULL
);