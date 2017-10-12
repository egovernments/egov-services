insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Create Boundary','/egov-location/boundarys','LOCATION_MS', NULL,(select id from service where code='LOCATION_MS' and tenantid='default'), 1,'Create Boundary',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Create HierarchyType','/egov-location/hierarchytypes','LOCATION_MS', NULL,(select id from service where code='LOCATION_MS' and tenantid='default'), 1,'Create HierarchyType',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Create Boundary'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Create HierarchyType'),'default');