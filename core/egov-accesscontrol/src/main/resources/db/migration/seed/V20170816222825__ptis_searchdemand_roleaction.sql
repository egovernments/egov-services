insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where name='SearchDemand' and url = '/billing-service/demand/_search';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where name='SearchDemand'  and url = '/billing-service/demand/_search';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where name='SearchDemand'  and url = '/billing-service/demand/_search';
