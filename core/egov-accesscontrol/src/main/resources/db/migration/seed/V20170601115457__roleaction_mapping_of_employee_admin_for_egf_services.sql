INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchBank'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchBankBranch'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchFund'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchFunction_MS'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchFunctionary'), 'default');