delete from egeis_hrConfigurationValues where tenantid='pb';
delete from egeis_hrConfiguration where tenantid='pb';
delete from egeis_hrstatus where tenantid='pb';

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'EMPLOYED','Currently employee of the system','pb');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'RETIRED','Employee who is currently retired','pb');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'DECEASED','Employee who is deceased when in service','pb');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Weekly_holidays','Define the weekly off for the organization',1,now(),1,now(),'pb');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Autogenerate_employeecode','This will define if employee code needs to be system generated or manually captured',1,now(),1,now(),'pb');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Include_enclosed_holidays','This will define if system needs to consider the holidays coming between as leave or not',1,now(),1,now(),'pb');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Compensatory leave validity','This will define the number of days till which an employee can apply for compensatory off',1,now(),1,now(),'pb');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Weekly_holidays' and tenantid='pb'),'5-day week',
'2016-01-01',1,now(),1,now(),'pb');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Autogenerate_employeecode' and tenantid='pb'),'N',
'2016-01-01',1,now(),1,now(),'pb');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Include_enclosed_holidays' and tenantid='pb'),'N',
'2016-01-01',1,now(),1,now(),'pb');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Compensatory leave validity' and tenantid='pb'),'90',
'2016-01-01',1,now(),1,now(),'pb');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'SUSPENDED','Employee who is suspended','pb');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'TRANSFERRED','Employee who is transferred','pb');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPLIED','Leave Status when applied','pb');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPROVED','Leave Status when approved','pb');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','REJECTED','Leave Status when rejected','pb');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','CANCELLED','Leave Status when cancelled','pb');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','RESUBMITTED','Leave Status when resubmitted','pb');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Cutoff_Date','Define the cut off date after which the application will go live',1,now(),1,now(),'pb');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Cutoff_Date' and tenantid='pb'),'2017-05-31',
'2016-01-01',1,now(),1,now(),'pb');


insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPLIED','Employee Movement when applied','pb');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPROVED','Employee Movement when approved','pb');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','REJECTED','Employee Movement when rejected','pb');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','CANCELLED','Employee Movement when cancelled','pb');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','RESUBMITTED','Employee Movement when resubmitted','pb');

SELECT setval('seq_egeis_designation', nextval('seq_egeis_designation'), true);


INSERT INTO egeis_hrConfiguration (id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES (nextval('seq_egeis_hrConfiguration'), 'Autogenerate_username', 'This will define if username for employee needs to be system generated or manually captured', 1, now(), 1, now(), 'pb');

INSERT INTO egeis_hrConfigurationValues (id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES(nextval('seq_egeis_hrConfigurationValues'), (SELECT id FROM egeis_hrconfiguration WHERE keyname='Autogenerate_username' AND tenantid='pb'), 'Y', '2016-01-01', 1, now(), 1, now(), 'pb');

update egeis_hrconfiguration set keyname='Compensatory_leave_validity' where keyname='Compensatory leave validity';

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','FORWARDED','Leave Status when forwarded','pb');

-- pb.amritsar

delete from egeis_hrConfigurationValues where tenantid='pb.amritsar';
delete from egeis_hrConfiguration where tenantid='pb.amritsar';
delete from egeis_hrstatus where tenantid='pb.amritsar';

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'EMPLOYED','Currently employee of the system','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'RETIRED','Employee who is currently retired','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'DECEASED','Employee who is deceased when in service','pb.amritsar');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Weekly_holidays','Define the weekly off for the organization',1,now(),1,now(),'pb.amritsar');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Autogenerate_employeecode','This will define if employee code needs to be system generated or manually captured',1,now(),1,now(),'pb.amritsar');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Include_enclosed_holidays','This will define if system needs to consider the holidays coming between as leave or not',1,now(),1,now(),'pb.amritsar');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Compensatory leave validity','This will define the number of days till which an employee can apply for compensatory off',1,now(),1,now(),'pb.amritsar');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Weekly_holidays' and tenantid='pb.amritsar'),'5-day week',
'2016-01-01',1,now(),1,now(),'pb.amritsar');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Autogenerate_employeecode' and tenantid='pb.amritsar'),'N',
'2016-01-01',1,now(),1,now(),'pb.amritsar');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Include_enclosed_holidays' and tenantid='pb.amritsar'),'N',
'2016-01-01',1,now(),1,now(),'pb.amritsar');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Compensatory leave validity' and tenantid='pb.amritsar'),'90',
'2016-01-01',1,now(),1,now(),'pb.amritsar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'SUSPENDED','Employee who is suspended','pb.amritsar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'TRANSFERRED','Employee who is transferred','pb.amritsar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPLIED','Leave Status when applied','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPROVED','Leave Status when approved','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','REJECTED','Leave Status when rejected','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','CANCELLED','Leave Status when cancelled','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','RESUBMITTED','Leave Status when resubmitted','pb.amritsar');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Cutoff_Date','Define the cut off date after which the application will go live',1,now(),1,now(),'pb.amritsar');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Cutoff_Date' and tenantid='pb.amritsar'),'2017-05-31',
'2016-01-01',1,now(),1,now(),'pb.amritsar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPLIED','Employee Movement when applied','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPROVED','Employee Movement when approved','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','REJECTED','Employee Movement when rejected','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','CANCELLED','Employee Movement when cancelled','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','RESUBMITTED','Employee Movement when resubmitted','pb.amritsar');

SELECT setval('seq_egeis_designation', nextval('seq_egeis_designation'), true);


INSERT INTO egeis_hrConfiguration (id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES (nextval('seq_egeis_hrConfiguration'), 'Autogenerate_username', 'This will define if username for employee needs to be system generated or manually captured', 1, now(), 1, now(), 'pb.amritsar');

INSERT INTO egeis_hrConfigurationValues (id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES(nextval('seq_egeis_hrConfigurationValues'), (SELECT id FROM egeis_hrconfiguration WHERE keyname='Autogenerate_username' AND tenantid='pb.amritsar'), 'Y', '2016-01-01', 1, now(), 1, now(), 'pb.amritsar');

update egeis_hrconfiguration set keyname='Compensatory_leave_validity' where keyname='Compensatory leave validity';

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','FORWARDED','Leave Status when forwarded','pb.amritsar');

--- pb.patiala

delete from egeis_hrConfigurationValues where tenantid='pb.patiala';
delete from egeis_hrConfiguration where tenantid='pb.patiala';
delete from egeis_hrstatus where tenantid='pb.patiala';

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'EMPLOYED','Currently employee of the system','pb.patiala');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'RETIRED','Employee who is currently retired','pb.patiala');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'DECEASED','Employee who is deceased when in service','pb.patiala');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Weekly_holidays','Define the weekly off for the organization',1,now(),1,now(),'pb.patiala');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Autogenerate_employeecode','This will define if employee code needs to be system generated or manually captured',1,now(),1,now(),'pb.patiala');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Include_enclosed_holidays','This will define if system needs to consider the holidays coming between as leave or not',1,now(),1,now(),'pb.patiala');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Compensatory leave validity','This will define the number of days till which an employee can apply for compensatory off',1,now(),1,now(),'pb.patiala');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Weekly_holidays' and tenantid='pb.patiala'),'5-day week',
'2016-01-01',1,now(),1,now(),'pb.patiala');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Autogenerate_employeecode' and tenantid='pb.patiala'),'N',
'2016-01-01',1,now(),1,now(),'pb.patiala');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Include_enclosed_holidays' and tenantid='pb.patiala'),'N',
'2016-01-01',1,now(),1,now(),'pb.patiala');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Compensatory leave validity' and tenantid='pb.patiala'),'90',
'2016-01-01',1,now(),1,now(),'pb.patiala');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'SUSPENDED','Employee who is suspended','pb.patiala');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'TRANSFERRED','Employee who is transferred','pb.patiala');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPLIED','Leave Status when applied','pb.patiala');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPROVED','Leave Status when approved','pb.patiala');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','REJECTED','Leave Status when rejected','pb.patiala');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','CANCELLED','Leave Status when cancelled','pb.patiala');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','RESUBMITTED','Leave Status when resubmitted','pb.patiala');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Cutoff_Date','Define the cut off date after which the application will go live',1,now(),1,now(),'pb.patiala');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Cutoff_Date' and tenantid='pb.patiala'),'2017-05-31',
'2016-01-01',1,now(),1,now(),'pb.patiala');


insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPLIED','Employee Movement when applied','pb.patiala');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPROVED','Employee Movement when approved','pb.patiala');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','REJECTED','Employee Movement when rejected','pb.patiala');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','CANCELLED','Employee Movement when cancelled','pb.patiala');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','RESUBMITTED','Employee Movement when resubmitted','pb.patiala');

SELECT setval('seq_egeis_designation', nextval('seq_egeis_designation'), true);


INSERT INTO egeis_hrConfiguration (id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES (nextval('seq_egeis_hrConfiguration'), 'Autogenerate_username', 'This will define if username for employee needs to be system generated or manually captured', 1, now(), 1, now(), 'pb.patiala');

INSERT INTO egeis_hrConfigurationValues (id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES(nextval('seq_egeis_hrConfigurationValues'), (SELECT id FROM egeis_hrconfiguration WHERE keyname='Autogenerate_username' AND tenantid='pb.patiala'), 'Y', '2016-01-01', 1, now(), 1, now(), 'pb.patiala');

update egeis_hrconfiguration set keyname='Compensatory_leave_validity' where keyname='Compensatory leave validity';

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','FORWARDED','Leave Status when forwarded','pb.patiala');


-- for default


delete from egeis_hrConfigurationValues where tenantId='default';
delete from egeis_hrConfiguration where tenantId='default';
delete from egeis_hrstatus where tenantId='default';

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'EMPLOYED','Currently employee of the system','default');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'RETIRED','Employee who is currently retired','default');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'DECEASED','Employee who is deceased when in service','default');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Weekly_holidays','Define the weekly off for the organization',1,now(),1,now(),'default');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Autogenerate_employeecode','This will define if employee code needs to be system generated or manually captured',1,now(),1,now(),'default');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Include_enclosed_holidays','This will define if system needs to consider the holidays coming between as leave or not',1,now(),1,now(),'default');
insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Compensatory leave validity','This will define the number of days till which an employee can apply for compensatory off',1,now(),1,now(),'default');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Weekly_holidays' and tenantid='default'),'5-day week',
'2016-01-01',1,now(),1,now(),'default');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Autogenerate_employeecode' and tenantid='default'),'N',
'2016-01-01',1,now(),1,now(),'default');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Include_enclosed_holidays' and tenantid='default'),'N',
'2016-01-01',1,now(),1,now(),'default');
insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Compensatory leave validity' and tenantid='default'),'90',
'2016-01-01',1,now(),1,now(),'default');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'SUSPENDED','Employee who is suspended','default');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'TRANSFERRED','Employee who is transferred','default');


insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPLIED','Leave Status when applied','default');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPROVED','Leave Status when approved','default');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','REJECTED','Leave Status when rejected','default');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','CANCELLED','Leave Status when cancelled','default');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','RESUBMITTED','Leave Status when resubmitted','default');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Cutoff_Date','Define the cut off date after which the application will go live',1,now(),1,now(),'default');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Cutoff_Date' and tenantid='default'),'2017-05-31',
'2016-01-01',1,now(),1,now(),'default');


insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPLIED','Employee Movement when applied','default');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPROVED','Employee Movement when approved','default');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','REJECTED','Employee Movement when rejected','default');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','CANCELLED','Employee Movement when cancelled','default');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','RESUBMITTED','Employee Movement when resubmitted','default');

SELECT setval('seq_egeis_designation', nextval('seq_egeis_designation'), true);

INSERT INTO egeis_hrConfiguration (id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES (nextval('seq_egeis_hrConfiguration'), 'Autogenerate_username', 'This will define if username for employee needs to be system generated or manually captured', 1, now(), 1, now(), 'default');

INSERT INTO egeis_hrConfigurationValues (id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES(nextval('seq_egeis_hrConfigurationValues'), (SELECT id FROM egeis_hrconfiguration WHERE keyname='Autogenerate_username' AND tenantid='default'), 'Y', '2016-01-01', 1, now(), 1, now(), 'default');

update egeis_hrconfiguration set keyname='Compensatory_leave_validity' where keyname='Compensatory leave validity';

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','FORWARDED','Leave Status when forwarded','default');
