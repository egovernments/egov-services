insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'DesignationUpdate','/designations/_update','EIS','',(select id from service where name='Designation' and contextroot='eis'),1,'DesignationUpdate',false,1,now(),1,now(),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'DesignationUpdate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'DesignationUpdate'),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'PositionCreate','/positions/_create','EIS','',(select id from service where name='Position' and contextroot='eis'),1,'PositionCreate',false,1,now(),1,now(),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'PositionCreate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'PositionCreate'),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'PositionUpdate','/positions/_update','EIS','',(select id from service where name='Position' and contextroot='eis'),1,'PositionUpdate',false,1,now(),1,now(),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'PositionUpdate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'PositionUpdate'),'default');
