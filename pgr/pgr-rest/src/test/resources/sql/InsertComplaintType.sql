INSERT INTO egpgr_complainttype_category (id, code,name, description, version, tenantid)
VALUES (4,'Town Planning','Town Planning','Town Planning',0,'tenantId');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate,
lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, type, tenantid)
VALUES (6,'Absenteesim of door to door garbage collector', 18,0, 'AODTDGC','t', 'garbage collector absent',
 '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, 'f', 4, 't', 'realtime', 'tenantId');
