delete from eg_roleaction where actionid in(select id from eg_action where parentmodule ='PGR')
and rolecode='EMPLOYEE' and tenantid='ap.public';

delete from eg_roleaction where actionid in(select id from eg_action where parentmodule ='PGR')
and rolecode='EMPLOYEE' and tenantid='default';