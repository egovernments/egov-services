insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'WorkflowForWCMS','/wc/application/update/{stateId}','Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),1,'WorkFlow WCMS',false,1,now(),1,now());


---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'WorkflowForWCMS'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE', (select id from eg_action where name = 'WorkflowForWCMS'),'default');
