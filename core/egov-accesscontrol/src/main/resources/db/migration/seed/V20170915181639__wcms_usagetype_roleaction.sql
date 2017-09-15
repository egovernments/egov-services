insert into eg_roleaction(roleCode, actionid, tenantId) values( (select code from eg_ms_role where code in ('CITIZEN')), (select id from eg_action where name='SearchUsageTypeMaster'), 'default' );
insert into eg_roleaction(roleCode, actionid, tenantId) values( (select code from eg_ms_role where code in ('CITIZEN')), (select id from eg_action where name='SearchSubUsageTypeMaster'), 'default' );




