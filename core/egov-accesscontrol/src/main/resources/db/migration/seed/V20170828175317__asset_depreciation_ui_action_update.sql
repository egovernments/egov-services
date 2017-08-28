delete from eg_action where name = 'AssetDepreciationCreate';

INSERT INTO eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname, enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
VALUES (NEXTVAL('SEQ_EG_ACTION'),'AssetDepreciationCreate','/app/asset/asset-depreciation.html','AssetDepreciation',null,
(select id from service where name ='Asset Depreciation' and tenantid = 'default'),1,'Create Depreciation',true,1,now(),1,now());