Create table egf_billchecklist(
id varchar(50),
billId varchar(50),
checklistId varchar(50),
checklistValue varchar(50),
createdby varchar(50),
createddate timestamp without time zone,
lastmodifiedby varchar(50),
lastmodifieddate timestamp without time zone,
tenantId varchar(250),
version bigint
);
alter table egf_billchecklist add constraint pk_egf_billchecklist primary key (id);
create sequence seq_egf_billchecklist;