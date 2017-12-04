insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Create Material Issue From Indent','/inventory-services/materialissues/_preparemifromindents','MATERIAL ISSUE',null,4,
'Create Material Issue From Indent',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Create Material Issue From Indent' ),'default');