INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)
VALUES (nextval('SEQ_EG_ACTION'), 'Rates Comparison With History Data', '/inventory/reports/ratesComparisonWithHistory', 'INVRPT',null,
(select id from service where name='INV Report' and tenantid='default'), 3, 'Rates Comparison With History Data', true, 1, now(), 1, now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Rates Comparison With History Data'),'default');



INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)
VALUES (nextval('SEQ_EG_ACTION'), 'Comparison Between Different Rates', '/inventory/reports/comparisonBetweenDifferentRates', 'INVRPT',null,
(select id from service where name='INV Report' and tenantid='default'), 4, 'Comparison Between Different Rates ', true, 1, now(), 1, now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Comparison Between Different Rates'),'default');


