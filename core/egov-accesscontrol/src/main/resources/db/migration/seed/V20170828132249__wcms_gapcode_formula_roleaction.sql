insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchGapcodeFormulaMaster','/wcms/masters/gapcode/formula/_search','GAPCODEMASTER',null,4,'View Gapcode Formulas',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchGapcodeLastmonthMaster','/wcms/masters/gapcode/lastmonths/_search','GAPCODEMASTER',null,5,'View Gapcode LastMonths',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchGapcodeLogicMaster','/wcms/masters/gapcode/logic/_search','GAPCODEMASTER',null,6,'View Gapcode Logic',true,1,now(),1,now());



insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='SearchGapcodeFormulaMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='SearchGapcodeLastmonthMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='SearchGapcodeLogicMaster'),'default');