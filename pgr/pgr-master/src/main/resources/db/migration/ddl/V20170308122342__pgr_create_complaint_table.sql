CREATE TABLE egpgr_complainant
(
  id bigint NOT NULL,
  email character varying(100),
  mobile character varying(20),
  name character varying(150),
  userdetail bigint,
  address character varying(256),
  version bigint,
  tenantid character varying(256) not null,
  CONSTRAINT pk_complainant PRIMARY KEY (id),
  constraint uk_complainant_id_tenant unique (id,tenantid)
);

CREATE SEQUENCE seq_egpgr_complainant
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE egpgr_complainttype_category
(
  id bigint NOT NULL,
  name character varying(100),
  description character varying(250),
  version numeric DEFAULT 0,
  tenantid character varying(256) not null,
  CONSTRAINT egpgr_complainttype_category_pkey PRIMARY KEY (id),
  CONSTRAINT egpgr_complainttype_category_name_key UNIQUE (name),
  constraint unique_complainttypecategory_name_tenant unique (name, tenantid)
);

CREATE SEQUENCE seq_egpgr_complainttype_category;

CREATE TABLE egpgr_complainttype
(
  id bigint NOT NULL,
  name character varying(150),
  department numeric,
  version bigint,
  code character varying(20),
  isactive boolean,
  description character varying(100),
  createddate timestamp ,
  lastmodifieddate timestamp ,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  slahours integer NOT NULL,
  hasfinancialimpact boolean,
  category numeric NOT NULL DEFAULT 0,
  metadata boolean DEFAULT true,
  tenantid character varying(256) not null,
  type character varying(50) DEFAULT 'realtime',
  keywords character varying(100),
  CONSTRAINT pk_pgr_complainttype_id PRIMARY KEY (id),
  CONSTRAINT uk_complainttype_code UNIQUE (code),
  CONSTRAINT uk_pgr_complainttype_name UNIQUE (name),
  constraint unique_complainttype_code_tenant unique (code, tenantid)
);

CREATE SEQUENCE seq_egpgr_complainttype
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE egpgr_complaint
(
  id bigint NOT NULL,
  crn character varying(32) NOT NULL,
  createddate timestamp ,
  lastmodifieddate timestamp ,
  createdby bigint,
  lastmodifiedby bigint,
  complainttype bigint NOT NULL,
  complainant bigint NOT NULL,
  assignee bigint,
  location bigint,
  details character varying(500) NOT NULL,
  landmarkdetails character varying(200),
  receivingmode smallint,
  receivingcenter bigint,
  lat double precision,
  lng double precision,
  status character varying(25),
  state_id bigint,
  escalation_date timestamp,
  version bigint,
  department bigint,
  citizenFeedback bigint,
  latlngAddress character varying(70),
  crossHierarchyId bigint,
  childlocation bigint,
  tenantid character varying(256) not null,
  CONSTRAINT pk_complaint PRIMARY KEY (id),
  CONSTRAINT fk_comp_receiving_center FOREIGN KEY (receivingcenter)
      REFERENCES egpgr_receiving_center (id),
  CONSTRAINT fk_complaint_ FOREIGN KEY (complainant)
      REFERENCES egpgr_complainant (id),
  CONSTRAINT fk_complaint_complainttype FOREIGN KEY (complainttype)
      REFERENCES  egpgr_complainttype (id) ,
  CONSTRAINT uk_complaint_crn UNIQUE (crn),
 CONSTRAINT uk_complaint_crn_tenant unique (crn,tenantid)

);

CREATE SEQUENCE SEQ_EGPGR_COMPLAINT 
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;