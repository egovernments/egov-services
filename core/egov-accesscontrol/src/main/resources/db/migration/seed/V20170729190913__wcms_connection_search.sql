INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, displayname, enabled, createdby, 
createddate, lastmodifiedby, lastmodifieddate) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Search Water Connection',
'/wcms-connection/connection/_search','tenantId=&acknowledgementNumber=' 'WCMS','WCMS','Search Water Connection', false, 1, now() ,1 ,now()); 

INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES 
('SUPERUSER',(SELECT id FROM eg_action WHERE url='/wcms-connection/connection/_search'),'default');