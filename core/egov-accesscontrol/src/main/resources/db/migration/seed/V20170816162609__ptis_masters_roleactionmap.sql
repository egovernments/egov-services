insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where servicecode='PTIS_MASTERS' and url like '%_search%';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where servicecode='PTIS_MASTERS' and url like '%_search%';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where servicecode='PTIS_MASTERS' and url like '%_search%';

insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where servicecode='Workflow_MS' and url like '%egov-common-workflows%';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where servicecode='Workflow_MS' and url like '%egov-common-workflows%';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where servicecode='Workflow_MS' and url like '%egov-common-workflows%';

insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where name='UpdateDemand' and url = '/billing-service/demand/_update';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where name='UpdateDemand'  and url = '/billing-service/demand/_update';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where name='UpdateDemand'  and url = '/billing-service/demand/_update';
