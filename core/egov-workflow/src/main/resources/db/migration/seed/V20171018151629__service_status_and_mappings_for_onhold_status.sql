INSERT INTO service_status (id, name, tenantid, version, code) VALUES (nextval('seq_service_status'), 'ONHOLD', 'default', 0, 'ONHOLD');

--For role EMPLOYEE
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 3, (select id from service_status where name = 'FORWARDED' and tenantid = 'default'),
 5, (select id from service_status where name = 'ONHOLD' and tenantid = 'default'), 0, 'default');

INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 3, (select id from service_status where name = 'PROCESSING' and tenantid = 'default'),
 5, (select id from service_status where name = 'ONHOLD' and tenantid = 'default'), 0, 'default');

INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 3, (select id from service_status where name = 'REGISTERED' and tenantid = 'default'),
 6, (select id from service_status where name = 'ONHOLD' and tenantid = 'default'), 0, 'default');

INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 3, (select id from service_status where name = 'REOPENED' and tenantid = 'default'),
 1, (select id from service_status where name = 'ONHOLD' and tenantid = 'default'), 0, 'default');


--Mappings when current status is ONHOLD
--For Citizen
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 1, (select id from service_status where name = 'ONHOLD' and tenantid = 'default'),
 1, (select id from service_status where name = 'WITHDRAWN' and tenantid = 'default'), 0, 'default');

--For Employee
INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 3, (select id from service_status where name = 'ONHOLD' and tenantid = 'default'),
 1, (select id from service_status where name = 'PROCESSING' and tenantid = 'default'), 0, 'default');

INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 3, (select id from service_status where name = 'ONHOLD' and tenantid = 'default'),
 2, (select id from service_status where name = 'FORWARDED' and tenantid = 'default'), 0, 'default');

INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 3, (select id from service_status where name = 'ONHOLD' and tenantid = 'default'),
 3, (select id from service_status where name = 'COMPLETED' and tenantid = 'default'), 0, 'default');


INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 3, (select id from service_status where name = 'ONHOLD' and tenantid = 'default'),
 4, (select id from service_status where name = 'REJECTED' and tenantid = 'default'), 0, 'default');

