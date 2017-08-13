-- Category--
update eg_action set url ='/tl-masters/category/v1/_create' where name ='CreateLicenseCategory' and servicecode = 'LICENSECATEGORY' and parentmodule =(select id from service where code='LICENSECATEGORY' and tenantid='default')::text;

update eg_action set url ='/tl-masters/category/v1/_search' where name ='ViewLicenseCategory' and servicecode = 'LICENSECATEGORY' and parentmodule =(select id from service where code='LICENSECATEGORY' and tenantid='default')::text;

update eg_action set url ='/tl-masters/category/v1/_search' where name ='ModifyLicenseCategory' and servicecode = 'LICENSECATEGORY' and parentmodule =(select id from service where code='LICENSECATEGORY' and tenantid='default')::text;

update eg_action set url ='/tl-masters/category/v1/_update' where name ='UpdateLicenseCategory' and servicecode = 'LICENSECATEGORY' and parentmodule =(select id from service where code='LICENSECATEGORY' and tenantid='default')::text;

--Sub Category--
update eg_action set url ='/tl-masters/category/v1/_create' where name ='CreateTLSUBCATEGORY' and servicecode = 'TLSUBCATEGORY' and parentmodule =(select id from service where code='TLSUBCATEGORY' and tenantid='default')::text;

update eg_action set url ='/tl-masters/category/v1/_search' where name ='ViewTLSUBCATEGORY' and servicecode = 'TLSUBCATEGORY' and parentmodule =(select id from service where code='TLSUBCATEGORY' and tenantid='default')::text;

update eg_action set url ='/tl-masters/category/v1/_search' where name ='ModifyTLSUBCATEGORY' and servicecode = 'TLSUBCATEGORY' and parentmodule =(select id from service where code='TLSUBCATEGORY' and tenantid='default')::text;

update eg_action set url ='/tl-masters/category/v1/_update' where name ='UpdateTLSUBCATEGORY' and servicecode = 'TLSUBCATEGORY' and parentmodule =(select id from service where code='TLSUBCATEGORY' and tenantid='default')::text;



--UOM--
update eg_action set url ='/tl-masters/uom/v1/_create' where name ='CreateTLUOM' and servicecode = 'TLUOM' and parentmodule =(select id from service where code='TLUOM' and tenantid='default')::text;

update eg_action set url ='/tl-masters/uom/v1/_search' where name ='ViewTLUOM' and servicecode = 'TLUOM' and parentmodule =(select id from service where code='TLUOM' and tenantid='default')::text;

update eg_action set url ='/tl-masters/uom/v1/_search' where name ='ModifyTLUOM' and servicecode = 'TLUOM' and parentmodule =(select id from service where code='TLUOM' and tenantid='default')::text;

update eg_action set url ='/tl-masters/uom/v1/_update' where name ='UpdateTLUOM' and servicecode = 'TLUOM' and parentmodule =(select id from service where code='TLUOM' and tenantid='default')::text;

--DocumentType--
update eg_action set url ='/tl-masters/documenttype/v1/_create' where name ='CreateTLDOCUMENTTYPE' and servicecode = 'TLDOCUMENTTYPE' and parentmodule =(select id from service where code='TLDOCUMENTTYPE' and tenantid='default')::text;

update eg_action set url ='/tl-masters/documenttype/v1/_search' where name ='ViewTLDOCUMENTTYPE' and servicecode = 'TLDOCUMENTTYPE' and parentmodule =(select id from service where code='TLDOCUMENTTYPE' and tenantid='default')::text;

update eg_action set url ='/tl-masters/documenttype/v1/_search' where name ='ModifyTLDOCUMENTTYPE' and servicecode = 'TLDOCUMENTTYPE' and parentmodule =(select id from service where code='TLDOCUMENTTYPE' and tenantid='default')::text;

update eg_action set url ='/tl-masters/documenttype/v1/_update' where name ='UpdateTLDOCUMENTTYPE' and servicecode = 'TLDOCUMENTTYPE' and parentmodule =(select id from service where code='TLDOCUMENTTYPE' and tenantid='default')::text;

--PenaltyRate--
update eg_action set url ='/tl-masters/penaltyrate/v1/_create' where name ='CreateTLPENALTYRATE' and servicecode = 'TLPENALTYRATE' and parentmodule =(select id from service where code='TLPENALTYRATE' and tenantid='default')::text;

update eg_action set url ='/tl-masters/penaltyrate/v1/_search' where name ='ViewTLPENALTYRATE' and servicecode = 'TLPENALTYRATE' and parentmodule =(select id from service where code='TLPENALTYRATE' and tenantid='default')::text;

update eg_action set url ='/tl-masters/penaltyrate/v1/_search' where name ='ModifyTLPENALTYRATE' and servicecode = 'TLPENALTYRATE' and parentmodule =(select id from service where code='TLPENALTYRATE' and tenantid='default')::text;

update eg_action set url ='/tl-masters/penaltyrate/v1/_update' where name ='UpdateTLPENALTYRATE' and servicecode = 'TLPENALTYRATE' and parentmodule =(select id from service where code='TLPENALTYRATE' and tenantid='default')::text;

--FeeMatrix--
update eg_action set url ='/tl-masters/feematrix/v1/_create' where name ='CreateTLFEEMATRIX' and servicecode = 'TLFEEMATRIX' and parentmodule =(select id from service where code='TLFEEMATRIX' and tenantid='default')::text;

update eg_action set url ='/tl-masters/feematrix/v1/_search' where name ='ViewTLFEEMATRIX' and servicecode = 'TLFEEMATRIX' and parentmodule =(select id from service where code='TLFEEMATRIX' and tenantid='default')::text;

update eg_action set url ='/tl-masters/feematrix/v1/_search' where name ='ModifyTLFEEMATRIX' and servicecode = 'TLFEEMATRIX' and parentmodule =(select id from service where code='TLFEEMATRIX' and tenantid='default')::text;

update eg_action set url ='/tl-masters/feematrix/v1/_update' where name ='UpdateTLFEEMATRIX' and servicecode = 'TLFEEMATRIX' and parentmodule =(select id from service where code='TLFEEMATRIX' and tenantid='default')::text;

--Status--
update eg_action set url ='/tl-masters/status/v1/_create' where name ='CreateLicenseStatus' and servicecode = 'TLSTATUS' and parentmodule =(select id from service where code='TLSTATUS' and tenantid='default')::text;

update eg_action set url ='/tl-masters/status/v1/_search' where name ='SearchLicenseStatus' and servicecode = 'TLSTATUS' and parentmodule =(select id from service where code='TLSTATUS' and tenantid='default')::text;


update eg_action set url ='/tl-masters/status/v1/_update' where name ='UpdateLicenseStatus' and servicecode = 'TLSTATUS' and parentmodule =(select id from service where code='TLSTATUS' and tenantid='default')::text;