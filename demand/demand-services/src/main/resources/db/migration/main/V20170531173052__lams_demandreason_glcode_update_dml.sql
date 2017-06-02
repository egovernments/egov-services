UPDATE EG_DEMAND_REASON SET GLCODE='4314001' where id_demand_reason_master in (select id from eg_demand_Reason_master where module='Leases And Agreements');
