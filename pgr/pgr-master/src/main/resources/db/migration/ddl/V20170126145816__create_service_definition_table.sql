CREATE TABLE service_definition (
    code character varying(20),
    tenantid character varying(256) NOT NULL,
    version bigint DEFAULT 0,
    createddate timestamp NOT NULL,
    lastmodifieddate timestamp ,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);

ALTER TABLE service_definition ADD CONSTRAINT service_definition_pkey
PRIMARY KEY(code, tenantid);

CREATE TABLE attribute_definition (
    code VARCHAR(50) NOT NULL,
    variable CHAR NOT NULL DEFAULT 'N',
    datatype VARCHAR(100) NOT NULL,
    required CHAR NOT NULL DEFAULT 'N',
    datatypedescription VARCHAR(200),
    ordernum INTEGER,
    description VARCHAR(300),
    servicecode VARCHAR (256) NOT NULL,
    tenantid character varying(256) NOT NULL,
    version bigint DEFAULT 0,
    createddate timestamp NOT NULL,
    lastmodifieddate timestamp ,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);

ALTER TABLE attribute_definition ADD CONSTRAINT attribute_definition_pkey
PRIMARY KEY(servicecode, code, tenantid);

ALTER TABLE attribute_definition ADD CONSTRAINT attribute_definition_fkey
FOREIGN KEY (servicecode, tenantid) REFERENCES service_definition (code, tenantid);

CREATE TABLE value_definition (
    servicecode VARCHAR (256) NOT NULL,
    attributecode VARCHAR(50) NOT NULL,
    key VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    tenantid character varying(256) NOT NULL,
    version bigint DEFAULT 0,
    createddate timestamp NOT NULL,
    lastmodifieddate timestamp ,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);

ALTER TABLE value_definition ADD CONSTRAINT value_definition_pkey
PRIMARY KEY(attributecode, key, tenantid);

ALTER TABLE value_definition ADD CONSTRAINT value_definition_attribute_definition_fkey
FOREIGN KEY (servicecode, attributecode, tenantid) REFERENCES attribute_definition (servicecode, code, tenantid);