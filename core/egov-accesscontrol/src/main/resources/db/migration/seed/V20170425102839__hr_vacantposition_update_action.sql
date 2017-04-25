update eg_action set url='/departments/_search',queryparams=null,parentmodule=(select id from service where name='EGOV Common Masters') where name='CommonDepartmentsSearch';
update eg_action set url='/vacantpositions/_search',queryparams=null,parentmodule=(select id from service where name='HR Masters') where name='VacantPositions';
