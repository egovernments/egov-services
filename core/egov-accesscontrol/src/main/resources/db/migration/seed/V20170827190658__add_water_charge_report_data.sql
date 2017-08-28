insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'WCMS REPORTS','Wcms Reports',true,null,'Reports',3,
(select id from service where code='WCMS' and tenantId='default'),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'WaterCharges Metadata','/report/wcms/metadata/_get','WCMS REPORTS',NULL, (select id from service where code ='WCMS REPORTS' and tenantid='default'),1,'Water Charges Metadata Report',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'WaterCharges Get','/report/wcms/_get','WCMS REPORTS',NULL, (select id from service where code ='WCMS REPORTS' and tenantid='default'),2,'Water Charges Get Report',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Citizen Metadata',' /report/citizen/metadata/_get','WCMS REPORTS',NULL, (select id from service where code ='WCMS REPORTS' and tenantid='default'),3,'Citizen Metadata Report',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Citizen Get','/report/citizen/_get','WCMS REPORTS',NULL, (select id from service where code ='WCMS REPORTS'  and tenantid='default'),4,'Citizen Get Report',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='WaterCharges Metadata'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='WaterCharges Get'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Citizen Metadata'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Citizen Get'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('ULB Operator',(select id from eg_action where name='WaterCharges Metadata'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('ULB Operator',(select id from eg_action where name='WaterCharges Get'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('ULB Operator',(select id from eg_action where name='Citizen Metadata'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('ULB Operator',(select id from eg_action where name='Citizen Get'),'default');

