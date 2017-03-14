
Create table EG_WF_ACTION( 
	id bigint,
	name varchar(255) NOT NULL,
	description varchar(1024) NOT NULL,
	type varchar(255) NOT NULL,
		createdby bigint,
		createddate timestamp without time zone,
		lastmodifiedby bigint,
		lastmodifieddate timestamp without time zone,
		version bigint,
		tenantId varchar(250)
);
alter table EG_WF_ACTION add constraint pk_EG_WF_ACTION primary key (id);
create sequence seq_EG_WF_ACTION;
