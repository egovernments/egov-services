 INSERT into egpgr_complainant (id,email,mobile,name,userdetail,address,version,tenantid)values(1,'abc@gmail.com','7475844747','kumar',2,'Near School',0,'ap.public');
 
 INSERT into egpgr_complainttype_category (id,name,description,version,tenantid) values(8,'Town_Planning','Town_Planning',0,'ap.public');

 INSERT into egpgr_complainttype (id,name,department,version,code,isactive,description,createddate,lastmodifieddate,createdby,
lastmodifiedby,slahours,hasfinancialimpact,category,metadata,type, tenantid) values (78,'Absenteesim of door_to_door garbage collector'
,18,0,'AODTDGCC','t','garbage collector absent','2010-01-01 00:00:00','2015-01-01 00:00:00',1,1,24,'f',8,'t','realtime', 'ap.public');
 
INSERT into egpgr_complaint(id,crn,complaintType,complainant,assignee,location,childlocation,
status,details,landmarkDetails,receivingMode,receivingCenter,lng,lat,escalation_date,department,
citizenFeedback,latlngAddress,crossHierarchyId,state_id,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)VALUES(1,'0005-2017-AB',78,1,1,1,4,
'REGISTERED','This is a huge problem','Near Temple',5,5,0,0,'2016-12-22T00:00:00.000',18,5,null,2,5,2,'2016-12-20 00:00:00',2,'2016-12-21 00:00:00','ap.public');


INSERT into egpgr_complainant (id,email,mobile,name,userdetail,address,version,tenantid)values(2,'ram@gmail.com','7475844747','Ram',3,'Near School',0,'ap.public');

INSERT into egpgr_complainttype_category (id,name,description,version,tenantid) values(9,'Admin','Administration',0,'ap.public');

INSERT into egpgr_complainttype (id,name,department,version,code,isactive,description,createddate,lastmodifieddate,createdby,
lastmodifiedby,slahours,hasfinancialimpact,category,metadata,type, tenantid)values(79,'Broken_Bin'
,19,0,'BB','t','bins are broken','2011-01-01 00:00:00','2016-01-01 00:00:00',1,1,28,'f',9,'t','realtime', 'ap.public');

INSERT into egpgr_complaint(id,crn,complaintType,complainant,assignee,location,childlocation,
status,details,landmarkDetails,receivingMode,receivingCenter,lng,lat,escalation_date,department,
citizenFeedback,latlngAddress,crossHierarchyId,state_id,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
VALUES(2,'0007-2016-XZ',79,2,2,2,null, 'FORWARDED','This is a huge problem','Near Temple',2,9,0,0,
'2017-12-22T00:00:00.000',19,5,null,2,5,2,'2016-12-20 00:00:00',2,'2016-12-21 00:00:00','ap.public');


 INSERT into egpgr_complainant (id,email,mobile,name,userdetail,address,version,tenantid)values(3,'shyam@gmail.com','8923618856','kumar',4,'Near School',0,'ap.public');
 
 INSERT into egpgr_complainttype_category (id,name,description,version,tenantid) values(10,'Default','Default',0,'ap.public');

 INSERT into egpgr_complainttype (id,name,department,version,code,isactive,description,createddate,lastmodifieddate,
 createdby, lastmodifiedby,slahours,hasfinancialimpact,category,metadata,type, tenantid)
 VALUES (80, 'Absenteesim_of_sweepers', 20, 0, 'AOSS', 't', 'sweepers not present', '2010-01-01 00:00:00',
 '2014-01-01 00:00:00', 1, 1, 20, 'f', 10, 't', 'realtime', 'ap.public');

INSERT into egpgr_complaint(id,crn,complaintType,complainant,assignee,location,childlocation,
status,details,landmarkDetails,receivingMode,receivingCenter,lng,lat,escalation_date,department,
citizenFeedback,latlngAddress,crossHierarchyId,state_id,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid)
VALUES(3, '0009-2016-MN', 80, 3, 1, 1, 4, 'REGISTERED', 'This is a huge problem', 'Near Temple', 5, 9, 0, 0,
'2016-12-02T00:00:00.000', 19, 5, null, 2, 5, 2, '2015-12-30 00:00:00', 2, '2016-12-21 00:00:00', 'ap.public');

