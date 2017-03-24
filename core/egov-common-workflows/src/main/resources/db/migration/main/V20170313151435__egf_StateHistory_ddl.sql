
create table eg_wf_state_history( 
	id bigint,
	state_id bigint ,
	value varchar(50) not null,
	owner_pos bigint,
	owner_user bigint,
	sendername varchar(50),
	nextaction varchar(50),
	comments varchar(50),
	natureoftask varchar(50),
	extrainfo varchar(50),
	dateinfo date,
	extradateinfo date,
	initiator_pos bigint,
		createdby bigint,
		createddate timestamp without time zone,
		lastmodifiedby bigint,
		lastmodifieddate timestamp without time zone,
		version bigint,
		tenantId varchar(250)
);
alter table eg_wf_state_history add constraint pk_eg_wf_state_history primary key (id);
alter table eg_wf_state_history add constraint fk_eg_wf_state_history_state_id  foreign key (state_id) references eg_wf_states(id);
create sequence seq_eg_wf_state_history;
