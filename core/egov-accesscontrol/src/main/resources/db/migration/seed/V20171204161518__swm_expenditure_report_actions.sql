insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Expenditure Incurred On Transportation Report','/swm-services/report/expenditureincurredreport','SWMRPT',null,(select id from service where code = 'SWM' and tenantid = 'default'),0,'Expenditure Incurred On Transportation Report',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Expenditure Incurred On Transportation Report'),'default');

