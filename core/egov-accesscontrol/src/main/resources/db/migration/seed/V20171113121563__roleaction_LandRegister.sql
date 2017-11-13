insert into eg_action (id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values (nextval('SEQ_EG_ACTION'),'Land Register','/report/asset/metadata/_get','AssetReports',null,(select id from service where name='Asset Register Report' and  tenantId='default'),4,'Land Register',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Land Register' and displayname='Land Register' AND url='/report/asset/metadata/_get'),'default');

