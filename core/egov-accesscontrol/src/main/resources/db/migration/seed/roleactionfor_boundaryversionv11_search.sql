insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Search MdmsBoundaries','/egov-location/location/v11/boundarys/_search','LOCATION_MS',null,(select id from service where code = 'LOCATION_MS' and tenantid = 'default'),0,'Search Boundaries',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Search MdmsBoundaries' ),'default');
