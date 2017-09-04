update eg_action set url='/egov-location/crosshierarchys' where url='/crosshierarchys' and name='Cross Hierarchys' and servicecode='LOCATION_MS'; 

INSERT INTO eg_action (id, name,url,servicecode, queryparams, parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) VALUES (nextval('seq_eg_action'), 'Search Cross Hierarchy', '/egov-location/crosshierarchys/_search', 'LOCATION_MS',null,(select id from service where code='LOCATION_MS' AND contextroot='egov-location' and tenantid='default'), 1, 'Search Cross Hierarchys', 'f',1, now(),1,now());

insert into eg_roleaction (rolecode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='Search Cross Hierarchy' and servicecode='LOCATION_MS'),'default'); update eg_action set url='/egov-location/crosshierarchys' where id=263; 
