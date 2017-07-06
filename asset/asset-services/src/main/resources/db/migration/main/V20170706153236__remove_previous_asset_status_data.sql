delete from egasset_statuses;

--rollback insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
--rollback	values('Asset Status','CREATED','Asset is Created','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
--rollback insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
--rollback	values('Asset Status','CAPITALIZED','Asset is Capitalized','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
--rollback insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
--rollback	values('Asset Status','CANCELLED','Asset is Cancelled','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
--rollback insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
--rollback	values('Reevaluation Status','INACTIVE','Asset is Inactive','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
--rollback insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
--rollback	values('Reevaluation Status','ACTIVE','Asset is Active','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
--rollback insert into egasset_statuses(objectname,code,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
--rollback	values('Disposal Status','DISPOSED','Asset is Disposed','1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
