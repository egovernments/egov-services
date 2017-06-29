insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Service Request Count','/pgr/seva/v1/_count','PGR','',(select code from service where name='PGR'and contextroot='pgr' and tenantid='ap.public'),null,'Service Request Count',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Service Request Count'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Service Request Count'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Service Request Count'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Service Request Count'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Service Request Count'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Service Request Count'),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Update Service Request Eligibility','/pgr/seva/v1/_get','PGR','',(select code from service where name='PGR'and contextroot='pgr'and tenantid='ap.public'),null,'Update Service Request Eligibility',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Update Service Request Eligibility'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Update Service Request Eligibility'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Update Service Request Eligibility'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Update Service Request Eligibility'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Update Service Request Eligibility'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Update Service Request Eligibility'),'default');
