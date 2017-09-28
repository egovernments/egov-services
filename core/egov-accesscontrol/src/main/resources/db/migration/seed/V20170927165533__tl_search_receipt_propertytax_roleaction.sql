insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_ADMIN',(select id from eg_action where name='SearchReceipt'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_CREATOR',(select id from eg_action where name='SearchReceipt'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='SearchReceipt'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_ADMIN',(select id from eg_action where name='SearchProperty'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_CREATOR',(select id from eg_action where name='SearchProperty'),'default');
