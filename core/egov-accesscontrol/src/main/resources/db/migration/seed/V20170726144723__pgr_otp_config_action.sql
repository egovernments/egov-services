insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId)
	values (nextval('SEQ_SERVICE'),'OTPCONFIG','OTPConfiguration',true, 'pgr' ,'OTP Configuration', 8 ,(select id from service where name ='Masters' and code='MSTR' and tenantId='default') ,'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Create OTP Config','/pgr-master/OTPConfig/_create','OTPCONFIG',NULL, (select id from service where name ='OTPConfiguration' and contextroot='pgr' and tenantid='default'),1,'Create OTP Configuration',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Update OTP Config','/pgr-master/OTPConfig/_update','OTPCONFIG',NULL, (select id from service where name ='OTPConfiguration' and contextroot='pgr' and tenantid='default'),3,'Update OTP Configuration',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Search OTP Config','/pgr-master/OTPConfig/_search','OTPCONFIG',NULL, (select id from service where name ='OTPConfiguration' and contextroot='pgr' and tenantid='default'),2,'Search OTP Configuration',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Create OTP Config'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Search OTP Config'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Update OTP Config'),'default');

