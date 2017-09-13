insert into eg_roleaction(roleCode,actionid,tenantid)values('COLL_REP_VIEW',(select id from eg_action where name='GetReportData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('COLL_REP_VIEW',(select id from eg_action where name='GetReportMetadata'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('COLL_REP_VIEW',(select id from eg_action where name='ReloadReport'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('COLL_REP_VIEW',(select id from eg_action where name='CashCollection'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('COLL_REP_VIEW',(select id from eg_action where name='ChequeCollection'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('COLL_REP_VIEW',(select id from eg_action where name='ReceiptRegister'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('COLL_REP_VIEW',(select id from eg_action where name='CollectionSummaryAccountHead'),'default');





