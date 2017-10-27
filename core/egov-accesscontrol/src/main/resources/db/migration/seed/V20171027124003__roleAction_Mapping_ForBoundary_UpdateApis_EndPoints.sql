insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Update Boundary','/egov-location/boundarys/{code}','LOCATION_MS', NULL,(select id from service where code='LOCATION_MS' and tenantid='default'), 1,'Update Boundary',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Update BoundaryType','/egov-location/boundarytypes/{code}','LOCATION_MS', NULL,(select id from service where code='LOCATION_MS' and tenantid='default'), 1,'Update BoundaryType',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Update CrossHierarchy','/egov-location/crosshierarchys/{code}','LOCATION_MS', NULL,(select id from service where code='LOCATION_MS' and tenantid='default'), 1,'Update CrossHierarchy',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Update HierarchyType','/egov-location/hierarchytypes/{code}','LOCATION_MS', NULL,(select id from service where code='LOCATION_MS' and tenantid='default'), 1,'Update HierarchyType',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Update Boundary'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Update BoundaryType'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Update CrossHierarchy'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Update HierarchyType'),'default');