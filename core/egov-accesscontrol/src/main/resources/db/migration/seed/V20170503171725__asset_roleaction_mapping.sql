update eg_action set url='/app/asset/search-asset.html',queryparams='type=update' where name='ModifyAsset' and tenantId='default';

update eg_action set queryparams='type=view' where name='ViewAsset' and tenantId='default';


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'SearchUomService','/uoms/_search','EGOV Common Masters',null,(select id from service where name='EGOV Common Masters' and tenantId='default'),1,'Search UOM',false,1,now(),1,now(),'default');

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'SearchUomCategoriesService','/uomcategories/_search','EGOV Common Masters',null,(select id from service where name='EGOV Common Masters' and tenantId='default'),1,'Search Uom Categories',false,1,now(),1,now(),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchUomService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'SearchUomService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchUomCategoriesService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'SearchUomCategoriesService' and tenantId='default' ),'default');




