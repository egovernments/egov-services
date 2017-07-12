INSERT INTO eg_action (id, name, url, servicecode, parentmodule, displayname, enabled, createdby, 
createddate, lastmodifiedby, lastmodifieddate) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Report Get Metadata','pgr-master/report/metadata/_get',
 'PGR','PGR','Report Get Metadata', false, 1, now() ,1 ,now()); 

INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('GA',(SELECT id FROM eg_action WHERE url='pgr-master/report/metadata/_get'),'default');
INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('GO',(SELECT id FROM eg_action WHERE url='pgr-master/report/metadata/_get'),'default');
INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('GRO',(SELECT id FROM eg_action WHERE url='pgr-master/report/metadata/_get'),'default');
INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('RO',(SELECT id FROM eg_action WHERE url='pgr-master/report/metadata/_get'),'default');

INSERT INTO eg_action (id, name, url, servicecode, parentmodule, displayname, enabled, createdby, 
createddate, lastmodifiedby, lastmodifieddate) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Report Get','pgr-master/report/_get',
 'PGR','PGR','Report Get', false, 1, now() ,1 ,now());

INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('GA',(SELECT id FROM eg_action WHERE url='pgr-master/report/_get'),'default');
INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('GO',(SELECT id FROM eg_action WHERE url='pgr-master/report/_get'),'default');
INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('GRO',(SELECT id FROM eg_action WHERE url='pgr-master/report/_get'),'default');
INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('RO',(SELECT id FROM eg_action WHERE url='pgr-master/report/_get'),'default');
