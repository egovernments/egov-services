insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) values (nextval('SEQ_SERVICE'),'OTP','Otp',false,'/otp','Otp',NULL,NULL,'default');


insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'CreateOtp','/otp/v1/_create','OTP', NULL,(select id from service where code='OTP' and tenantid='default'), 1,'Create Otp',false,1,now(),1,now());


insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'ValidateOtp','/otp/v1/_validate','OTP', NULL,(select id from service where code='OTP' and tenantid='default'), 1,'Validate Otp',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'SearchOtp','/otp/v1/_search','OTP', NULL,(select id from service where code='OTP' and tenantid='default'), 1,'Search Otp',false,1,now(),1,now());



insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='CreateOtp'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='ValidateOtp'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='SearchOtp'),'default');