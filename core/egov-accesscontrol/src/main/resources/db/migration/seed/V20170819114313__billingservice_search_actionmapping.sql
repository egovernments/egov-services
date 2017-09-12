insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where name='SearchBill' and url = '/billing-service/bill/_search';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where name='SearchBill'  and url = '/billing-service/bill/_search';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where name='SearchBill'  and url = '/billing-service/bill/_search';

insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where name='BS_SearchDemandDetail' and url = '/billing-service/demand/demanddetail/_search';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where name='BS_SearchDemandDetail'  and url = '/billing-service/demand/demanddetail/_search';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where name='BS_SearchDemandDetail'  and url = '/billing-service/demand/demanddetail/_search';
