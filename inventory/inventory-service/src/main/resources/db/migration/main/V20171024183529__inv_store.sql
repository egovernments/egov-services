Create sequence seq_stores;

Create table stores(
id character varying(50) NOT NULL,
code character varying(50) NOT NULL,
name character varying(50) NOT NULL,
description character varying(1000) NOT NULL,
departmentcode character varying(132) NOT NULL,
storeinchargecode character varying(132) NOT NULL,
billingaddress character varying(1000) NOT NULL,
deliveryAddress character varying(1000) NOT NULL,
contactnumber1 character varying(10) NOT NULL,
contactnumber2 character varying(10),
emailid character varying(100) NOT NULL,
iscentralstore boolean,
active boolean,
tenantid character varying(232) NOT NULL,
createdby character varying(50) NOT NULL,
createddate bigint NOT NULL,
lastmodifiedby character varying(50) NOT NULL,
lastmodifieddate bigint NOT NULL
);