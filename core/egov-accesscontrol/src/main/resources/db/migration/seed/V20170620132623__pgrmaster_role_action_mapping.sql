
 
INSERT INTO eg_action (id, name, url, servicecode, parentmodule, displayname, enabled, createdby, 
createddate, lastmodifiedby, lastmodifieddate) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Create a Service Type','pgr-master/service/_create',
 'PGR','PGR','Create a Service Type for PGR', false, 1, now() ,1 ,now());
 
INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('GA',(SELECT id FROM eg_action WHERE url='pgr-master/service/_create'),'default');
 
INSERT INTO eg_action (id, name, url, servicecode, parentmodule, displayname, enabled, createdby, 
createddate, lastmodifiedby, lastmodifieddate) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Update a Service Type','pgr-master/service/{code}/_update',
 'PGR','PGR','Update a Service Type for PGR', false, 1, now() ,1 ,now());
 
INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('GA',(SELECT id FROM eg_action WHERE url='pgr-master/service/{code}/_update'),'default');
 
 
 

 
INSERT INTO eg_action(id, name, url, servicecode, parentmodule, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
VALUES(NEXTVAL('SEQ_EG_ACTION'), 'Create Service Group', '/pgr-master/serviceGroup/_create', 'pgr', 'pgr', 'createServiceGroup', false, 1, now(), 1, now());
 
 
 
 
 
 
INSERT INTO eg_action(id, name, url, servicecode, parentmodule, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
VALUES(NEXTVAL('SEQ_EG_ACTION'), 'Update Service Group', '/pgr-master/serviceGroup/{id}/_update', 'pgr', 'pgr', 'updateServiceGroup', false, 1, now(), 1, now());
 
INSERT INTO eg_roleaction(rolecode, actionid, tenantid) VALUES ('GA', (SELECT id FROM eg_action WHERE url = '/pgr-master/serviceGroup/{id}/_update'), 'default');
 
INSERT INTO eg_action(id, name, url, servicecode, parentmodule, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
VALUES(NEXTVAL('SEQ_EG_ACTION'), 'Create Escalation Time Type', '/workflow/escalation-hours/_create', 'pgr', 'pgr', 'createEscalationTimeType', false, 1, now(), 1, now());
 
INSERT INTO eg_roleaction(rolecode, actionid, tenantid) VALUES ('GA', (SELECT id FROM eg_action WHERE url = '/workflow/escalation-hours/_create'), 'default');
 
INSERT INTO eg_action(id, name, url, servicecode, parentmodule, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
VALUES(NEXTVAL('SEQ_EG_ACTION'), 'Update Escalation Time Type', '/workflow/escalation-hours/_update/{id}', 'pgr', 'pgr', 'updateEscalationTimeType', false, 1, now(), 1, now());
 
INSERT INTO eg_roleaction(rolecode, actionid, tenantid) VALUES ('GA', (SELECT id FROM eg_action WHERE url = '/workflow/escalation-hours/_update/{id}'), 'default');
 
 
 
 
 

 
insert into eg_action(id,name,url,servicecode,parentmodule,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values
(nextval('SEQ_EG_ACTION'),'Create receivingcenter Master','/pgr-master/receivingcenter/_create','PGR','PGR','Create receivingcenter Master',true,1,now(),1,now());
 
INSERT INTO eg_roleaction(rolecode, actionid, tenantid) VALUES ('GA', (SELECT id FROM eg_action WHERE url = '/pgr-master/receivingcenter/_create'), 'default');
 
insert into eg_action(id,name,url,servicecode,parentmodule,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values
(nextval('SEQ_EG_ACTION'),'Update receivingcenter Master','/pgr-master/receivingcenter/{id}/_update','PGR','PGR','Update receivingcenter Master',true,1,now(),1,now());
 
INSERT INTO eg_roleaction(rolecode, actionid, tenantid) VALUES ('GA', (SELECT id FROM eg_action WHERE url = '/pgr-master/receivingcenter/{id}/_update'), 'default');
 
insert into eg_action(id,name,url,servicecode,parentmodule,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values
(nextval('SEQ_EG_ACTION'),'Create receivingmode Master','/pgr-master/receivingmode/_create','PGR','PGR','Create receivingmode Master',true,1,now(),1,now());
 
INSERT INTO eg_roleaction(rolecode, actionid, tenantid) VALUES ('GA', (SELECT id FROM eg_action WHERE url = '/pgr-master/receivingmode/_create'), 'default');
 
 
insert into eg_action(id,name,url,servicecode,parentmodule,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values
(nextval('SEQ_EG_ACTION'),'Update receivingmode Master','/pgr-master/receivingmode/{id}/_update','PGR','PGR','Update receivingmode Master',true,1,now(),1,now());
 
INSERT INTO eg_roleaction(rolecode, actionid, tenantid) VALUES ('GA', (SELECT id FROM eg_action WHERE url = '/pgr-master/receivingmode/{id}/_update'), 'default');
 
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values
(nextval('SEQ_EG_ACTION'),'Get all receivingcenters','/pgr-master/receivingcenter/_search','PGR','tenantId=&id=&name=&code=&active=','PGR','Get all receivingcenters',true,1,now(),1,now());
 
INSERT INTO eg_roleaction(rolecode, actionid, tenantid) VALUES ('GA', (SELECT id FROM eg_action WHERE url = '/pgr-master/receivingcenter/_search'), 'default');
 
 
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values
(nextval('SEQ_EG_ACTION'),'Get all receivingmodes','/pgr-master/receivingmode/_search','PGR','tenantId=&id=&name=&code=&active=&visible=','PGR','Get all receivingmodes',true,1,now(),1,now());
 
INSERT INTO eg_roleaction(rolecode, actionid, tenantid) VALUES ('GA', (SELECT id FROM eg_action WHERE url = '/pgr-master/receivingmode/_search'), 'default');
 
 

 
 
 insert into eg_action (ID,NAME,URL,servicecode,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),'CREATE COMPLAINT ROUTER',
'/workflow/router/_create','PGR',null,'PGR',null,'CREATE COMPLAINT ROUTER',true,1,now(),1,now());
insert into eg_roleaction (rolecode, actionid, tenantid) values('GA', (Select id from eg_action where url='/workflow/router/_create'), 'default');
 

 
insert into eg_action (ID,NAME,URL,servicecode,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),'UPDATE COMPLAINT ROUTER',
'/workflow/router/_update','PGR',null,'PGR',null,'UPDATE COMPLAINT ROUTER',true,1,now(),1,now());
insert into eg_roleaction (rolecode, actionid, tenantid) values('GA', (Select id from eg_action where url='/workflow/router/_update'), 'default');
 

 
insert into eg_action (ID,NAME,URL,servicecode,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),'SEARCH COMPLAINT ROUTER',
'/workflow/router/_search','PGR','tenantId=&id=&boundaryid=&serviceid=&designationid=','PGR',null,'SEARCH COMPLAINT ROUTER',true,1,now(),1,now());
insert into eg_roleaction (rolecode, actionid, tenantid) values('GA', (Select id from eg_action where url='/workflow/router/_search'), 'default');
 

 
