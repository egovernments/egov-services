INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date, isdemand, tenantid) VALUES(nextval('seq_eg_demand_reason_master'), 'ServiceTax On GoodWill', (select id from eg_reason_category where code='SERVICETAX' and tenantid='default'), 'N', 'Leases And Agreements', 'GW_ST', 5, current_timestamp, current_timestamp,false, 'default');

INSERT INTO EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODE, tenantid) select nextval('seq_eg_demand_reason'), (select id from eg_demand_reason_master where code='GW_ST' and module='Leases And Agreements' and tenantid='default'), inst.id, null, null, current_timestamp, current_timestamp, '3502034', 'default' from eg_installment_master inst where inst.module='Leases And Agreements' and inst.tenantid='default';

INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date, isdemand, tenantid) VALUES(nextval('seq_eg_demand_reason_master'), 'ServiceTax On Advance', (select id from eg_reason_category where code='SERVICETAX' and tenantid='default'), 'N', 'Leases And Agreements', 'ADV_ST', 5, current_timestamp, current_timestamp,false, 'default');

INSERT INTO EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODE, tenantid) select nextval('seq_eg_demand_reason'), (select id from eg_demand_reason_master where code='ADV_ST' and module='Leases And Agreements' and tenantid='default'), inst.id, null, null, current_timestamp, current_timestamp, '3502034', 'default' from eg_installment_master inst where inst.module='Leases And Agreements' and inst.tenantid='default';


