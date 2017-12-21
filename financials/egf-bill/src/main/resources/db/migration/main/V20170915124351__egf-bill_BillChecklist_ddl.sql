Create table egf_billchecklist(
	id varchar(50),
	bill varchar(50),
	checklist varchar(50),
	checklistValue varchar(50),
	createdby varchar(50),
	createdtime bigint,
	lastmodifiedby varchar(50),
	lastmodifiedtime bigint,
	tenantId varchar(250),
	version bigint
);
alter table egf_billchecklist add constraint pk_egf_billchecklist primary key (id,tenantId);
create sequence seq_egf_billchecklist;