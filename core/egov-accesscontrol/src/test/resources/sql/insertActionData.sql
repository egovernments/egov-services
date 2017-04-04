insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId)values
(nextval('SEQ_SERVICE'),'PGR','PGR',true,'pgr','Grievance Redressal',3,'','ap.kurnool');
insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId)values
(nextval('SEQ_SERVICE'),'PgrComp','PGRComplaints',true,'','Grievance',1,(select id from service where name='PGR'and contextroot='pgr'),'ap.kurnool');




insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(1,'Get all ReceivingMode','/pgr/receivingmode','PGR','tenantId=',1,null,'Get all ReceivingMode',false,1,now(),1,now(),'ap.kurnool');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(2,'Get all CompaintTypeCategory','/pgr/complaintTypeCategories','PGR','tenantId=',2,null,'Get all CompaintTypeCategory',false,1,now(),1,now(),'ap.kurnool');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(3,'Get ComplaintType by type,count and tenantId','/pgr/services','PGR','type=&count=&tenantId=',3,null,'Get ComplaintType by type,count and tenantId',false,1,now(),1,now(),'ap.kurnool');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(4,'Get all ReceivingCenters','/pgr/receivingcenter','PGR','tenantId=',3,null,'Get all ReceivingCenters',false,1,now(),1,now(),'ap.kurnool');

insert into eg_roleaction(roleid,actionid,tenantId)values(4,1,'ap.public');
insert into eg_roleaction(roleid,actionid,tenantId)values(4,2,'ap.public');
insert into eg_roleaction(roleid,actionid,tenantId)values(4,3,'ap.public');
insert into eg_roleaction(roleid,actionid,tenantId)values(4,4,'ap.public');
insert into eg_roleaction(roleid,actionid,tenantId)values(50,1,'ap.public');
insert into eg_roleaction(roleid,actionid,tenantId)values(50,2,'ap.public');
insert into eg_roleaction(roleid,actionid,tenantId)values(50,3,'ap.public');
insert into eg_roleaction(roleid,actionid,tenantId)values(50,4,'ap.public');
