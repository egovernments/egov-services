CREATE TABLE cs_group_constraint (
    id bigint,
    servicecode VARCHAR (256) NOT NULL,
    groupcode VARCHAR(50) NOT NULL,
    tenantid character varying(256) NOT NULL,
    constrainttype VARCHAR(100) NOT NULL,
    action VARCHAR(100) NOT NULL,
    role VARCHAR(100) NOT NULL,
    version bigint DEFAULT 0,
    createddate timestamp NOT NULL,
    lastmodifieddate timestamp,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);

ALTER TABLE cs_group_constraint ADD CONSTRAINT cs_group_constraint_pkey PRIMARY KEY(id);

ALTER TABLE cs_group_constraint ADD CONSTRAINT cs_group_constraint_group_definition_fkey
FOREIGN KEY (groupcode, servicecode, tenantid) REFERENCES cs_group_definition (code, servicecode, tenantid);

CREATE SEQUENCE seq_cs_group_constraint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


