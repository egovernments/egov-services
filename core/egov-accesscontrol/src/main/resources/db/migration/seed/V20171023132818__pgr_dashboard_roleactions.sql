insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) 
values (nextval('SEQ_SERVICE'),'DSHBRD','Dashboards',true,'pgr' ,'Dashboards',4 ,(select id from service where name = 'Grievance Redressal' and tenantid = 'default') ,'default');


insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'), 'Monthly Dashboard', '/pgr/dashboard', 'DSHBRD', null, 1, 'Monthly Dashboard', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantId) 
values ('GA', (select id from eg_action where name='Monthly Dashboard' and url='/pgr/dashboard' and displayname='Monthly Dashboard'), 'default');
