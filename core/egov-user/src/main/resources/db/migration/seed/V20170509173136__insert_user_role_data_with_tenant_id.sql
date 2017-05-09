insert into eg_role
(id, name, code, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version, tenantid) values
(nextval('SEQ_EG_ROLE'),'Citizen','CITIZEN','Citizen who can raise complaint',now(),1,1,now(),0,'default');
insert into eg_role
(id,name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,tenantid) values
(nextval('SEQ_EG_ROLE'),'Employee','EMPLOYEE','Default role for all employees',now(),1,1,now(),0,'default');
insert into eg_role
(id,name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,tenantid) values
(nextval('SEQ_EG_ROLE'),'Super User','SUPERUSER','System Administrator. Can change all master data and has access to all the system screens.',now(),1,1,now(),0,'default');
insert into eg_role
(id,name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,tenantid) values
(nextval('SEQ_EG_ROLE'),'Grievance Officer','GO','Heads the grivance cell. Also all complaints that cannot be routed based on the rules are routed to Grievance Officer.',now(),1,1,now(),0,'default');
insert into eg_role
(id,name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,tenantid) values
(nextval('SEQ_EG_ROLE'),'Redressal Officer','RO','Employees that address citizens grievances.',now(),1,1,now(),0,'default');
insert into eg_role
(id,name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,tenantid) values
(nextval('SEQ_EG_ROLE'),'Grivance Administrator','GA','System Administator for PGR. Can change PGR Master data only.',now(),1,1,now(),0,'default');
insert into eg_role (id,name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,tenantid) values
(nextval('SEQ_EG_ROLE'),'Grievance Routing Officer','GRO','Grievance Routing Officer',now(),1,1,now(),0,'default');
insert into eg_role
(id,name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,tenantid) values
(nextval('SEQ_EG_ROLE'),'Employee Admin','EMPLOYEE ADMIN','Employee Administrator',now(),1,1,now(),0, 'default');
insert into eg_role
(id,name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,tenantid) values
(nextval('SEQ_EG_ROLE'),'ULB Operator','ULB Operator','City Official',now(),1,1,now(),0, 'default');
insert into eg_role
(id,name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,tenantid) values
(nextval('SEQ_EG_ROLE'),'Property Verifier','Property Verifier','One who do field survey and verified the data entered by ULB Operator',now(),1,1,now(),0, 'default');
insert into eg_role
(id,name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,tenantid) values
(nextval('SEQ_EG_ROLE'),'Property Approver','Property Approver','One who approves the record finally',now(),1,1,now(),0, 'default');
insert into eg_role
(id,name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version,tenantid) values
(nextval('SEQ_EG_ROLE'),'Asset Administrator','Asset Administrator','One who manages the Asset Master data',now(),1,1,now(),0, 'default');
INSERT INTO EG_ROLE
(ID,NAME,DESCRIPTION,CREATEDDATE,CREATEDBY,LASTMODIFIEDBY,LASTMODIFIEDDATE,VERSION,CODE,TENANTID) values
(nextval('seq_eg_role'),'Asset Creator','Creator of Assets',now(),1,1,now(),0,'AssetCreator', 'default');


insert into eg_userrole (roleid, userid, roleidtenantid, useridtenantid)
values( (select id from eg_role where name='Employee' and tenantid='default'),
(select id from eg_user where username='narasappa' and tenantid='default'),
'default',
'default');
insert into eg_userrole (roleid, userid, roleidtenantid, useridtenantid)
values( (select id from eg_role where name='Employee' and tenantid='default'),
(select id from eg_user where username='manas' and tenantid='default'),
'default',
'default');
insert into eg_userrole (roleid, userid, roleidtenantid, useridtenantid)
values((select id from eg_role where name='Employee' and tenantid='default'),
(select id from eg_user where username='ramana' and tenantid='default'),
'default',
'default');
insert into eg_userrole (roleid, userid, roleidtenantid, useridtenantid)
values((select id from eg_role where name='Citizen' and tenantid='default'),
(select id from eg_user where username='9999999999' and tenantid='default'),
'default',
'default');
insert into eg_userrole (roleid, userid, roleidtenantid, useridtenantid) values
((select id from eg_role where name='Employee' and tenantid='default'),
(select id from eg_user where username='ravi' and tenantid='default'),
'default',
'default');
insert into eg_userrole (roleid, userid, roleidtenantid, useridtenantid) values(
(select id from eg_role where name='Grievance Routing Officer' and tenantid='default'),
(select id from eg_user where username='ravi' and tenantid='default'),
'default',
'default');
