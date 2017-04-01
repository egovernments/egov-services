INSERT INTO eg_department (id, name, createddate, code, createdby, lastmodifiedby, lastmodifieddate, version)
VALUES(18,'Health','2010-01-01 00:00:00','H',1,1,'2015-01-01 00:00:00',0);

INSERT INTO egpgr_complainttype_category (id, name, description, version) VALUES (4,'Town Planning','Town Planning',0);

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby,
lastmodifiedby, slahours, hasfinancialimpact, attributes, category, metadata, type, keywords) VALUES (6,'Absenteesim of door to door garbage collector'
,18,0,'AODTDGC','t','garbage collector absent','2010-01-01 00:00:00','2015-01-01 00:00:00',1,1,24,'f',null,4,'t','realtime',null);
