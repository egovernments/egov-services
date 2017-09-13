insert into eg_roleaction(roleCode,actionid,tenantId)values('ULB Operator',(select id from eg_action where name='WCOutstandingReport'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('ULB Operator',(select id from eg_action where name='WCConsumerRegisterReport'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('ULB Operator',(select id from eg_action where name='WCDemandRegister'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('ULB Operator',(select id from eg_action where name='WCDCBReport'),'default');