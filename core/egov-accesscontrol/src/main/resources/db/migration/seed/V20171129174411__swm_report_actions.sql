insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) 
values (nextval('SEQ_SERVICE'), 'SWMRPT', 'SWM Reports', true, 'SWM Reports', 3, (select id from service where code = 'SWM' and tenantid = 'default'), 'default');

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Dumping Ground Detail Report','/swm-services/report/dumpinggrounddetailsreport','SWMRPT',null,(select id from service where code = 'SWM' and tenantid = 'default'),0,'Dumping Ground Detail Report',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Dumping Ground Detail Report' ),'default');
