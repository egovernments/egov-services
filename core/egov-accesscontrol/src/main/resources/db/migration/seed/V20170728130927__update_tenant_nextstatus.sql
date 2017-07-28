UPDATE eg_action SET displayname = 'Ageing By Boundary Report' WHERE servicecode='RPT' and name='Ageing Report' and url='/pgr-master/report/ageingReport';
UPDATE eg_action SET displayname = 'Drill Down By Boundary Report' WHERE servicecode='RPT' and name='Drill Down Report' and url='/pgr-master/report/drillDownReport';

insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Ageing By Department Report','/pgr-master/report/ageingByDeptReport','RPT',null,(select id from service where code='RPT' and tenantid ='default'),6,'Ageing By Department Report',true,1,now(),1,now());
insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Drill Down By Department Report','/pgr-master/report/drillDownByDeptReport','RPT',null,(select id from service where code='RPT' and tenantid ='default'),7,'Drill Down By Department Report',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Ageing By Department Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Drill Down By Department Report'),'default');

update eg_roleaction set tenantid = 'default' where actionid = (select id from eg_action where url = '/pgr/nextstatuses/v1/_search' and name = 'Get next statuses by CurrentStatus and Role') and tenantid  = 'ap.public';