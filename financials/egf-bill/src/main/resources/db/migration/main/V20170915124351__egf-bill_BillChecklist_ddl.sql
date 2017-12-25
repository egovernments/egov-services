Create table egf_billchecklist(
	id varchar(256),
	bill varchar(256),
	checklist varchar(256),
	checklistValue varchar(256),
	createdby varchar(256),
	createdtime bigint,
	lastmodifiedby varchar(256),
	lastmodifiedtime bigint,
	tenantId varchar(256),
	version bigint
);
alter table egf_billchecklist add constraint pk_egf_billchecklist primary key (id,tenantId);
create sequence seq_egf_billchecklist;