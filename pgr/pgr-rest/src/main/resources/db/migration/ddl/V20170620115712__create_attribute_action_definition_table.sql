CREATE TABLE attribute_action_definition (
    servicecode VARCHAR (256) NOT NULL,
    attributecode VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    tenantid character varying(256) NOT NULL,
    version bigint DEFAULT 0,
    createddate timestamp NOT NULL,
    lastmodifieddate timestamp ,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);

ALTER TABLE attribute_action_definition ADD CONSTRAINT attribute_action_definition_pkey
PRIMARY KEY(servicecode, attributecode, tenantid, name);

ALTER TABLE attribute_action_definition ADD CONSTRAINT attribute_action_definition_attribute_definition_fkey
FOREIGN KEY (servicecode, attributecode, tenantid) REFERENCES attribute_definition (servicecode, code, tenantid);

