update eg_action set url = '/pgr-master/receivingcenter/v1/_search' where name = 'Get all ReceivingCenters'; 

insert into eg_roleaction(roleCode, actionid, tenantId) 
values ('EMPLOYEE', (select id from eg_action where name = 'Get all ReceivingCenters' and url = '/pgr-master/receivingcenter/v1/_search'), 'default');
