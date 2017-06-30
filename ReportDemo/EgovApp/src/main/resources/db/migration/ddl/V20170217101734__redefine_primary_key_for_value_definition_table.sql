ALTER TABLE value_definition DROP CONSTRAINT value_definition_pkey;

ALTER TABLE value_definition ADD CONSTRAINT value_definition_pkey
PRIMARY KEY(servicecode, attributecode, key, tenantid);