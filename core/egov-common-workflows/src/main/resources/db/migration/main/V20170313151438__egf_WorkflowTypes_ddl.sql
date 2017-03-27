
Create table EG_WF_TYPES( 
	id bigint,
	module bigint,
	type varchar(50),
	typeFQN varchar(50),
	link varchar(50),
	displayName varchar(50),
	enabled boolean,
	grouped boolean,
		createdby bigint,
		createddate timestamp without time zone,
		lastmodifiedby bigint,
		lastmodifieddate timestamp without time zone,
		version bigint,
		tenantId varchar(128)
		
);
alter table EG_WF_TYPES add constraint pk_EG_WF_TYPES primary key (id);
create sequence seq_EG_WF_TYPES;
