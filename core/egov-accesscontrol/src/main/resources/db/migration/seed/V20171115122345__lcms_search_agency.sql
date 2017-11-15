update eg_action set servicecode='ADVOCATE',displayname='Search Agency', parentmodule=(select id from service where code='ADVOCATE' and tenantid='default'),enabled=true where name = 'Agency Search';

