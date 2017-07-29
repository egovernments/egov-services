delete from eg_roleaction where rolecode = 'EMPLOYEE' and actionid = (select id from eg_action where url = '/egov-common-masters/categories/_search');

update service set name = 'Employee Self Service' where code = 'ESS' and tenantid = 'default';