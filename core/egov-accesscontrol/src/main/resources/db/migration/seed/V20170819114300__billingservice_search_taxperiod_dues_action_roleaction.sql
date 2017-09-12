--------- Search Taxperiod ------------
insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'BS_TaxPeriodSearch', '/billing-service/taxperiod/_search', 'BILLING_SERVICE', null, 1, 'Get Tax Periods', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantId) select 'SUPERUSER', id, 'default' from eg_action where name='BS_TaxPeriodSearch' and url = '/billing-service/taxperiod/_search';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where name='BS_TaxPeriodSearch' and url = '/billing-service/taxperiod/_search';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where name='BS_TaxPeriodSearch'  and url = '/billing-service/taxperiod/_search';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where name='BS_TaxPeriodSearch'  and url = '/billing-service/taxperiod/_search';


--------- Get Dues -----------
insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'BS_GetDemandDues', '/billing-service/demand/_dues', 'BILLING_SERVICE', null, 1, 'Get Demand Dues', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantId) select 'SUPERUSER', id, 'default' from eg_action where name='BS_GetDemandDues' and url = '/billing-service/demand/_dues';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where name='BS_GetDemandDues' and url = '/billing-service/demand/_dues';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where name='BS_GetDemandDues'  and url = '/billing-service/demand/_dues';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where name='BS_GetDemandDues'  and url = '/billing-service/demand/_dues';

