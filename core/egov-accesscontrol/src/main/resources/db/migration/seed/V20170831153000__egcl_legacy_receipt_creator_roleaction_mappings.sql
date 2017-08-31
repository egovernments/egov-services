INSERT INTO eg_ms_role(name,code,description,createddate,createdby,lastmodifiedby,lastmodifieddate) values
('Legacy Receipt Creator','LEGACY_RECEIPT_CREATOR','Legacy Receipt Creator',now(),1,1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('LEGACY_RECEIPT_CREATOR',(select id from eg_action where name = 'CreateReceipt'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('LEGACY_RECEIPT_CREATOR',(select id from eg_action where name ='SearchReceipt'),'default');

