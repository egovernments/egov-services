delete  from eg_roleaction where actionid in (select id from eg_action where name = 'Get Child Boundary By Boundary' and servicecode = 'LOCATION_MS') and tenantid = 'default';

delete from eg_action  where name = 'Get Child Boundary By Boundary' and servicecode = 'LOCATION_MS';