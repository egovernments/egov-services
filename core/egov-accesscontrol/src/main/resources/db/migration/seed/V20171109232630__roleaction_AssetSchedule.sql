 --Asset Schedule


insert into eg_action (id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values (nextval('SEQ_EG_ACTION'),'AssetSchedule','/report/asset/metadata/_get','AssetSchedule',null,(select id from service where name='Asset Register Report' and  tenantId='default'),3,'Asset Schedule',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'AssetSchedule' and displayname='Asset Schedule' AND url='/report/asset/metadata/_get'),'default');

