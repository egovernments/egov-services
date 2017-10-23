insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Search Compensatory Leaves','/hr-leave/compensatoryleaves/_search','EIS Masters','',(select id from service where name='HR Leave' and tenantid='default'),0,'Search Compensatory Leaves',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Search Compensatory Leaves'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'Search Compensatory Leaves'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE', (select id from eg_action where name = 'Search Compensatory Leaves'),'default');