INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'Water Charge',
 'Water Charge Connection',true,'wcms','Water Charge Transanction',1,
 (select id from service where code='WCMS' and parentmodule is null and tenantId='default'),'default');
 
