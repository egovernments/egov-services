insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where name='Get Report Data' and url = '/pgr-master/report/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where name='Get Report Data'  and url = '/pgr-master/report/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where name='Get Report Data'  and url = '/pgr-master/report/_get';
