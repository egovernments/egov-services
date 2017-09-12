insert into eg_ms_role (name, code, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) values ('TL Approver', 'TL_APPROVER', 'Who has a access to Trade License Workflow', now(), 1, 1, now(), 0);

insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='ViewLicenseCategory'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='DesignationSearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='ViewTLUOM'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='ViewTLDOCUMENTTYPE'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='ViewTLFEEMATRIX'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='SearchLicense'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='ViewTLPENALTYRATE'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='ModifyTLFEEMATRIX'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='SearchLicenseStatus'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='SearchEmployee'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='designationsMSSearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='License Register Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='TL Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='TL Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='TL Report Reload'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='SearchNoticeDocuments'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='SearchNoticeUI'),'default');