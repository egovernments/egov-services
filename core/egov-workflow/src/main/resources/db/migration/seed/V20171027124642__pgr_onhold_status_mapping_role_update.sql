update egpgr_complaintstatus_mapping set role_id = 3 where tenantid = 'default' and role_id = 1 and current_status_id = 
(select id from service_status where name = 'ONHOLD' and tenantid = 'default') and
show_status_id = (select id from service_status where name = 'ONHOLD' and tenantid = 'default'); 
