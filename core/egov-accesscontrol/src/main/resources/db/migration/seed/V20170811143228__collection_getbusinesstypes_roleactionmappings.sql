insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'GetBusinessTypes','/egov-common-masters/businessDetails/_getBusinessTypes','COLLECTION-MASTERS',null,1,'Business Types',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='GetBusinessTypes'),'default');