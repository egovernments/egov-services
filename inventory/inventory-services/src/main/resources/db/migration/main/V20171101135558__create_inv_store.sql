Create sequence seq_store;

Create table store(
id character varying(50) NOT NULL,
code character varying(50) NOT NULL,
name character varying(50) NOT NULL,
description character varying(1000) NOT NULL,
department character varying(132) NOT NULL,
storeincharge character varying(132) NOT NULL,
billingaddress character varying(1000) NOT NULL,
deliveryAddress character varying(1000) NOT NULL,
contactno1 character varying(10) NOT NULL,
contactno2 character varying(10),
email character varying(100) NOT NULL,
iscentralstore boolean,
active boolean,
tenantid character varying(232) NOT NULL,
createdby character varying(50) NOT NULL,
createdtime bigint NOT NULL,
lastmodifiedby character varying(50) NOT NULL,
lastmodifiedtime bigint NOT NULL,
constraint primary_inv_store primary key (code,tenantId)
);