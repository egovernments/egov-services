--pgr url changes service type category

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'Get all service type categories','/pgr/servicecategories/_search','PGR','tenantId=',(select code from service where name ='PGR' and contextroot='pgr'),null,'Get all service type categories',false,1,now(),1,now(),'default');


-- role action service type and category

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get all service type categories'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Get all service type categories'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Get all service type categories'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Get all service type categories'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Get all service type categories'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Get all service type categories'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Get all service type categories'),'default');

