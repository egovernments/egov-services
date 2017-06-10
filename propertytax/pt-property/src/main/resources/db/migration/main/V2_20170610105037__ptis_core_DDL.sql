-- - master tables

CREATE TABLE egpt_department_master(
    id bigint NOT NULL,
    tenantId character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egpt_department_master
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE seq_egpt_department_master OWNED BY egpt_department_master.id;
ALTER TABLE ONLY egpt_department_master ALTER COLUMN id SET DEFAULT nextval('seq_egpt_department_master'::regclass);
ALTER TABLE ONLY egpt_department_master
ADD CONSTRAINT egpt_department_master_pkey PRIMARY KEY(id);


CREATE TABLE egpt_floortype_master(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);


CREATE SEQUENCE seq_egpt_floortype_master
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE seq_egpt_floortype_master OWNED BY egpt_floortype_master.id;

ALTER TABLE ONLY egpt_floortype_master ALTER COLUMN id SET DEFAULT nextval('seq_egpt_floortype_master'::regclass);

ALTER TABLE ONLY egpt_floortype_master
ADD CONSTRAINT egpt_floortype_master_pkey PRIMARY KEY(id);


CREATE TABLE egpt_occuapancy_master(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint

);


CREATE SEQUENCE seq_egpt_occuapancy_master
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE seq_egpt_occuapancy_master OWNED BY egpt_occuapancy_master.id;

ALTER TABLE ONLY egpt_occuapancy_master ALTER COLUMN id SET DEFAULT nextval('seq_egpt_occuapancy_master'::regclass);

ALTER TABLE ONLY egpt_occuapancy_master
ADD CONSTRAINT egpt_occuapancy_master_pkey PRIMARY KEY(id);


CREATE TABLE egpt_propertytypes_master(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
   data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint

);


CREATE SEQUENCE seq_egpt_propertytypes_master
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE seq_egpt_propertytypes_master OWNED BY egpt_propertytypes_master.id;

ALTER TABLE ONLY egpt_propertytypes_master ALTER COLUMN id SET DEFAULT nextval('seq_egpt_propertytypes_master'::regclass);

ALTER TABLE ONLY egpt_propertytypes_master
ADD CONSTRAINT egpt_propertytypes_master_pkey PRIMARY KEY(id);


CREATE TABLE egpt_rooftypes_master(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint

);


CREATE SEQUENCE seq_egpt_rooftypes_master
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE seq_egpt_rooftypes_master OWNED BY egpt_rooftypes_master.id;

ALTER TABLE ONLY egpt_rooftypes_master ALTER COLUMN id SET DEFAULT nextval('seq_egpt_rooftypes_master'::regclass);

ALTER TABLE ONLY egpt_rooftypes_master
ADD CONSTRAINT egpt_rooftypes_master_pkey PRIMARY KEY(id);

CREATE TABLE egpt_structureclasses_master(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
   data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint

);


CREATE SEQUENCE seq_egpt_structureclasses_master
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE seq_egpt_structureclasses_master OWNED BY egpt_structureclasses_master.id;

ALTER TABLE ONLY egpt_structureclasses_master ALTER COLUMN id SET DEFAULT nextval('seq_egpt_structureclasses_master'::regclass);

ALTER TABLE ONLY egpt_structureclasses_master
ADD CONSTRAINT egpt_structureclasses_master_pkey PRIMARY KEY(id);

CREATE TABLE egpt_usage_master(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint

);


CREATE SEQUENCE seq_egpt_usage_master
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE seq_egpt_usage_master OWNED BY egpt_usage_master.id;

ALTER TABLE ONLY egpt_usage_master ALTER COLUMN id SET DEFAULT nextval('seq_egpt_usage_master'::regclass);

ALTER TABLE ONLY egpt_usage_master
ADD CONSTRAINT egpt_usage_master_pkey PRIMARY KEY(id);


CREATE TABLE egpt_walltypes_master(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint

);


CREATE SEQUENCE seq_egpt_walltypes_master
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE seq_egpt_walltypes_master OWNED BY egpt_walltypes_master.id;

ALTER TABLE ONLY egpt_walltypes_master ALTER COLUMN id SET DEFAULT nextval('seq_egpt_walltypes_master'::regclass);

ALTER TABLE ONLY egpt_walltypes_master
ADD CONSTRAINT egpt_walltypes_master_pkey PRIMARY KEY(id);

CREATE TABLE egpt_woodtypes_master(
    id bigint NOT NULL,
    tenantid character varying NOT NULL,
    code character varying NOT NULL,
    data jsonb NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint

);


CREATE SEQUENCE seq_egpt_woodtypes_master
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE seq_egpt_woodtypes_master OWNED BY egpt_woodtypes_master.id;

ALTER TABLE ONLY egpt_woodtypes_master ALTER COLUMN id SET DEFAULT nextval('seq_egpt_woodtypes_master'::regclass);

ALTER TABLE ONLY egpt_woodtypes_master
ADD CONSTRAINT egpt_woodtypes_master_pkey PRIMARY KEY(id);
