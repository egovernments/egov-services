insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'Upload Leave Opening Balance','/app/hr/leavemaster/upload-leave-opening-balance.html','EIS','tenantId=',(select code from service where name='Leave Opening Balance' and contextroot='eis'),1,'Upload LeaveBalance',true,1,now(),1,now(),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Upload Leave Opening Balance'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'Upload Leave Opening Balance'),'default');
