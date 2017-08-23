create sequence SEQ_EGWTR_SERVICECHARGE;

create table egwtr_servicecharge
(
id bigint NOT NULL,
code character varying(20) NOT NULL,
servicetype character varying(100) NOT NULL,
servicechargeapplicable boolean NOT NULL,
servicechargetype character varying(100) NOT NULL,
description character varying(1024),
effectivefrom bigint NOT NULL,
effectiveto bigint NOT NULL,
outsideulb boolean,
createdby bigint NOT NULL,
createddate bigint,
lastmodifiedby bigint NOT NULL,
lastmodifieddate bigint,
tenantid character varying (250) NOT NULL,
CONSTRAINT pk_service_charge PRIMARY KEY (id,tenantid),
CONSTRAINT unq_service_charge UNIQUE (code,tenantid)
);


create sequence SEQ_EGWTR_SERVICECHARGE_DETAILS;

create table egwtr_servicecharge_details
(
id bigint NOT NULL,
code character varying(20) NOT NULL,
uomfrom double precision NOT NULL,
uomto double precision NOT NULL,
amountorpercentage double precision NOT NULL,
servicecharge bigint NOT NULL,
tenantid character varying(250) NOT NULL,
CONSTRAINT pk_service_charge_details PRIMARY KEY (id,tenantid),
CONSTRAINT unq_service_charge_details UNIQUE (code,tenantid)
);



