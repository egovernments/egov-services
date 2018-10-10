insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'EMPLOYED','Currently employee of the system','pb.mohali');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'RETIRED','Employee who is currently retired','pb.mohali');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'DECEASED','Employee who is deceased when in service','pb.mohali');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'SUSPENDED','Employee who is suspended','pb.mohali');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'TRANSFERRED','Employee who is transferred','pb.mohali');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Autogenerate_employeecode','This will define if employee code needs to be system generated or manually captured',1,now(),1,now(),'pb.mohali');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Autogenerate_employeecode' and tenantid='pb.mohali'),'N',
'2016-01-01',1,now(),1,now(),'pb.mohali');

INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Temporary', NULL, 'pb.mohali');
INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Deputation', NULL, 'pb.mohali');
INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Outsourced', NULL, 'pb.mohali');
INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Permanent', NULL, 'pb.mohali');

insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'APPSC','Andhra Public Service','pb.mohali');
insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'Compassionate','Compassionate','pb.mohali');

insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'Others','Others','pb.mohali');

delete from egeis_group where name in ('State','Central','Local') and tenantid = 'pb.mohali' ;
delete from egeis_recruitmenttype where name in ('Direct','Transfer','Compensatory') and tenantid='pb.mohali' ;
delete from egeis_recruitmentMode where name in ('UPSC','Direct','Department Exams') and tenantid = 'pb.mohali' ;
delete from egeis_recruitmentQuota where name in ('Sports','Ex-Serviceman','General') and tenantid = 'pb.mohali' ;
delete from egeis_grade where name in ('A','B','G') and tenantid = 'pb.mohali' ;

insert into egeis_group(id,name,description,tenantid) values (nextval('seq_egeis_group'),'State','State','pb.mohali');
insert into egeis_group(id,name,description,tenantid) values (nextval('seq_egeis_group'),'Central','Central','pb.mohali');
insert into egeis_group(id,name,description,tenantid) values (nextval('seq_egeis_group'),'Local','Local','pb.mohali');


insert into egeis_recruitmenttype(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentType'),'Direct','Direct','pb.mohali');
insert into egeis_recruitmenttype(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentType'),'Transfer','Transfer','pb.mohali');
insert into egeis_recruitmenttype(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentType'),'Compensatory','Compensatory','pb.mohali');


insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'UPSC','Public Service','pb.mohali');
insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'Direct','Direct','pb.mohali');
insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'Department Exams','Department Exams','pb.mohali');


insert into egeis_recruitmentQuota(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentQuota'),'Sports','Sports Quota','pb.mohali');
insert into egeis_recruitmentQuota(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentQuota'),'Ex-Serviceman','Ex-Serviceman Quota','pb.mohali');
insert into egeis_recruitmentQuota(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentQuota'),'General','General Quota','pb.mohali');

insert into egeis_grade values (nextval('seq_egeis_grade'),'A','First Grade',1,true,'pb.mohali');
insert into egeis_grade values (nextval('seq_egeis_grade'),'B','Second Level',2,true,'pb.mohali');
insert into egeis_grade values (nextval('seq_egeis_grade'),'G','General Level',3,true,'pb.mohali');


INSERT INTO egeis_hrConfiguration (id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES (nextval('seq_egeis_hrConfiguration'), 'Autogenerate_username', 'This will define if username for employee needs to be system generated or manually captured', 1, now(), 1, now(), 'pb.mohali');

INSERT INTO egeis_hrConfigurationValues (id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES(nextval('seq_egeis_hrConfigurationValues'), (SELECT id FROM egeis_hrconfiguration WHERE keyname='Autogenerate_username' AND tenantid='pb.mohali'), 'Y', '2016-01-01', 1, now(), 1, now(), 'pb.mohali');

