INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
VALUES (nextval('seq_egpgr_complainttype'), 'Absenteesim of door to door garbage collector ', NULL, 0, 'PAODTDGC', true, NULL, now(), NULL, 0, NULL, 24, false, (select id from egpgr_complainttype_category where name='Public Health and Sanitation ' and tenantId='panavel'), true, 'panavel', 'realtime');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
VALUES (nextval('seq_egpgr_complainttype'), 'Absenteesim of sweepers ', NULL, 0, 'PAOS', true, NULL, now(), NULL, 0, NULL, 24, false, (select id from egpgr_complainttype_category where name='Administration '  and tenantId='panavel'), true, 'panavel', 'realtime');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
VALUES (nextval('seq_egpgr_complainttype'), 'Bio Medical waste/Health hazard waste removal ', NULL, 0, 'PBMWHHWR', true, NULL, now(), NULL, 0, NULL, 24, false, (select id from egpgr_complainttype_category where name='Public Health and Sanitation ' and tenantId='panavel'), true, 'panavel', 'realtime');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
VALUES (nextval('seq_egpgr_complainttype'), 'Broken Bin ', NULL, 0, 'PBRKNB', true, NULL, now(), NULL, 0, NULL, 24, false, (select id from egpgr_complainttype_category where name='Public Health and Sanitation ' and tenantId='panavel'), true, 'panavel', 'realtime');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
VALUES (nextval('seq_egpgr_complainttype'), 'Building plan sanction ', NULL, 0, 'PBPS', true, NULL, now(), NULL, 0, NULL, 24, false, (select id from egpgr_complainttype_category where name='Administration ' and tenantId='panavel'), true, 'panavel', 'realtime');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
 VALUES (nextval('seq_egpgr_complainttype'), 'Burning of garbage ', NULL, 0, 'PBOG', true, NULL, now(), NULL, 0, NULL, 24, false, (select id from egpgr_complainttype_category where name='Public Health and Sanitation ' and tenantId='panavel'), true, 'panavel', 'realtime');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
VALUES (nextval('seq_egpgr_complainttype'), 'Marriage certificate ', NULL, 0, 'PMC', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Marriage certificate ' and tenantId='panavel') ,true, 'panavel', 'realtime');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
VALUES (nextval('seq_egpgr_complainttype'), 'Water connection ', NULL, 0, 'PWCMS', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Water connection ' and tenantId='panavel'), true, 'panavel', 'realtime');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
VALUES (nextval('seq_egpgr_complainttype'), 'Drainage Connection ', NULL, 0, 'PSCMS', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Drainage Connection ' and tenantId='panavel'), true, 'panavel', 'realtime');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
VALUES (nextval('seq_egpgr_complainttype'), 'Assessment certificate ', NULL, 0, 'PPTAC', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Property Tax ' and tenantId='panavel'), true, 'panavel', 'realtime');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
VALUES (nextval('seq_egpgr_complainttype'), 'No Due certificate ', NULL, 0, 'PPTNDC', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Property Tax ' and tenantId='panavel'), true, 'panavel', 'realtime');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, tenantid, type)
VALUES (nextval('seq_egpgr_complainttype'), 'Transfer of property Heredity ', NULL, 0, 'PPTPH', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Property Tax ' and tenantId='panavel'), true, 'panavel', 'realtime');
