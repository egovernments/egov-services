
insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Create Escalation Hierarchy','/pgr-master/escalation-hierarchy/v1/_create','ESCL', NULL,(select id from service where code='ESCL' and tenantid='default'), 1,'Create Escalation Hierarchy',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Create Escalation Hierarchy'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Create Escalation Hierarchy'),'default');

