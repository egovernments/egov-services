insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN', (select id from eg_action where name = 'ViewTLSUBCATEGORY'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN', (select id from eg_action where name = 'ViewLicenseCategory' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN', (select id from eg_action where name = 'ViewTLDOCUMENTTYPE' ),'default');


insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN', (select id from eg_action where name = 'CreateNewLicense'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN', (select id from eg_action where name = 'SearchLicense' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN', (select id from eg_action where name = 'UpdateLegacyLicense' ),'default');

