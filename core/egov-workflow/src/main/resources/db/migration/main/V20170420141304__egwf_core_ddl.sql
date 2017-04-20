-----------------START--------------------
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
------------------END---------------------
------------------START------------------
CREATE TABLE eg_wf_action (
    id bigint NOT NULL,
    type character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(1024) NOT NULL,
    createdby bigint,
    createddate timestamp,
    lastModifiedBy bigint,
    lastModifiedDate timestamp,
    version bigint default 0,
    tenantid character varying(256) not null
);

CREATE SEQUENCE seq_eg_wf_action
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE eg_wf_action ADD CONSTRAINT eg_wf_action_name_type_key UNIQUE (name, type);
ALTER TABLE eg_wf_action ADD CONSTRAINT eg_wf_action_pkey PRIMARY KEY (id);
alter table eg_wf_action add constraint eg_wfaction_id_tenant_uk unique (id,tenantid);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_wf_matrix (
    id bigint NOT NULL,
    department character varying(30),
    objecttype character varying(30) NOT NULL,
    currentstate character varying(100),
    currentstatus character varying(30),
    pendingactions character varying(512),
    currentdesignation character varying(512),
    additionalrule character varying(50),
    nextstate character varying(100),
    nextaction character varying(100),
    nextdesignation character varying(512),
    nextstatus character varying(30),
    validactions character varying(512) NOT NULL,
    fromqty bigint,
    toqty bigint,
    fromdate date,
    todate date,
    version bigint default 0,
    tenantid character varying(256) not null
);
CREATE SEQUENCE SEQ_EG_WF_MATRIX
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE eg_wf_matrix ADD CONSTRAINT eg_wf_matrix_pkey PRIMARY KEY (id);
alter table eg_wf_matrix add constraint eg_wfmatrix_id_tenant_uk unique (id,tenantid);
-------------------END-------------------
------------------START------------------
CREATE TABLE eg_wf_state_history (
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
    tenantid character varying(256) not null
);
CREATE SEQUENCE seq_eg_wf_state_history
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE eg_wf_state_history ADD CONSTRAINT eg_wf_states_history_pkey PRIMARY KEY (id);
alter table eg_wf_state_history add constraint eg_wfstatehistory_id_tenant_uk unique (id,tenantid);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_wf_states (
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
    tenantid character varying(256) not null

);
CREATE SEQUENCE seq_eg_wf_states
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE eg_wf_states ADD CONSTRAINT eg_wf_states_pkey PRIMARY KEY (id);
alter table eg_wf_states add constraint eg_wfstates_id_tenant_uk unique (id,tenantid);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_wf_types (
    id bigint NOT NULL,
    module bigint NOT NULL,
    type character varying(100) NOT NULL,
    link character varying(255) NOT NULL,
    createdby bigint,
    createddate timestamp,
    lastmodifiedby bigint,
    lastmodifieddate timestamp,
    enabled boolean default CASE WHEN 'Y' THEN TRUE ELSE FALSE END,
    grouped boolean default CASE WHEN 'Y' THEN TRUE ELSE FALSE END,
    typefqn character varying(255) NOT NULL,
    displayname character varying(100) NOT NULL,
    version bigint default 0,
    tenantid character varying(256) not null
);
CREATE SEQUENCE seq_eg_wf_types
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE eg_wf_types ADD CONSTRAINT eg_wf_types_pkey PRIMARY KEY (id);
ALTER TABLE eg_wf_types ADD CONSTRAINT eg_wf_types_wf_type_key UNIQUE (type);
alter table eg_wf_types add constraint eg_wfytype_type_tenant_key unique (type, tenantid);
-------------------END-------------------