
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Search Boundary','/egov-location/boundarys/_search','LOCATION_MS', NULL,(select id from service where code='LOCATION_MS' and tenantid='default'), 1,'Search Boundary',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Search Boundary'),'default');

