INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('AODTDGC', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'AODTDGC', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('AODTDGC', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('AODTDGC', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('AODTDGC', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('AODTDGC', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('AODTDGC', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('AOS', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'AOS', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('AOS', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('AOS', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('AOS', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('AOS', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('AOS', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('BMWHHWR', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'BMWHHWR', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('BMWHHWR', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('BMWHHWR', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('BMWHHWR', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('BMWHHWR', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('BMWHHWR', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('BPS', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'BPS', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('BPS', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('BPS', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('BPS', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('BPS', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('BPS', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('BOG', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'BOG', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('BOG', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('BOG', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('BOG', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('BOG', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('BOG', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('COAIEB', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'COAIEB', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('COAIEB', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('COAIEB', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('COAIEB', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('COAIEB', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('COAIEB', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRCTSC', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRCTSC', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRCTSC', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRCTSC', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRCTSC', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRCTSC', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRCTSC', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRD', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRD', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRD', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRD', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRD', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRD', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRD', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRBFS', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRBFS', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRBFS', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRBFS', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRBFS', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRBFS', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRBFS', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRP', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRP', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRP', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRP', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRP', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRP', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRP', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRPG', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRPG', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRPG', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRPG', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRPG', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRPG', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRPG', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRS', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRS', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRS', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRS', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRS', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRS', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRS', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRVL', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRVL', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRVL', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRVL', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRVL', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRVL', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRVL', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRBG', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRBG', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRBG', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRBG', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRBG', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRBG', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRBG', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRCH', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRCH', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRCH', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRCH', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRCH', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRCH', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRCH', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRPT', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRPT', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRPT', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRPT', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRPT', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRPT', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRPT', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRQOFIH', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRQOFIH', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRQOFIH', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRQOFIH', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRQOFIH', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRQOFIH', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRQOFIH', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRUR', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRUR', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRUR', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRUR', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRUR', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRUR', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRUR', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('20code', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', '20code', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('20code', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('20code', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('20code', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('20code', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('20code', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRTPT', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'CRTPT', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRTPT', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRTPT', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('CRTPT', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRTPT', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CRTPT', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('COMHOSWD', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'COMHOSWD', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('COMHOSWD', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('COMHOSWD', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('COMHOSWD', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('COMHOSWD', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('COMHOSWD', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DTTEP', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'DTTEP', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DTTEP', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DTTEP', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DTTEP', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DTTEP', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DTTEP', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOSA', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'DOSA', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DOSA', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DOSA', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DOSA', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOSA', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOSA', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOC', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'DOC', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DOC', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DOC', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DOC', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOC', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOC', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOD', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'DOD', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DOD', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DOD', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DOD', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOD', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOD', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DORSOTR', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'DORSOTR', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DORSOTR', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DORSOTR', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DORSOTR', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DORSOTR', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DORSOTR', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DM', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'DM', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DM', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DM', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('DM', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DM', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DM', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ESDTSL', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'ESDTSL', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ESDTSL', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ESDTSL', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ESDTSL', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ESDTSL', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ESDTSL', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('EOTPP', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'EOTPP', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('EOTPP', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('EOTPP', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('EOTPP', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('EOTPP', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('EOTPP', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('FNR', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'FNR', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('FNR', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('FNR', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('FNR', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('FNR', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('FNR', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--------------------------------------------------------------------------------------------------------------------
INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('GLWON', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'GLWON', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('GLWON', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('GLWON', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('GLWON', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('GLWON', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('GLWON', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

-----------------------------------------------------------------------------------------------------------------
INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IDOSTOS', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'IDOSTOS', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IDOSTOS', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IDOSTOS', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IDOSTOS', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IDOSTOS', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IDOSTOS', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

-----------------------------------------------------------------------------------------------------------------------
INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IS', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'IS', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IS', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IS', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IS', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IS', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IS', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

-----------------------------------------------------------------------------------------------------------------------
INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IMS', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'IMS', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IMS', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IMS', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IMS', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IMS', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IMS', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

-------------------------------------------------------------------------------------------------------------------
INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IOBADC', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'IOBADC', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IOBADC', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IOBADC', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('IOBADC', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IOBADC', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('IOBADC', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

------------------------------------------------------------------------------------------------------------------
INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MM', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'MM', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('MM', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('MM', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('MM', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MM', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MM', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

-------------------------------------------------------------------------------------------------------------------
INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NESR', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'NESR', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NESR', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NESR', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NESR', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NESR', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NESR', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

-------------------------------------------------------------------------------------------------------------------
INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NNFIER', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'NNFIER', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NNFIER', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NNFIER', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NNFIER', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NNFIER', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NNFIER', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

-----------------------------------------------------------------------------------------------------------------
INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NDC', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'NDC', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NDC', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NDC', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NDC', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NDC', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NDC', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

----------------------------------------------------------------------------------------------------------------
INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SGB', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'SGB', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SGB', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SGB', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SGB', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SGB', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SGB', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

-------------------------------------------------------------------------------------------------------------------

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SH', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'SH', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SH', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SH', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SH', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SH', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SH', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SPOW', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'SPOW', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SPOW', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SPOW', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SPOW', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SPOW', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SPOW', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SOGFL', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'SOGFL', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SOGFL', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SOGFL', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SOGFL', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SOGFL', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SOGFL', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SOW', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'SOW', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SOW', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SOW', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SOW', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SOW', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SOW', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SC', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'SC', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SC', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SC', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SC', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SC', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SC', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('TSS', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'TSS', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('TSS', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('TSS', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('TSS', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('TSS', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('TSS', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UAIC', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'UAIC', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UAIC', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UAIC', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UAIC', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UAIC', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UAIC', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UAAB', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'UAAB', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UAAB', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UAAB', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UAAB', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UAAB', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UAAB', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UASMP', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'UASMP', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UASMP', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UASMP', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UASMP', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UASMP', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UASMP', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UATC', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'UATC', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UATC', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UATC', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UATC', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UATC', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UATC', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NSL', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'NSL', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NSL', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NSL', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NSL', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NSL', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NSL', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NBOSL', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'NBOSL', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NBOSL', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NBOSL', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NBOSL', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NBOSL', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NBOSL', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NBGTOT', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'NBGTOT', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NBGTOT', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NBGTOT', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('NBGTOT', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NBGTOT', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('NBGTOT', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OOT', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'OOT', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OOT', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OOT', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OOT', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OOT', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OOT', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OOWF', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'OOWF', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OOWF', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OOWF', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OOWF', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OOWF', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OOWF', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OFOGB', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'OFOGB', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OFOGB', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OFOGB', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OFOGB', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OFOGB', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OFOGB', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OHCWRIHM', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'OHCWRIHM', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OHCWRIHM', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OHCWRIHM', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('OHCWRIHM', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OHCWRIHM', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('OHCWRIHM', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PI', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'PI', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PI', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PI', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PI', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PI', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PI', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PQOW', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'PQOW', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PQOW', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PQOW', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PQOW', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PQOW', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PQOW', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);


--61--
INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PHFURTTDS', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'PHFURTTDS', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PHFURTTDS', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PHFURTTDS', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PHFURTTDS', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PHFURTTDS', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PHFURTTDS', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--62--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('POGB', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'POGB', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('POGB', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('POGB', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('POGB', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('POGB', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('POGB', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--63--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PHDMG', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'PHDMG', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PHDMG', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PHDMG', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PHDMG', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PHDMG', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PHDMG', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--64--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROD', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'ROD', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROD', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROD', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROD', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROD', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROD', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--65--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROFT', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'ROFT', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROFT', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROFT', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROFT', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROFT', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROFT', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--66--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROG', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'ROG', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROG', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROG', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROG', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROG', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROG', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--67--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROSITFP', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'ROSITFP', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROSITFP', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROSITFP', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('ROSITFP', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROSITFP', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('ROSITFP', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--68--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTEFP', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'RTEFP', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTEFP', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTEFP', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTEFP', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTEFP', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTEFP', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--69--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTTSWD', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'RTTSWD', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTTSWD', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTTSWD', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTTSWD', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTTSWD', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTTSWD', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--70--


INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTPFP', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'RTPFP', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTPFP', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTPFP', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTPFP', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTPFP', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTPFP', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--71--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTRR', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'RTRR', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTRR', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTRR', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RTRR', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTRR', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RTRR', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--72--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RSE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'RSE', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RSE', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RSE', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('RSE', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RSE', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RSE', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--73--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SOSLP', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'SOSLP', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SOSLP', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SOSLP', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SOSLP', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SOSLP', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SOSLP', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--74--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SP', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'SP', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SP', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SP', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SP', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SP', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SP', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--75--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UHITOMAL', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'UHITOMAL', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UHITOMAL', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UHITOMAL', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('UHITOMAL', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UHITOMAL', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('UHITOMAL', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--76--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('USCOTR', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'USCOTR', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('USCOTR', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('USCOTR', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('USCOTR', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('USCOTR', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('USCOTR', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);

--77--

INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('VODCRBL', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'VODCRBL', 'default', NULL, 0, now(), NULL, 0, NULL);
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('VODCRBL', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('VODCRBL', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('VODCRBL', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('VODCRBL', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);
INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('VODCRBL', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);