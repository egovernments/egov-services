
Create table store( 
	id varchar(32),
	tenantId varchar(128),
	code varchar(50),
	name varchar(50),
	description varchar(1000),
	department varchar(50),
	officeLocation varchar(50),
	billingAddress varchar(1000),
	deliveryAddress varchar(1000),
	contactNo1 varchar(10),
	contactNo2 varchar(50),
	email varchar(100),
	storeInCharge varchar(50),
	isCentralStore boolean,
	active boolean,
	createdby bigint,
		createdTime timestamp without time zone,
		lastmodifiedby bigint,
		lastModifiedTime timestamp without time zone,
		version bigint
);
alter table store add constraint pk_store primary key (code,tenantId);
create sequence seq_store;
