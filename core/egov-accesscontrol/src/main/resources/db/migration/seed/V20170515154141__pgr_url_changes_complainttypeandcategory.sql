insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'Get all CompaintTypeCategory','/pgr/complaintTypeCategories/_search','PGR','tenantId=',(select code from service where name ='PGR' and contextroot='pgr'),null,'Get all CompaintTypeCategory',false,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'Get ComplaintType by type,count and tenantId','/pgr/services/_search','PGR','type=&count=&tenantId=',(select code from service where name ='PGR' and contextroot='pgr'),null,'Get ComplaintType by type,count and tenantId',false,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'Get ComplaintType by type,categoryId and tenantId','/pgr/services/_search','PGR','type=&categoryId=&tenantId=',(select code from service where name ='PGR' and contextroot='pgr'),null,'Get ComplaintType by type,categoryId and tenantId',false,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'Get ComplaintType by type and tenantId','/pgr/services/_search','PGR','type=&tenantId=',(select code from service where name ='PGR' and contextroot='pgr'),null,'Get ComplaintType by type and tenantId',false,1,now(),1,now(),'default');
