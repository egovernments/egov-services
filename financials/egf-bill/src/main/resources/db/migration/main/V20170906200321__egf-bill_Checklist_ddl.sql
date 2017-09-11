
Create table egf_checklist( 
  id varchar(50),
  type varchar(50),
  subType varchar(50),
  key varchar(250),
  description varchar(250),
    createdby varchar(50),
		createddate timestamp without time zone,
		lastmodifiedby varchar(50),
		lastmodifieddate timestamp without time zone,
		tenantId varchar(250),
		version bigint
);
alter table egf_checklist add constraint pk_egf_checklist primary key (id);
create sequence seq_egf_checklist;
