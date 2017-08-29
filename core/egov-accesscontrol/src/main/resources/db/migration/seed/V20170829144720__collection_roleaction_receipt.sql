insert into eg_roleaction(roleCode,actionid,tenantid)values('COLL_OPERATOR',(select id from eg_action where name='CreateReceipt'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('COLL_OPERATOR',(select id from eg_action where name='UpdateReceipt'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('COLL_OPERATOR',(select id from eg_action where name='SearchReceipt'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('BANK_COLL_OPERATOR',(select id from eg_action where name='CreateReceipt'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('BANK_COLL_OPERATOR',(select id from eg_action where name='SearchReceipt'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('CSC_COLL_OPERATOR',(select id from eg_action where name='CreateReceipt'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('CSC_COLL_OPERATOR',(select id from eg_action where name='SearchReceipt'),'default');
