insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'SearchBoundaryType','/egov-location/boundarytypes/_search','LOCATION_MS', NULL,(select id from service where code='LOCATION_MS' and tenantid='default'), 1,'Search BoundaryType',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'SearchHierarchyTypes','/egov-location/hierarchytypes/_search','LOCATION_MS', NULL,(select id from service where code='LOCATION_MS' and tenantid='default'), 1,'Search HierarchyTypes',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchBoundaryType'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchHierarchyTypes'),'default');

