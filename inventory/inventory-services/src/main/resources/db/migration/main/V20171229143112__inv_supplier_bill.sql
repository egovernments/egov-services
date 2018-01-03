CREATE TABLE supplierbill (
id VARCHAR(50),
tenantid VARCHAR(128) NOT NULL,
store VARCHAR(50),
invoicenumber VARCHAR(50),
invoicedate bigint,
approveddate bigint,
approvedby bigint,
cancellationreason VARCHAR (500),
cancellationremarks VARCHAR (500),
stateid VARCHAR (50),
status VARCHAR (50),
billid VARCHAR (50),
deleted boolean DEFAULT FALSE,
createdby VARCHAR(50),
createdtime bigint,
lastmodifiedby VARCHAR(50),
lastmodifiedtime bigint,
version bigint
);

alter table supplierbill add constraint pk_supplierbill primary key (id,tenantid);

create sequence seq_supplierbill;

CREATE TABLE supplierbillreceipt (
id VARCHAR(50),
tenantid VARCHAR(128) NOT NULL,
supplierbill VARCHAR(50) NOT NULL,
materialreceipt VARCHAR(50) NOT NULL,
createdby VARCHAR(50),
createdtime bigint,
lastmodifiedby VARCHAR(50),
lastmodifiedtime bigint,
version bigint
);

alter table supplierbillreceipt add constraint pk_supplierbillreceipt primary key (id,tenantid);

create sequence seq_supplierbillreceipt;


CREATE TABLE supplierbilladvanceadjustment (
id VARCHAR(50),
tenantid VARCHAR(128) NOT NULL,
supplierbill VARCHAR(50),
supplieradvancerequisition VARCHAR(50),
advanceadjustedamount numeric(13,2),
createdby VARCHAR(50),
createdtime bigint,
lastmodifiedby VARCHAR(50),
lastmodifiedtime bigint,
version bigint
);

alter table supplierbilladvanceadjustment add constraint pk_supplierbilladvanceadjustment primary key (id,tenantid);

create sequence seq_supplierbilladvanceadjustment;

CREATE TABLE supplieradvancerequisition (
id VARCHAR(50),
tenantid VARCHAR(128) NOT NULL,
supplier VARCHAR(50),
purchaseorder VARCHAR(50),
advanceadjustedamount numeric(13,2),
advancefullyadjustedinbill boolean,
stateid VARCHAR (50),
status VARCHAR (50),
createdby VARCHAR(50),
createdtime bigint,
lastmodifiedby VARCHAR(50),
lastmodifiedtime bigint,
version bigint
);

alter table supplieradvancerequisition add constraint pk_supplieradvancerequisition primary key (id,tenantid);

create sequence seq_supplieradvancerequisition;
