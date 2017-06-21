-----------------START--------------------
drop table if exists egpgr_router;
CREATE TABLE egpgr_router (
    id bigint NOT NULL,
    complainttypeid numeric,
    "position" bigint,
    bndryid bigint,
    version bigint default 0,
    createdby bigint,
    createddate date,
    lastmodifiedby bigint,
    lastmodifieddate date,
    tenantid character varying(256) not null
);

DROP SEQUENCE IF EXISTS seq_egpgr_router;
CREATE SEQUENCE seq_egpgr_router
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE egpgr_router
    ADD CONSTRAINT pk_pgr_router_id PRIMARY KEY (id);
ALTER TABLE egpgr_router
    ADD constraint uk_pgr_router_id_tenant unique (id,tenantid);

    
CREATE INDEX idx_pgr_router_complainttypeid
  ON egpgr_router
  (complainttypeid);

CREATE INDEX idx_pgr_router_bndryid
  ON egpgr_router
  (bndryid);

CREATE INDEX idx_pgr_router_position
  ON egpgr_router
  ("position");
-------------------END-------------------
 
------------------START------------------
CREATE TABLE cs_wf_state_history (
    id bigint NOT NULL,
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
    tenantid character varying(256) not null,
    previous_owner bigint
);
CREATE SEQUENCE seq_cs_wf_state_history
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE cs_wf_state_history ADD CONSTRAINT cs_wf_states_history_pkey PRIMARY KEY (id);
alter table cs_wf_state_history add constraint cs_wfstatehistory_id_tenant_uk unique (id,tenantid);
-------------------END-------------------

------------------START------------------
CREATE TABLE cs_wf_states (
    id bigint NOT NULL,
    type character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp NOT NULL,
    lastmodifiedby bigint NOT NULL,
    lastmodifieddate timestamp NOT NULL,
    dateinfo timestamp,
    extradateinfo timestamp,
    comments character varying(1024),
    extrainfo character varying(1024),
    nextaction character varying(255),
    owner_pos bigint,
    owner_user bigint,
    sendername character varying(100),
    status numeric(1,0),
    version bigint default 0,
    natureoftask varchar(100),
    INITIATOR_POS bigint,
    tenantid character varying(256) not null,
    previous_owner bigint
);
CREATE SEQUENCE seq_cs_wf_states
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE cs_wf_states ADD CONSTRAINT cs_wf_states_pkey PRIMARY KEY (id);
alter table cs_wf_states add constraint cs_wfstates_id_tenant_uk unique (id,tenantid);
-------------------END-------------------
