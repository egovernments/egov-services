------------------START------------------
CREATE TABLE cs_wf_state_history (
    id serial PRIMARY KEY ,
    state_id bigint NOT NULL,
    value character varying(255) NOT NULL,
    createdby bigint,
    createddate timestamp,
    lastmodifiedby bigint,
    lastmodifieddate timestamp,
    owner_pos bigint,
    owner_user bigint,
    dateinfo timestamp,
    extradateinfo timestamp,
    sendername character varying(100),
    comments character varying(1024),
    extrainfo character varying(1024),
    nextaction character varying(255),
    natureoftask varchar(100),
    INITIATOR_POS bigint,
    PREVIOUS_OWNER bigint,
    tenantid character varying(256) not null
);
CREATE SEQUENCE seq_cs_wf_state_history
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
-------------------END-------------------

------------------START------------------
CREATE TABLE cs_wf_states (
    id serial PRIMARY KEY,
    type character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp  NOT NULL,
    lastmodifiedby bigint NOT NULL,
    lastmodifieddate timestamp  NOT NULL,
    dateinfo timestamp ,
    extradateinfo timestamp ,
    comments character varying(1024),
    extrainfo character varying(1024),
    nextaction character varying(255),
    owner_pos bigint,
    owner_user bigint,
    sendername character varying(100),
    status numeric(1,0),
    version bigint,
    natureoftask varchar(100),
    INITIATOR_POS bigint,
    PREVIOUS_OWNER bigint,
    tenantid character varying(256) not null
);
CREATE SEQUENCE seq_cs_wf_states
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
-------------------END-------------------