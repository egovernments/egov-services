CREATE SEQUENCE seq_eg_action;
CREATE TABLE eg_action (
        id serial primary key,
        name character varying(100) NOT NULL,
        url character varying(150),
        servicecode character varying(50),
        queryparams character varying(150),
        parentmodule bigint NOT NULL,
        ordernumber bigint,
        displayname character varying(80),
        enabled boolean,
        contextroot character varying(32),
        version numeric DEFAULT 0,
        createdby numeric DEFAULT 1,
        createddate timestamp DEFAULT now(),
        lastmodifiedby numeric DEFAULT 1,
        lastmodifieddate timestamp DEFAULT now(),
        tenantid character varying(50) NOT NULL
    );

CREATE TABLE eg_roleaction (
        roleid bigint NOT NULL,
        actionid bigint NOT NULL
    );

