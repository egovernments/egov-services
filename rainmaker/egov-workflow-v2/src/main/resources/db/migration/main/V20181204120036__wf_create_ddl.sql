CREATE TABLE eg_wf_processinstance(

    id character varying(64),
    tenantid character varying(128),
    businessService character varying(128),
    businessId character varying(128),
    action character varying(128),
    status character varying(128),
    comment character varying(128),
    assigner character varying(128),
    assignee character varying(128),
    sla bigint,
    previousStatus character varying(128),
    createdBy character varying(64),
    lastModifiedBy character varying(64),
    createdTime bigint,
    lastModifiedTime bigint,
    CONSTRAINT uk_eg_wf_processinstance UNIQUE (id)
);


CREATE TABLE eg_wf_Document(
    id character varying(64),
    tenantId character varying(64),
    documentType character varying(64),
    documentUid character varying(64),
    filestoreid character varying(64),
    processinstanceid character varying(64),
    active boolean,
    createdBy character varying(64),
    lastModifiedBy character varying(64),
    createdTime bigint,
    lastModifiedTime bigint,

    CONSTRAINT uk_eg_wf_Document PRIMARY KEY (id),
    CONSTRAINT fk_eg_wf_Document FOREIGN KEY (processinstanceid) REFERENCES eg_wf_processinstance (id)

    ON UPDATE CASCADE
    ON DELETE CASCADE
);