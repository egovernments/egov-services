
create table eg_wf_states( 
	id bigint,
	type varchar(50) not null,
	value varchar(1024) not null,
	owner_pos bigint,
	owner_user bigint,
	sendername varchar(50),
	nextaction varchar(50),
	comments varchar(50),
	natureoftask varchar(50),
	extrainfo varchar(50),
	dateinfo date,
	extradateinfo date,
	status varchar(10) not null,
	initiator_pos bigint,
		createdby bigint,
		createddate timestamp without time zone,
		lastmodifiedby bigint,
		lastmodifieddate timestamp without time zone,
		version bigint,
		tenantId varchar(250)
);
alter table eg_wf_states add constraint pk_eg_wf_states primary key (id);
create sequence seq_eg_wf_states;
