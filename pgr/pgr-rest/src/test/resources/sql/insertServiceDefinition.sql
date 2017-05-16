INSERT INTO service_definition(code, tenantid, createddate, createdby) VALUES ('DMV66', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES
('WHISHETN', 'Y', 'singlevaluelist', 'Y', 'data type description', 1, 'What is the ticket/tag/DL number?',
'DMV66', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, active, tenantid, createddate, createdby) VALUES
('DMV66', 'WHISHETN', '123', 'Ford', 'N', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, active, tenantid, createddate, createdby) VALUES
('DMV66', 'WHISHETN', '124', 'Chrysler', 'Y', 'default', now(), 0);