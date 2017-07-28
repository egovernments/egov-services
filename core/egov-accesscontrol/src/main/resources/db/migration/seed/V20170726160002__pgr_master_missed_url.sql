insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Search Escalation Time','/workflow/escalation-hours/v1/_search','ESCLT',NULL, (select id from service where code ='ESCLT' and contextroot='pgr' and tenantid='default'),2,'Search Escalation Time',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Search Escalation Time'),'default');

UPDATE eg_action SET   servicecode = 'ESCLT' , parentmodule = (select id from service where code='ESCLT' and tenantid ='default'),displayname ='Create/Update Escalation Time', enabled=true , ordernumber =1 WHERE servicecode='pgr' and url='/workflow/escalation-hours/v1/_create';
UPDATE eg_action SET   servicecode = 'ESCLT' , parentmodule = (select id from service where code='ESCLT' and tenantid ='default'),displayname ='Update Escalation Time', ordernumber =3 WHERE servicecode='pgr' and url='/workflow/escalation-hours/v1/_update';

------------------------------------------------------------------------------------------------------------------------------------------------------------------------
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Create Escalation','/pgr-master/escalation/_create','ESCL',NULL, (select id from service where name ='Escalation' and contextroot='pgr' and tenantid='default'),1,'Create Escalation',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Update Escalation','/pgr-master/escalation/_update','ESCL',NULL, (select id from service where name ='Escalation' and contextroot='pgr' and tenantid='default'),3,'Update Escalation',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Search Escalation','/pgr-master/escalation/_search','ESCL',NULL, (select id from service where name ='Escalation' and contextroot='pgr' and tenantid='default'),2,'Search Escalation',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Create Escalation'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Search Escalation'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Update Escalation'),'default');

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
UPDATE eg_action SET displayname = 'View Receiving Center' WHERE servicecode='RCVC' and url='/pgr-master/receivingcenter/v1/_search';

UPDATE eg_action SET displayname = 'Update Receiving Center' WHERE servicecode='RCVC' and url='/pgr-master/receivingcenter/v1/{id}/_update';


insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Ageing Report','/pgr-master/report/ageingReport','RPT',null,(select id from service where code='RPT' and tenantid ='default'),1,'Ageing Report',true,1,now(),1,now());

insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Drill Down Report','/pgr-master/report/drillDownReport','RPT',null,(select id from service where code='RPT' and tenantid ='default'),2,'Drill Down Report',true,1,now(),1,now());

insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Grievance Type Wise Report','/pgr-master/report/grievanceTypeWiseReport','RPT',null,(select id from service where code='RPT' and tenantid ='default'),3,'Grievance Type Wise Report',true,1,now(),1,now());

insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Functionary Wise Report','/pgr-master/report/functionaryWiseReport','RPT',null,(select id from service where code='RPT' and tenantid ='default'),4,'Functionary Wise Report',true,1,now(),1,now());


insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Router Escalation Report','/pgr-master/report/routerEscalationReport','RPT',null,(select id from service where code='RPT' and tenantid ='default'),5,'Router Escalation Report',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Ageing Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Drill Down Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Grievance Type Wise Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Functionary Wise Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Router Escalation Report'),'default');
