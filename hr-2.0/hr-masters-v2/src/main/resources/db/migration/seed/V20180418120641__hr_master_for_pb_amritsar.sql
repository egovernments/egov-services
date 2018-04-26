delete from egeis_hrConfigurationValues;
delete from egeis_hrConfiguration;
delete from egeis_hrstatus;

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

INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Temporary', NULL, 'pb.amritsar');
INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Deputation', NULL, 'pb.amritsar');
INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Outsourced', NULL, 'pb.amritsar');

insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'APPSC','Andhra Public Service','pb.amritsar');
insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'Compassionate','Compassionate','pb.amritsar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'SUSPENDED','Employee who is suspended','pb.amritsar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'TRANSFERRED','Employee who is transferred','pb.amritsar');

insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'Others','Others','pb.amritsar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPLIED','Leave Status when applied','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','APPROVED','Leave Status when approved','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','REJECTED','Leave Status when rejected','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','CANCELLED','Leave Status when cancelled','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','RESUBMITTED','Leave Status when resubmitted','pb.amritsar');

insert into egeis_hrConfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(nextval('seq_egeis_hrConfiguration'),'Cutoff_Date','Define the cut off date after which the application will go live',1,now(),1,now(),'pb.amritsar');

insert into egeis_hrConfigurationValues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
values(nextval('seq_egeis_hrConfigurationValues'),(select id from egeis_hrconfiguration where keyname='Cutoff_Date' and tenantid='pb.amritsar'),'2017-05-31',
'2016-01-01',1,now(),1,now(),'pb.amritsar');

delete from egeis_group where name in ('State','Central','Local') and tenantid = 'pb.amritsar' ;
delete from egeis_recruitmenttype where name in ('Direct','Transfer','Compensatory') and tenantid='pb.amritsar' ;
delete from egeis_recruitmentMode where name in ('UPSC','Direct','Department Exams') and tenantid = 'pb.amritsar' ;
delete from egeis_recruitmentQuota where name in ('Sports','Ex-Serviceman','General') and tenantid = 'pb.amritsar' ;
delete from egeis_grade where name in ('A','B','G') and tenantid = 'pb.amritsar' ;

insert into egeis_group(id,name,description,tenantid) values (nextval('seq_egeis_group'),'State','State','pb.amritsar');
insert into egeis_group(id,name,description,tenantid) values (nextval('seq_egeis_group'),'Central','Central','pb.amritsar');
insert into egeis_group(id,name,description,tenantid) values (nextval('seq_egeis_group'),'Local','Local','pb.amritsar');


insert into egeis_recruitmenttype(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentType'),'Direct','Direct','pb.amritsar');
insert into egeis_recruitmenttype(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentType'),'Transfer','Transfer','pb.amritsar');
insert into egeis_recruitmenttype(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentType'),'Compensatory','Compensatory','pb.amritsar');


insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'UPSC','Public Service','pb.amritsar');
insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'Direct','Direct','pb.amritsar');
insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'Department Exams','Department Exams','pb.amritsar');


insert into egeis_recruitmentQuota(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentQuota'),'Sports','Sports Quota','pb.amritsar');
insert into egeis_recruitmentQuota(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentQuota'),'Ex-Serviceman','Ex-Serviceman Quota','pb.amritsar');
insert into egeis_recruitmentQuota(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentQuota'),'General','General Quota','pb.amritsar');

insert into egeis_grade values (nextval('seq_egeis_grade'),'A','First Grade',1,true,'pb.amritsar');
insert into egeis_grade values (nextval('seq_egeis_grade'),'B','Second Level',2,true,'pb.amritsar');
insert into egeis_grade values (nextval('seq_egeis_grade'),'G','General Level',3,true,'pb.amritsar');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPLIED','Employee Movement when applied','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','APPROVED','Employee Movement when approved','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','REJECTED','Employee Movement when rejected','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','CANCELLED','Employee Movement when cancelled','pb.amritsar');
insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'EmployeeMovement','RESUBMITTED','Employee Movement when resubmitted','pb.amritsar');

SELECT setval('seq_egeis_designation', nextval('seq_egeis_designation'), true);

INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Assistant Examiner of Accounts', 'AEOA', 'Assistant Examiner of Accounts', null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Commissioner', 'COMM', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Examiner of Accounts', 'EOA', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Junior Accounts Officer', 'JAO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Senior Accountant', 'SACC', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Junior Accountant', 'JACC', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Accountant Category - IV', 'ACIV', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Manager', 'MGR', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Deputy Executive Engineer', 'DEE', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Senior Stenographer', 'SSTEN', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Typist', 'TYP', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Record Assistant', 'RC', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'System Manager', 'SM', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'System Assistant/ Data Entry Operator', 'SYASST', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Office Subordinate', 'OS', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Night Watchmen', 'NW', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Ayah', 'AYH', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Secretary', 'SEC', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Assistant Commissioner', 'ACOMM', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Public Relations Officer', 'PRO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Deputy Commissioner', 'DYCOMM', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Superintendent', 'SUPR', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Education Officer', 'EO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'School Supervisor', 'SCHSUP', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Assistant Engineer', 'AE', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Draughtsman', 'DRM', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Work Inspector/ Technical Mastry', 'WI', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'CAD/GIS Operator', 'CADOP', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Environmental Engineer (AE/AEE)', 'ENVE', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Fitter', 'FIT', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Tank Watchers', 'TW', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Turn Cock', 'TC', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Bore Well Attender', 'BOREA', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Electrician', 'ELEC', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Lighting Superintendent', 'LS', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Park Mazdor', 'PM', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Driver', 'DRV', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Chief Engineer', 'CE', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'PA to Chief Engineer', 'PACE', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Superintending Engineer', 'SUPRE', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Executive Engineer', 'EE', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Assistant Executive Engineer', 'AEE', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Homeo Medical Officer', 'HMO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Sanitary Inspector', 'SI', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Health Assistant/Birth and Death Registrar', 'HA', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Sanitary Maistry/Jawan', 'SANM', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'P H Worker', 'PHW', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Chief Medical Officer', 'CMO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Assistant Medical Officer', 'AMO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Municipal Health Officer', 'MHO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Sanitary Supervisor', 'SS', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Revenue Officer', 'RO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Bill Collector', 'BC', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'UD Revenue Inspector', 'UDRI', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Town Planning Officer (Special Grade)', 'TPOSPG', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Town Planning Supervisor', 'TPS', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Town Planning Building Overseer', 'TPB', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Tracer', 'TRC', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Town Surveyor', 'TS', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Chainman', 'CHNM', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Horticulture Officer', 'HRO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Chief City Planner', 'CCP', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'City Planner', 'CP', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Deputy City Planner', 'DCP', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Assistant City Planner', 'ACP', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Town Planning Offcer (Selection Grade)', 'TPOSCG', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Town Planning Offcer (Ordinary Grade)', 'TPOORG', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Transportation Planner', 'TRANP', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Town Project Officer', 'TPRJO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Community Organiser', 'CO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Project Officer', 'PO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Town Corodinator', 'TWC', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Assistant Project Officer', 'APO', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Deputy Statistical Officer', 'DSO', 'Deputy Statistical Officer', null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Veterinary officer', 'VO', 'Veterinary officer', null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Additional Commissioner', 'ADCOM', null, null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Deputy Chief Engineer', 'DCE', 'Deputy Chief Engineer', null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Technical Officer', 'TO', 'Technical Officer', null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Assistant Technical Officer', 'ATO', 'Assistant Technical Officer', null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Junior Technical Officer', 'JTO', 'Junior Technical Officer', null, true, 'pb.amritsar');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Municipal Engineer', 'ME', 'Municipal Engineer', null, true, 'pb.amritsar');

INSERT INTO egeis_hrConfiguration (id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES (nextval('seq_egeis_hrConfiguration'), 'Autogenerate_username', 'This will define if username for employee needs to be system generated or manually captured', 1, now(), 1, now(), 'pb.amritsar');

INSERT INTO egeis_hrConfigurationValues (id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)
    VALUES(nextval('seq_egeis_hrConfigurationValues'), (SELECT id FROM egeis_hrconfiguration WHERE keyname='Autogenerate_username' AND tenantid='pb.amritsar'), 'Y', '2016-01-01', 1, now(), 1, now(), 'pb.amritsar');

update egeis_hrconfiguration set keyname='Compensatory_leave_validity' where keyname='Compensatory leave validity';

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'LeaveApplication','FORWARDED','Leave Status when forwarded','pb.amritsar');


-- Disciplinary Authority

insert into egeis_disciplinaryauthority(id,name,tenantid) values(nextval('seq_egeis_disciplinaryauthority'),'Government of AP, AP Secretariat, Velagapudi','pb.amritsar');
insert into egeis_disciplinaryauthority(id,name,tenantid) values(nextval('seq_egeis_disciplinaryauthority'),'Commissioner & Director of Municipal Administration','pb.amritsar');
insert into egeis_disciplinaryauthority(id,name,tenantid) values(nextval('seq_egeis_disciplinaryauthority'),'Engineer-in-Chief (PH)','pb.amritsar');
insert into egeis_disciplinaryauthority(id,name,tenantid) values(nextval('seq_egeis_disciplinaryauthority'),'Director of Town & Country Planning','pb.amritsar');
insert into egeis_disciplinaryauthority(id,name,tenantid) values(nextval('seq_egeis_disciplinaryauthority'),'Regional Director-cum-Appellate Commissioner of Municipal Administration','pb.amritsar');
insert into egeis_disciplinaryauthority(id,name,tenantid) values(nextval('seq_egeis_disciplinaryauthority'),'Regional Deputy Director of Town & Country Planning','pb.amritsar');
insert into egeis_disciplinaryauthority(id,name,tenantid) values(nextval('seq_egeis_disciplinaryauthority'),'Superintending Engineer (PH)','pb.amritsar');
insert into egeis_disciplinaryauthority(id,name,tenantid) values(nextval('seq_egeis_disciplinaryauthority'),'Chairperson/Mayor of Municipal Council','pb.amritsar');
insert into egeis_disciplinaryauthority(id,name,tenantid) values(nextval('seq_egeis_disciplinaryauthority'),'Municipal Commissioner','pb.amritsar');
insert into egeis_disciplinaryauthority(id,name,tenantid) values(nextval('seq_egeis_disciplinaryauthority'),'Others','pb.amritsar');


-- Court Order Type

insert into egeis_courtordertype (id,name,tenantid) values(nextval('seq_egeis_courtordertype'),'Interim orders','pb.amritsar');
insert into egeis_courtordertype (id,name,tenantid) values(nextval('seq_egeis_courtordertype'),'Stay','pb.amritsar');
insert into egeis_courtordertype (id,name,tenantid) values(nextval('seq_egeis_courtordertype'),'Final orders','pb.amritsar');
