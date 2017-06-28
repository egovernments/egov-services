UPDATE eg_action SET url = 'pgr-master/service/v1/_create' WHERE url = 'pgr-master/service/_create' AND name = 'Create a Service Type';

UPDATE eg_action SET url = 'pgr-master/service/v1/{code}/_update' WHERE url = 'pgr-master/service/{code}/_update' AND name='Update a Service Type'; 

UPDATE eg_action SET url = '/pgr-master/serviceGroup/v1/_create' WHERE url = '/pgr-master/serviceGroup/_create' AND name = 'Create Service Group';

UPDATE eg_action SET url = '/pgr-master/serviceGroup/v1/{id}/_update' WHERE url = '/pgr-master/serviceGroup/{id}/_update' AND name = 'Update Service Group'; 

UPDATE eg_action SET url = '/workflow/escalation-hours/v1/_create' WHERE url = '/workflow/escalation-hours/_create' AND name = 'Create Escalation Time Type';

UPDATE eg_action SET url = '/workflow/escalation-hours/v1/_update/{id}' WHERE url = '/workflow/escalation-hours/_update/{id}' AND name = 'Update Escalation Time Type';

UPDATE eg_action SET url = '/pgr-master/receivingcenter/v1/_create' WHERE url = '/pgr-master/receivingcenter/_create' AND name = 'Create receivingcenter Master'; 

UPDATE eg_action SET url = '/pgr-master/receivingcenter/v1/{id}/_update' WHERE url = '/pgr-master/receivingcenter/{id}/_update' AND name = 'Update receivingcenter Master'; 

UPDATE eg_action SET url = '/pgr-master/receivingmode/v1/_create' WHERE url = '/pgr-master/receivingmode/_create' AND name = 'Create receivingmode Master';

UPDATE eg_action SET url = '/pgr-master/receivingmode/v1/{id}/_update' WHERE url = '/pgr-master/receivingmode/{id}/_update' AND name = 'Update receivingmode Master'; 

UPDATE eg_action SET url = '/pgr-master/receivingcenter/v1/_search' WHERE url = '/pgr-master/receivingcenter/_search' AND name = 'Get all receivingcenters';

UPDATE eg_action SET url = '/pgr-master/receivingmode/v1/_search' WHERE url = '/pgr-master/receivingmode/_search' AND name = 'Get all receivingmodes'; 

UPDATE eg_action SET url = '/workflow/router/v1/_create' WHERE url = '/workflow/router/_create' AND name = 'CREATE COMPLAINT ROUTER'; 

UPDATE eg_action SET url = '/workflow/router/v1/_update' WHERE url = '/workflow/router/_update' AND name = 'UPDATE COMPLAINT ROUTER'; 

UPDATE eg_action SET url = '/workflow/router/v1/_search' WHERE url = '/workflow/router/_search' AND name = 'SEARCH COMPLAINT ROUTER';

INSERT INTO eg_action (id, name, url, servicecode, parentmodule, displayname, enabled, createdby, 
createddate, lastmodifiedby, lastmodifieddate) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Search a Service Type','pgr-master/service/v1/_search',
 'PGR','PGR','Search a Service Type for PGR', false, 1, now() ,1 ,now());
 
INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('GA',(SELECT id FROM eg_action WHERE url='pgr-master/service/v1/_search'),'default');

INSERT INTO eg_action (id, name, url, servicecode, parentmodule, displayname, enabled, createdby, 
createddate, lastmodifiedby, lastmodifieddate) VALUES (NEXTVAL('SEQ_EG_ACTION'),'Search a Service Group','pgr-master/serviceGroup/v1/_search',
 'PGR','PGR','Search a Service Group for PGR', false, 1, now() ,1 ,now());
 
INSERT INTO eg_roleaction (rolecode, actionid, tenantid) VALUES ('GA',(SELECT id FROM eg_action WHERE url='pgr-master/serviceGroup/v1/_search'),'default');
