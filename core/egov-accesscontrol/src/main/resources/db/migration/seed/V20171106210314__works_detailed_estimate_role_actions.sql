insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'DetailedEstimate', 'Detailed Estimate', false, 'Detailed Estimate', 1, (select id from service where code = 'WMS' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Detailed Estimate Create','/works-estimate/detailedestimates/_create','DetailedEstimate',null,null,0,'Detailed Estimate Create',false,1,now(),1,now());
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Detailed Estimate Update','/works-estimate/detailedestimates/_update','DetailedEstimate',null,null,0,'Detailed Estimate Update',false,1,now(),1,now());
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Detailed Estimate Search','/works-estimate/detailedestimates/_search','DetailedEstimate',null,null,0,'Detailed Estimate Search',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Detailed Estimate Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Detailed Estimate Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Detailed Estimate Search' ),'default');
