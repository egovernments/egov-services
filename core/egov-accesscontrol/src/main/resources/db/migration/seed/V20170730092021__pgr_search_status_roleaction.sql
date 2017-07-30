insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PGR get Status','/workflow/v1/statuses/_search','PGR',NULL, (select id from service where name ='PGR' and contextroot='pgr' and tenantid='default'),1,'PGR get status',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='PGR get Status'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='PGR get Status'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='PGR get Status'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='PGR get Status'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PGR get Status'),'default');

