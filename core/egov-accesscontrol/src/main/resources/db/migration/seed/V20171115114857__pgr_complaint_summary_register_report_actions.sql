insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'Complaint Summary Register Report','/pgr-master/report/complaintSummaryRegisterReport','RPT',null,(select id from service where code='RPT' and tenantid ='default'),1,'Complaint Summary Register Report',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Complaint Summary Register Report'),'default');
