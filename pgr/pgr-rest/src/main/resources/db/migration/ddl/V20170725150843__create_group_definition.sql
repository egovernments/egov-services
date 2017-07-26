CREATE TABLE cs_group_definition (
    servicecode VARCHAR(256) NOT NULL,
    code VARCHAR(50) NOT NULL,
    tenantid character varying(256) NOT NULL,
    name VARCHAR(200) NOT NULL,
    version bigint DEFAULT 0,
    createddate timestamp NOT NULL,
    lastmodifieddate timestamp ,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);

ALTER TABLE cs_group_definition ADD CONSTRAINT cs_group_definition_pkey PRIMARY KEY(code, servicecode, tenantid);

ALTER TABLE cs_group_definition ADD CONSTRAINT cs_group_definition_service_definition_fkey
FOREIGN KEY (servicecode, tenantid) REFERENCES service_definition (code, tenantid);

