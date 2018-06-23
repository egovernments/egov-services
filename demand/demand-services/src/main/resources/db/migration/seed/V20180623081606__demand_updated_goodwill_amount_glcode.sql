update eg_demand_reason set glcode='1308003' where id_demand_reason_master=(select id from eg_demand_reason_master where code='GOODWILL_AMOUNT' and tenantid='default');
