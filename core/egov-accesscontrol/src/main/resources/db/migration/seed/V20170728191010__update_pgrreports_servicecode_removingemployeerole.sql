delete from eg_roleaction where tenantId='default' and actionId =(select id from eg_action where name = 'Get Report Data') and roleCode='EMPLOYEE';
delete from eg_roleaction where tenantId='default' and actionId =(select id from eg_action where name = 'Get Report MetaData') and roleCode='EMPLOYEE';
delete from eg_roleaction where tenantId='default' and actionId =(select id from eg_action where name = 'Get Report Reload') and roleCode='EMPLOYEE';


delete from eg_roleaction where tenantId='panavel' and actionId =(select id from eg_action where name = 'Get Report Data') and roleCode='EMPLOYEE';
delete from eg_roleaction where tenantId='panavel' and actionId =(select id from eg_action where name = 'Get Report MetaData') and roleCode='EMPLOYEE';
delete from eg_roleaction where tenantId='panavel' and actionId =(select id from eg_action where name = 'Get Report Reload') and roleCode='EMPLOYEE';

update eg_action set servicecode ='RPT' where name ='Get Report Data';
update eg_action set servicecode ='RPT' where name ='Get Report MetaData';
update eg_action set servicecode ='RPT' where name ='Get Report Reload';
