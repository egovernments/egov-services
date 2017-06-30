ALTER TABLE submission_attribute DROP CONSTRAINT submission_attribute_pk;
ALTER TABLE submission_attribute DROP COLUMN id;
ALTER TABLE submission_attribute ADD CONSTRAINT submission_attribute_pk PRIMARY KEY(crn, tenantid, code, key);
