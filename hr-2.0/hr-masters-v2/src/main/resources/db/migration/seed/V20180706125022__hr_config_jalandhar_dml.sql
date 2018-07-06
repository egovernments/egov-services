delete from egeis_hrConfigurationValues where tenantid='pb.jalandhar';
delete from egeis_hrConfiguration where tenantid='pb.jalandhar';
delete from egeis_hrstatus where tenantid='pb.jalandhar';

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'EMPLOYED','Currently employee of the system','pb.jalandhar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'RETIRED','Employee who is currently retired','pb.jalandhar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'DECEASED','Employee who is deceased when in service','pb.jalandhar');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Weekly_holidays','Define the weekly off for the organization',1,now(),1,now(),'pb.jalandhar');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Autogenerate_employeecode','This will define if employee code needs to be system generated or manually captured',1,now(),1,now(),'pb.jalandhar');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Include_enclosed_holidays','This will define if system needs to consider the holidays coming between as leave or not',1,now(),1,now(),'pb.jalandhar');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Compensatory leave validity','This will define the number of days till which an employee can apply for compensatory off',1,now(),1,now(),'pb.jalandhar');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Weekly_holidays' and tenantid='pb.jalandhar'),'5-day week',
'2016-01-01',1,now(),1,now(),'pb.jalandhar');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Autogenerate_employeecode' and tenantid='pb.jalandhar'),'N',
'2016-01-01',1,now(),1,now(),'pb.jalandhar');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Include_enclosed_holidays' and tenantid='pb.jalandhar'),'N',
'2016-01-01',1,now(),1,now(),'pb.jalandhar');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Compensatory leave validity' and tenantid='pb.jalandhar'),'90',
'2016-01-01',1,now(),1,now(),'pb.jalandhar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'SUSPENDED','Employee who is suspended','pb.jalandhar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'TRANSFERRED','Employee who is transferred','pb.jalandhar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPLIED','Leave Status when applied','pb.jalandhar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPROVED','Leave Status when approved','pb.jalandhar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','REJECTED','Leave Status when rejected','pb.jalandhar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','CANCELLED','Leave Status when cancelled','pb.jalandhar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','RESUBMITTED','Leave Status when resubmitted','pb.jalandhar');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Cutoff_Date','Define the cut off date after which the application will go live',1,now(),1,now(),'pb.jalandhar');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Cutoff_Date' and tenantid='pb.jalandhar'),'2017-05-31',
'2016-01-01',1,now(),1,now(),'pb.jalandhar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPLIED','Employee Movement when applied','pb.jalandhar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPROVED','Employee Movement when approved','pb.jalandhar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','REJECTED','Employee Movement when rejected','pb.jalandhar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','CANCELLED','Employee Movement when cancelled','pb.jalandhar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','RESUBMITTED','Employee Movement when resubmitted','pb.jalandhar');

SELECT setval('seq_egeis_designation', nextval('seq_egeis_designation'), true);


INSERT INTO egeis_hrConfiguration (id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES (nextval('seq_egeis_hrConfiguration'), 'Autogenerate_username', 'This will define if username for employee needs to be system generated or manually captured', 1, now(), 1, now(), 'pb.jalandhar');

INSERT INTO egeis_hrConfigurationValues (id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES(nextval('seq_egeis_hrConfigurationValues'), (SELECT id FROM egeis_hrconfiguration WHERE keyname='Autogenerate_username' AND tenantid='pb.jalandhar'), 'Y', '2016-01-01', 1, now(), 1, now(), 'pb.jalandhar');

update egeis_hrconfiguration set keyname='Compensatory_leave_validity' where keyname='Compensatory leave validity';

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','FORWARDED','Leave Status when forwarded','pb.jalandhar');