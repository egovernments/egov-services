insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Get Child Boundary By Boundary','/boundarys/childLocationsByBoundaryId','LOCATION_MS', NULL,(select id from service where code='LOCATION_MS' and tenantid='default'), 1,'Get Child Boundary By Boundary',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get Child Boundary By Boundary'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get Child Boundary By Boundary'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get Child Boundary By Boundary'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get Child Boundary By Boundary'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get Child Boundary By Boundary'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get Child Boundary By Boundary'),'default');
