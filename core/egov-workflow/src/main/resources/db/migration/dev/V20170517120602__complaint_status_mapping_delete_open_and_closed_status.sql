delete from egpgr_complaintstatus_mapping where current_status_id = (select id from service_status where code = 'DO');
delete from egpgr_complaintstatus_mapping where current_status_id = (select id from service_status where code = 'DC');
