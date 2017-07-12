insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Create Draft','/pgr/draft/v1/_create','PGR',NULL, (select code from service where name ='PGR' and contextroot='pgr' and tenantid='ap.public'),null,'Create Draft',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Search Draft','/pgr/draft/v1/_search','PGR',NULL, (select code from service where name ='PGR' and contextroot='pgr' and tenantid='ap.public'),null,'Search Draft',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Update Draft','/pgr/draft/v1/_update','PGR',NULL, (select code from service where name ='PGR' and contextroot='pgr' and tenantid='ap.public'),null,'Update Draft',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Delete Draft','/pgr/draft/v1/_delete','PGR',NULL, (select code from service where name ='PGR' and contextroot='pgr' and tenantid='ap.public'),null,'Delete Draft',false,1,now(),1,now());

