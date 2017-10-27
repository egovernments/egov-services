INSERT INTO egpgr_complaintstatus_mapping (id, role_id, current_status_id, orderno, show_status_id, version, tenantid) 
VALUES (nextval('seq_egpgr_complaintstatus_mapping'), 1, (select id from service_status where name = 'ONHOLD' and tenantid = 'default'),
 5, (select id from service_status where name = 'ONHOLD' and tenantid = 'default'), 0, 'default');
