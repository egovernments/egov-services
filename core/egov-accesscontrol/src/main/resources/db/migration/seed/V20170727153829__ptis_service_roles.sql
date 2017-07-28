insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'PTIS', 'Property Tax', true, 'Property Tax', 1, NULL, 'default');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'NEW_PROPERTY', 'New Property', true, 'New Property', 2, (select id from service where name ='Property Tax' and code='PTIS' and tenantId='default'), 'default');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'PTIS_MASTERS', 'PTIS Masters', true, 'Masters', 3, (select id from service where name ='Property Tax' and code='PTIS' and tenantId='default'), 'default');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'CALC_MASTERS', 'Calculation Masters', true, 'Tax Masters', 4, (select id from service where name ='Property Tax' and code='PTIS' and tenantId='default'), 'default');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'EXIST_PROPERTY', 'Existing Property', true, 'Existing Property', 5, (select id from service where name ='Property Tax' and code='PTIS' and tenantId='default'), 'default');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'PTIS_REPORTS', 'PTIS Reports', true, 'Reports', 6, (select id from service where name ='Property Tax' and code='PTIS' and tenantId='default'), 'default');

insert into eg_ms_role (name, code, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) values ('PTIS Master', 'PTIS_MASTER', 'Who has a access to Property Tax Masters', now(), 1, 1, now(), 0);
insert into eg_ms_role (name, code, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) values ('PTIS Admin', 'PTIS_ADMIN', 'Who has a access to change tax rates, factor etc', now(), 1, 1, now(), 0);
