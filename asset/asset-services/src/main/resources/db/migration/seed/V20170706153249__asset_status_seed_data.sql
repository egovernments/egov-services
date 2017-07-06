insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Asset Master','CREATED','Asset status is Created','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Asset Master','CAPITALIZED','Asset status is Capitalized','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Asset Master','CANCELLED','Asset status is Cancelled','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Asset Master','DISPOSED','Asset status is Disposed','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Revaluation','CREATED','Asset Revaluation is created','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Revaluation','APPROVED','Asset Revaluation is approved','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Revaluation','CANCELLED','Asset Revaluation is Cancelled','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Disposal','CREATED','Asset Disposal is created','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Disposal','APPROVED','Asset Disposal is approved','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values('Disposal','CANCELLED','Asset Disposal is Cancelled','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

--rollback delete from egasset_statuses where objectname in ('Asset Master','Revaluation','Disposal');
