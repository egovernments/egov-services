insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'LegacyCreateReceipt','/collection-services/receipt/_legacycreate','COLLECTION-TRANSACTIONS',null,4,'Create Legacy  Receipt',false,1,now(),1,now());

update eg_action set url='/collection-services/receipt/_legacysearch' where url ='/collection-services/receipt/_search' ;