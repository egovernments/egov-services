SELECT setval('seq_egpgr_complaintstatus_mapping',(select max(id)+1 from egpgr_complaintstatus_mapping));

INSERT INTO service_status values(nextval('seq_service_status'),'RESUBMIT',0,'default','DS-RESUBMIT');

--keyword_service_status table data--
INSERT INTO keyword_service_status (keyword,tenantId,servicestatuscode,version,createdby,createddate,lastmodifiedby,lastmodifieddate)
values('Deliverable_service','default','DS-RESUBMIT',0,0,now(),0,now());


--egpgr_complaintstatus_mapping table data--
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 14, (select id from service_status where code = 'DSREJECTED' and tenantid='default'), 1, (select id from service_status where code = 'DS-RESUBMIT' and tenantid='default'), 0,'default');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid)
 VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 16, (select id from service_status where code = 'DSREJECTED' and tenantid='default'), 1, (select id from service_status where code = 'DS-RESUBMIT' and tenantid='default'), 0,'default');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 16, (select id from service_status where code = 'DS-RESUBMIT' and tenantid='default'), 1, (select id from service_status where code = 'DSPROGRESS' and tenantid='default'), 0,'default');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 16, (select id from service_status where code = 'DS-RESUBMIT' and tenantid='default'), 2, (select id from service_status where code = 'DSAPPROVED' and tenantid='default'), 0,'default');
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version,tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 16, (select id from service_status where code = 'DS-RESUBMIT' and tenantid='default'), 3, (select id from service_status where code = 'DSREJECTED' and tenantid='default'), 0,'default');


