INSERT into eg_user (id,title,salutation,dob,locale,username,password,pwdexpirydate,mobilenumber,altcontactnumber,emailid,createddate,lastmodifieddate,
createdby,lastmodifiedby,active,name,gender,pan,aadhaarnumber,type,version,guardian,guardianrelation,signature,accountlocked)values(2,null,'MR.',null,'en_IN',
'anonymous','XYZ','2099-01-01 00:00:00',null,null,null,'2010-01-01 00:00:00','2015-01-01 00:00:00',1,1,'t','Anonymous',null,null,null,'SYSTEM',0,null,null,null,'f');


INSERT into egpgr_complainant (id,email,mobile,name,userdetail,address,version)values(1,'abc@gmail.com','7475844747','kumar',2,'Near Temple',0);



Insert into eg_hierarchy_type( id,name,code,createddate,lastmodifieddate,createdby,lastmodifiedby,version,localname)values(
2,'LOCATION','LOCATION','2010-01-01 00:00:00','2015-01-01 00:00:00',1,1,0,null);

Insert into eg_boundary_type(id,hierarchy,parent,name,hierarchytype,createddate,lastmodifieddate,createdby,lastmodifiedby,version,localname,code)
values(6,2,null,'City',2,'2015-08-28 10:44:03.831086','2015-08-28 10:44:03.831086',1,1,0,null,null);


Insert into eg_boundary (id,boundarynum,parent,name,boundarytype,localname,bndry_name_old,bndry_name_old_local,fromdate,todate,bndryid,longitude,latitude,
materializedpath,ishistory,createddate,lastmodifieddate,createdby,lastmodifiedby,version,code)values(1,25,null,'Bheemili Mangala
Veedhi',6,'Bheemili Mangala Veedhi',null,null,'2004-04-01 00:00:00','2099-04-01 00:00:00',null,null,null,2.25,'f','2015-08-28 10:44:03.831086',
'2015-08-28 10:44:03.831086',1,1,null,null);


insert into eg_department (id,name,createddate,code,createdby,lastmodifiedby,lastmodifieddate,version)
values(18,'Health','2010-01-01 00:00:00','H',1,1,'2015-01-01 00:00:00',0);

Insert into egpgr_complainttype_category (id,name,description,version) values(4,'Town Planning','Town Planning',0);

insert into egpgr_complainttype (id,name,department,version,code,isactive,description,createddate,lastmodifieddate,createdby,
lastmodifiedby,slahours,hasfinancialimpact,attributes,category,metadata,type,keywords)values(6,'Absenteesim of door to door garbage collector'
,18,0,'AODTDGC','t',null,'2010-01-01 00:00:00','2015-01-01 00:00:00',1,1,24,'f',null,4,'t','realtime',null);

insert into eg_position (name,id,deptdesig,createddate,lastmodifieddate,createdby,lastmodifiedby,ispostoutsourced,version)
values('L-JUNIOR ENGINEER-1',1,1,'2015-01-01 00:00:00','2015-01-01 00:00:00',1,1,'f',0);


INSERT into egpgr_complaint(id,crn,complaintType,complainant,assignee,location,childlocation,
status,details,landmarkDetails,receivingMode,receivingCenter,lng,lat,escalation_date,department,
citizenFeedback,latlngAddress,crossHierarchyId,state_id,createdby,createddate,lastmodifiedby,lastmodifieddate)VALUES(1,'0005-2017-AB',6,1,1,1,null,
1,'This is a huge problem','Near Temple',5,5,0,0,'2016-12-22 00:00:00',18,5,null,2,5,2,'2016-12-20 00:00:00',2,'2016-12-21 00:00:00');













