INSERT into eg_user (id,title,salutation,dob,locale,username,password,pwdexpirydate,mobilenumber,altcontactnumber,emailid,createddate,lastmodifieddate,
createdby,lastmodifiedby,active,name,gender,pan,aadhaarnumber,type,version,guardian,guardianrelation,signature,accountlocked)values(2,null,'MR.',null,'en_IN',
'anonymous','XYZ','2099-01-01 00:00:00',null,null,null,'2010-01-01 00:00:00','2015-01-01 00:00:00',1,1,'t','Anonymous',null,null,null,'SYSTEM',0,null,null,null,'f');
INSERT into egpgr_complainant (id,email,mobile,name,userdetail,address,version)values(1,'abc@gmail.com','7475844747','kumar',2,'Near School',0);
Insert into eg_hierarchy_type( id,name,code,createddate,lastmodifieddate,createdby,lastmodifiedby,version,localname)values(
2,'LOCATION','LOCATION','2010-01-01 00:00:00','2015-01-01 00:00:00',1,1,0,null);
Insert into eg_boundary_type(id,hierarchy,parent,name,hierarchytype,createddate,lastmodifieddate,createdby,lastmodifiedby,version,localname,code)
values(6,2,null,'City',2,'2015-08-28 10:44:03.831086','2015-08-28 10:44:03.831086',1,1,0,null,null);
Insert into eg_boundary (id,boundarynum,parent,name,boundarytype,localname,bndry_name_old,bndry_name_old_local,fromdate,todate,bndryid,longitude,latitude,
materializedpath,ishistory,createddate,lastmodifieddate,createdby,lastmodifiedby,version,code)values(1,25,null,'Bheemili Mangala
Veedhi',6,'Bheemili Mangala Veedhi',null,null,'2004-04-01 00:00:00','2099-04-01 00:00:00',null,null,null,2.25,'f','2015-08-28 10:44:03.831086',
'2015-08-28 10:44:03.831086',1,1,null,null);
Insert into eg_boundary (id,boundarynum,parent,name,boundarytype,localname,bndry_name_old,bndry_name_old_local,fromdate,todate,bndryid,longitude,latitude,
materializedpath,ishistory,createddate,lastmodifieddate,createdby,lastmodifiedby,version,code)values(4,29,null,'Bank Road'
,6,'Bank Road',null,null,'2004-04-01 00:00:00','2099-04-01 00:00:00',null,0,0,2.25,'f','2015-08-28 10:44:03.831086',
'2015-08-28 10:44:03.831086',1,1,null,null);
insert into eg_department (id,name,createddate,code,createdby,lastmodifiedby,lastmodifieddate,version)
values(18,'Health','2010-01-01 00:00:00','H',1,1,'2015-01-01 00:00:00',0);
Insert into egpgr_complainttype_category (id,name,description,version) values(4,'Town Planning','Town Planning',0);
insert into egpgr_complainttype (id,name,department,version,code,isactive,description,createddate,lastmodifieddate,createdby,
lastmodifiedby,slahours,hasfinancialimpact,attributes,category,metadata,type,keywords)values(6,'Absenteesim of door to door garbage collector'
,18,0,'AODTDGC','t','garbage collector absent','2010-01-01 00:00:00','2015-01-01 00:00:00',1,1,24,'f',null,4,'t','realtime',null);
insert into eg_position (name,id,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version)
values('L-JUNIOR ENGINEER-1',1,1,'2015-01-01 00:00:00','2015-01-01 00:00:00',1,1,'f',0);
INSERT into egpgr_complaint(id,crn,complaintType,complainant,assignee,location,childlocation,
status,details,landmarkDetails,receivingMode,receivingCenter,lng,lat,escalation_date,department,
citizenFeedback,latlngAddress,crossHierarchyId,state_id,createdby,createddate,lastmodifiedby,lastmodifieddate)VALUES(1,'0005-2017-AB',6,1,1,1,4,
1,'This is a huge problem','Near Temple',5,5,0,0,'2016-12-22T00:00:00.000',18,5,null,2,5,2,'2016-12-20 00:00:00',2,'2016-12-21 00:00:00');
INSERT into eg_user (id,title,salutation,dob,locale,username,password,pwdexpirydate,mobilenumber,altcontactnumber,emailid,createddate,lastmodifieddate,
createdby,lastmodifiedby,active,name,gender,pan,aadhaarnumber,type,version,guardian,guardianrelation,signature,accountlocked)values(3,null,'MR.',null,'mr_IN',
'Ram','XYZABC','2056-01-01 00:00:00','7475844747',null,'ram@gmail.com','2011-01-01 00:00:00','2016-02-01 00:00:00',1,1,'t','Rama',null,null,null,'CITIZEN',0,null,null,null,'f');
INSERT into egpgr_complainant (id,email,mobile,name,userdetail,address,version)values(2,'ram@gmail.com','7475844747','Ram',3,'Near School',0);
Insert into eg_hierarchy_type( id,name,code,createddate,lastmodifieddate,createdby,lastmodifiedby,version,localname)values(
3,'ADMINISTRATION','ADMIN','2010-01-01 00:00:00','2015-01-01 00:00:00',1,1,0,null);
Insert into eg_boundary_type(id,hierarchy,parent,name,hierarchytype,createddate,lastmodifieddate,createdby,lastmodifiedby,version,localname,code)
values(2,10,null,'Zone',3,'2016-08-28 10:44:03.831086','2016-08-28 10:44:03.831086',1,1,0,null,null);
Insert into eg_boundary_type(id,hierarchy,parent,name,hierarchytype,createddate,lastmodifieddate,createdby,lastmodifiedby,version,localname,code)
values(8,3,2,'Ward',3,'2017-08-28 10:44:03.831086','2017-08-28 10:44:03.831086',1,1,0,null,null);
Insert into eg_boundary (id,boundarynum,parent,name,boundarytype,localname,bndry_name_old,bndry_name_old_local,fromdate,todate,bndryid,longitude,latitude,
materializedpath,ishistory,createddate,lastmodifieddate,createdby,lastmodifiedby,version,code)values(2,15,null,'Bheemilipatnam',8,'Bheemilipatnam',null,null,'2014-04-01 00:00:00','2080-04-01 00:00:00',null,null,null,2.25,'f','2016-08-28 10:44:03.831086',
'2016-08-28 10:44:03.831086',1,1,null,null);
insert into eg_department (id,name,createddate,code,createdby,lastmodifiedby,lastmodifieddate,version)
values(19,'Accounts','2009-01-01 00:00:00','A',1,1,'2010-01-01 00:00:00',0);
Insert into egpgr_complainttype_category (id,name,description,version) values(5,'Administration','Administration',0);
insert into egpgr_complainttype (id,name,department,version,code,isactive,description,createddate,lastmodifieddate,createdby,
lastmodifiedby,slahours,hasfinancialimpact,attributes,category,metadata,type,keywords)values(7,'Broken Bin'
,19,0,'BB','t','bins are broken','2011-01-01 00:00:00','2016-01-01 00:00:00',1,1,28,'f',null,5,'t','realtime',null);
insert into eg_position (name,id,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version)
values('L-JUNIOR ENGINEER-2',2,2,'2016-01-01 00:00:00','2016-01-01 00:00:00',1,1,'f',0);
INSERT into egpgr_complaint(id,crn,complaintType,complainant,assignee,location,childlocation,
status,details,landmarkDetails,receivingMode,receivingCenter,lng,lat,escalation_date,department,
citizenFeedback,latlngAddress,crossHierarchyId,state_id,createdby,createddate,lastmodifiedby,lastmodifieddate)VALUES(2,'0007-2016-XZ',7,2,2,2,null,
2,'This is a huge problem','Near Temple',2,9,0,0,'2017-12-22T00:00:00.000',19,5,null,2,5,2,'2016-12-20 00:00:00',2,'2016-12-21 00:00:00');
INSERT into eg_user (id,title,salutation,dob,locale,username,password,pwdexpirydate,mobilenumber,altcontactnumber,emailid,createddate,lastmodifieddate,
createdby,lastmodifiedby,active,name,gender,pan,aadhaarnumber,type,version,guardian,guardianrelation,signature,accountlocked)values(4,null,'MR.',null,'mr_IN',
'Shyam','YAMLA','2078-01-01 00:00:00','8923618856',null,'shyam@gmail.com','2015-01-01 00:00:00','2015-02-01 00:00:00',1,1,'t','Shyama',null,null,null,'CITIZEN',0,null,null,null,'f');
INSERT into egpgr_complainant (id,email,mobile,name,userdetail,address,version)values(3,'shyam@gmail.com','8923618856','Shyam',4,'Near School',0);
Insert into eg_hierarchy_type( id,name,code,createddate,lastmodifieddate,createdby,lastmodifiedby,version,localname)values(
1,'REVENUE','REV','2011-01-01 00:00:00','2012-01-01 00:00:00',1,1,0,null);
insert into eg_department (id,name,createddate,code,createdby,lastmodifiedby,lastmodifieddate,version)
values(20,'Roads','2009-01-01 00:00:00','R',1,1,'2010-01-01 00:00:00',0);
Insert into egpgr_complainttype_category (id,name,description,version) values(6,'Default','Default',0);
insert into egpgr_complainttype (id,name,department,version,code,isactive,description,createddate,lastmodifieddate,createdby,
lastmodifiedby,slahours,hasfinancialimpact,attributes,category,metadata,type,keywords)values(8,'Absenteesim of sweepers'
,20,0,'AOS','t','sweepers not present','2010-01-01 00:00:00','2014-01-01 00:00:00',1,1,20,'f',null,6,'t','realtime',null);
insert into eg_position (name,id,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version)
values('L-JUNIOR ENGINEER-3',3,2,'2016-01-01 00:00:00','2016-01-01 00:00:00',1,1,'f',0);
INSERT into egpgr_complaint(id,crn,complaintType,complainant,assignee,location,childlocation,
status,details,landmarkDetails,receivingMode,receivingCenter,lng,lat,escalation_date,department,
citizenFeedback,latlngAddress,crossHierarchyId,state_id,createdby,createddate,lastmodifiedby,lastmodifieddate)VALUES(3,'0009-2016-MN',6,3,1,1,4,
2,'This is a huge problem','Near Temple',5,9,0,0,'2016-12-02T00:00:00.000',19,5,null,2,5,2,'2015-12-30 00:00:00',2,'2016-12-21 00:00:00');


























