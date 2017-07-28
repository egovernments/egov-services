insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'COLLECTION','Collection',true,null,'Collection',null,null,'default');
insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'COLLECTION-MASTERS','Collection-Masters',true,null,'Masters',1,
(select id from service where code='COLLECTION'),'default');
insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'BUSINESSCATEGORY','Business Category',true,null,'Business Category',1,
(select id from service where code='COLLECTION-MASTERS'),'default');
insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'BUSINESSDETAIL','Business Detail',true,null,'Business Detail',2,
(select id from service where code='COLLECTION-MASTERS'),'default');
insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'COLLECTION-TRANSACTIONS','Collection-Transactions',true,null,'Transactions',2,
(select id from service where code='COLLECTION'),'default');
