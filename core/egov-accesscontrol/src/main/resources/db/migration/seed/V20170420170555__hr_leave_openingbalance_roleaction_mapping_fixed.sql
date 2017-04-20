INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),'Leave Opening Balance', 'Leave Opening Balance', true, 'eis', 'PIS', 13, (select id from service where name='EIS'), 'default');



insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'CreateLeave Opening Balance','/hr-web/app/hr/leavemaster/pis.html','EIS','tenantId=',(select code from service where name='Leave Opening Balance' and contextroot='eis'),1,'Create',true,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'CreateLeave Opening Balances','/hr-leave/leaveopeningbalances/_create','EIS','tenantId=',(select code from service where name='Leave Opening Balance' and contextroot='eis'),2,'CreateLeave Opening Balances',false,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'UpdateLeave Opening Balance','/hr-web/app/hr/leavemaster/pis.html','EIS','type=update&tenantId=',(select code from service where name='Leave Opening Balance' and contextroot='eis'),3,'Update',true,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'Leave Opening BalanceUpdate','/hr-leave/leaveopeningbalances/_update','EIS','tenantId=',(select code from service where name='Leave Opening Balance' and contextroot='eis'),4,'Leave Opening BalanceUpdate',false,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ViewLeave Opening Balance','/hr-web/app/hr/leavemaster/pis.html','EIS','type=view&tenantId=',(select code from service where name='Leave Opening Balance' and contextroot='eis'),5,'View',true,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'SearchLeave Opening Balance','/hr-leave/leaveopeningbalances/_search','EIS','tenantId=',(select code from service where name='Leave Opening Balance' and contextroot='eis'),6,'Search Leave Opening Balance',false,1,now(),1,now(),'default');


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreateLeave Opening Balance'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreateLeave Opening Balances'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'UpdateLeave Opening Balance'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Leave Opening BalanceUpdate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewLeave Opening Balance'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchLeave Opening Balance'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'CreateLeave Opening Balance'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'CreateLeave Opening Balances'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'UpdateLeave Opening Balance'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'Leave Opening BalanceUpdate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'ViewLeave Opening Balance'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'SearchLeave Opening Balance'),'default');