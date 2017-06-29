CREATE TABLE egpgr_escalation_hierarchy (
    tenantid character varying(256) NOT NULL,
    fromposition bigint NOT NULL,
    toposition bigint NOT NULL,
    servicecode character varying(25) NOT NULL,
    createdby bigint,
    createddate timestamp without time zone,
 	lastmodifiedby bigint,
 	lastmodifieddate timestamp without time zone,
 	CONSTRAINT egpgr_escalation_hierarchy_un UNIQUE (fromposition, toposition, servicecode, tenantid)
);