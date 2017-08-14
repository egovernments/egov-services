
 
 insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId)
 values (nextval('SEQ_SERVICE'),'SDS','SDS',true, 'pgr' ,'Service Definition Search', 10 ,(select id from service where name ='Masters' and code='MSTR' and tenantId='default') ,'default');
 
 insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
 values(nextval('SEQ_EG_ACTION'),'Service Definition Search','/pgr-master/servicedefinition/v1/_search','SDS',NULL, (select id from service where name ='SDS' and contextroot='pgr' and tenantid='default'),1,'Service Definition Search',false,1,now(),1,now());
 
 insert into eg_roleaction(roleCode,actionid,tenantId)values('SRA',(select id from eg_action where name='Service Definition Search'),'default');
