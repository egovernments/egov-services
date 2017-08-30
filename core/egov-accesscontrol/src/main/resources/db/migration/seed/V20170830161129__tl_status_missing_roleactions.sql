insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',(select id from eg_action where name = 'CreateLicenseStatus' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',(select id from eg_action where name = 'SearchLicenseStatus' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_CREATOR',(select id from eg_action where name = 'SearchLicenseStatus' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',(select id from eg_action where name = 'UpdateLicenseStatus' ),'default');