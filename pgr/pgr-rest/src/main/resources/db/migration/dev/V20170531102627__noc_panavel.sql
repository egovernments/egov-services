INSERT into egpgr_complainttype_category (id,name,description,version,tenantid)
values (nextval('seq_egpgr_complainttype_category'),'NOC for Fire protection','NOC for Fire protection',0,'panavel');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, type,tenantid)
VALUES (nextval('SEQ_EGPGR_COMPLAINTTYPE'), 'NOC for Fire protection', NULL, 0, 'NOC', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='NOC for Fire protection'), true, 'realtime','panavel');



