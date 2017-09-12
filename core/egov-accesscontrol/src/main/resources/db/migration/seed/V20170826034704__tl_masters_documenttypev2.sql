--DocumentType--
update eg_action set url ='/tl-masters/documenttype/v2/_create' where name ='CreateTLDOCUMENTTYPE' and servicecode = 'TLDOCUMENTTYPE' and parentmodule =(select id from service where code='TLDOCUMENTTYPE' and tenantid='default')::text;

update eg_action set url ='/tl-masters/documenttype/v2/_search' where name ='ViewTLDOCUMENTTYPE' and servicecode = 'TLDOCUMENTTYPE' and parentmodule =(select id from service where code='TLDOCUMENTTYPE' and tenantid='default')::text;

update eg_action set url ='/tl-masters/documenttype/v2/_search' where name ='ModifyTLDOCUMENTTYPE' and servicecode = 'TLDOCUMENTTYPE' and parentmodule =(select id from service where code='TLDOCUMENTTYPE' and tenantid='default')::text;

update eg_action set url ='/tl-masters/documenttype/v2/_update' where name ='UpdateTLDOCUMENTTYPE' and servicecode = 'TLDOCUMENTTYPE' and parentmodule =(select id from service where code='TLDOCUMENTTYPE' and tenantid='default')::text;