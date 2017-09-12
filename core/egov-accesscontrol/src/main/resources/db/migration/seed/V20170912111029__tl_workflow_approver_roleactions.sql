insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_CREATOR', (select id from eg_action where name = 'designationsMSSearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_CREATOR', (select id from eg_action where name = 'DesignationSearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_CREATOR', (select id from eg_action where name = 'SearchEmployee' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN', (select id from eg_action where name = 'designationsMSSearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN', (select id from eg_action where name = 'DesignationSearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN', (select id from eg_action where name = 'SearchEmployee'),'default');