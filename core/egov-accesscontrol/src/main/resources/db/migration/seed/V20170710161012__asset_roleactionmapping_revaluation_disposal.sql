insert into eg_roleaction(roleCode,actionid,tenantid) values
('SUPERUSER',(select id from eg_action where name ='AssetSaleAndDisposalSearchToView'),'default'); 


insert into eg_roleaction(roleCode,actionid,tenantid) values
('SUPERUSER',(select id from eg_action where name ='AssetSaleAndDisposalSearchService'),'default');


insert into eg_roleaction(roleCode,actionid,tenantid) values
('SUPERUSER',(select id from eg_action where name ='AssetRevaluationCreate'),'default'); 



insert into eg_roleaction(roleCode,actionid,tenantid) values
('SUPERUSER',(select id from eg_action where name ='AssetRevaluationSearchService'),'default'); 


insert into eg_roleaction(roleCode,actionid,tenantid) values
('SUPERUSER',(select id from eg_action where name ='AssetSaleAndDisposalCreate'),'default'); 
