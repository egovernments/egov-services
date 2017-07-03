CREATE TABLE egasset_statuses
(
  objectname character varying(50),
  code character varying(20),
  description character varying(250),
  createdby character varying(64) NOT NULL,
  createddate bigint NOT NULL,
  lastmodifiedby character varying(64),
  lastmodifieddate bigint,
  tenantid character varying(25),
  CONSTRAINT "egasset_statuses_objectname_code_tenantid_key" UNIQUE (objectname, code, tenantid)
);

insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Asset Status','CREATED','Asset is Created','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Asset Status','CAPITALIZED','Asset is Capitalized','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Asset Status','CANCELLED','Asset is Cancelled','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Reevaluation Status','INACTIVE','Asset is Inactive','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Reevaluation Status','ACTIVE','Asset is Active','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Disposal Status','DISPOSED','Asset is Disposed','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
	
--rollback delete from egasset_statuses where code in ('CREATED','CAPITALIZED','CANCELLED','INACTIVE','ACTIVE','DISPOSED') 
--rollback   and tenantid='default';
--rollback drop table egasset_statuses;