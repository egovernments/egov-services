CREATE TABLE eg_position
(
  name character varying(256) NOT NULL,
  id bigint NOT NULL,
  deptdesig bigint NOT NULL,
  createddate timestamp ,
  lastmodifieddate timestamp,
  createdby bigint,
  lastmodifiedby bigint,
  ispostoutsourced boolean,
  version bigint,
  CONSTRAINT eg_position_pkey PRIMARY KEY (id)
);


CREATE TABLE eg_department
(
  id numeric NOT NULL,
  name character varying(64) NOT NULL,
  createddate timestamp DEFAULT CURRENT_TIMESTAMP,
  code character varying(520),
  createdby bigint,
  lastmodifiedby bigint,
  lastmodifieddate timestamp ,
  version bigint,
  CONSTRAINT eg_department_pkey PRIMARY KEY (id),
  CONSTRAINT eg_department_dept_code_key UNIQUE (code),
  CONSTRAINT eg_department_dept_name_key UNIQUE (name)
);


CREATE TABLE eg_hierarchy_type
(
  id bigint NOT NULL,
  name character varying(128) NOT NULL,
  code character varying(50) NOT NULL,
  createddate timestamp ,
  lastmodifieddate timestamp ,
  createdby bigint,
  lastmodifiedby bigint,
  version bigint,
  localname character varying(256),
  CONSTRAINT eg_heirarchy_type_pkey PRIMARY KEY (id),
  CONSTRAINT eg_heirarchy_type_type_code_key UNIQUE (code),
  CONSTRAINT eg_heirarchy_type_type_name_key UNIQUE (name)
);



CREATE TABLE eg_boundary_type
(
  id bigint NOT NULL,
  hierarchy bigint NOT NULL,
  parent bigint,
  name character varying(64) NOT NULL,
  hierarchytype bigint NOT NULL,
  createddate timestamp ,
  lastmodifieddate timestamp ,
  createdby bigint,
  lastmodifiedby bigint,
  version bigint,
  localname character varying(64),
  code character varying (22),
  CONSTRAINT eg_boundary_type_pkey PRIMARY KEY (id),
  CONSTRAINT bndry_type_heirarchy_fk FOREIGN KEY (hierarchytype)
      REFERENCES eg_hierarchy_type (id),
  CONSTRAINT bndry_type_parent FOREIGN KEY (parent)
      REFERENCES eg_boundary_type (id) 
    
);




CREATE TABLE eg_boundary
(
  id bigint NOT NULL,
  boundarynum bigint,
  parent bigint,
  name character varying(512) NOT NULL,
  boundarytype bigint NOT NULL,
  localname character varying(256),
  bndry_name_old character varying(256),
  bndry_name_old_local character varying(256),
  fromdate timestamp ,
  todate timestamp ,
  bndryid bigint,
  longitude double precision,
  latitude double precision,
  materializedpath character varying(32),
  ishistory boolean,
  createddate timestamp ,
  lastmodifieddate timestamp ,
  createdby bigint,
  lastmodifiedby bigint,
  version bigint,
  code character varying(22),
  CONSTRAINT eg_boundary_pkey PRIMARY KEY (id),
  CONSTRAINT bndry_type_fk FOREIGN KEY (boundarytype)
      REFERENCES eg_boundary_type (id),
  CONSTRAINT parent_bndry_fk FOREIGN KEY (parent)
      REFERENCES eg_boundary (id) 
 
);


CREATE TABLE eg_user
(
  id bigint NOT NULL,
  title character varying(8),
  salutation character varying(5),
  dob timestamp ,
  locale character varying(16),
  username character varying(64) NOT NULL,
  password character varying(64) NOT NULL,
  pwdexpirydate timestamp DEFAULT CURRENT_TIMESTAMP,
  mobilenumber character varying(50),
  altcontactnumber character varying(50),
  emailid character varying(128),
  createddate timestamp ,
  lastmodifieddate timestamp ,
  createdby bigint,
  lastmodifiedby bigint,
  active boolean,
  name character varying(100),
  gender smallint,
  pan character varying(10),
  aadhaarnumber character varying(20),
  type character varying(50),
  version numeric DEFAULT 0,
  guardian character varying(100),
  guardianrelation character varying(32),
  signature bytea,
  accountlocked boolean DEFAULT false,
  CONSTRAINT eg_user_pkey PRIMARY KEY (id),
  CONSTRAINT eg_user_user_name_key UNIQUE (username)
);


CREATE TABLE egpgr_complainant
(
  id bigint NOT NULL,
  email character varying(100),
  mobile character varying(20),
  name character varying(150),
  userdetail bigint,
  address character varying(256),
  version bigint,
  CONSTRAINT pk_complainant PRIMARY KEY (id),
  CONSTRAINT fk_complainant_user FOREIGN KEY (userdetail)
  REFERENCES eg_user (id) 
   
);
CREATE TABLE egpgr_complainttype_category
(
  id numeric NOT NULL,
  name character varying(100),
  description character varying(250),
  version numeric DEFAULT 0,
  CONSTRAINT egpgr_complainttype_category_pkey PRIMARY KEY (id),
  CONSTRAINT egpgr_complainttype_category_name_key UNIQUE (name)
);  


CREATE TABLE egpgr_complainttype
(
  id numeric NOT NULL,
  name character varying(150),
  department numeric,
  version bigint,
  code character varying(20),
  isactive boolean,
  description character varying(100),
  createddate timestamp ,
  lastmodifieddate timestamp ,
  createdby bigint,
  lastmodifiedby bigint,
  slahours integer NOT NULL,
  hasfinancialimpact boolean,
  category numeric NOT NULL DEFAULT 0,
  attributes character varying (100),
  metadata boolean DEFAULT true,
  type character varying(50) DEFAULT 'realtime',
  keywords character varying(100) DEFAULT '',
  CONSTRAINT pk_pgr_complainttype_id PRIMARY KEY (id),
  CONSTRAINT fk_pgr_complainttype_deptid FOREIGN KEY (department)
  REFERENCES eg_department (id) ,CONSTRAINT uk_complainttype_code UNIQUE (code),
  CONSTRAINT uk_pgr_complainttype_name UNIQUE (name)
);

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
  status bigint,
  state_id bigint,
  escalation_date timestamp,
  version bigint,
  department bigint,
  citizenFeedback bigint,
  latlngAddress character varying(70),
  crossHierarchyId bigint,
  childlocation bigint,
  CONSTRAINT pk_complaint PRIMARY KEY (id),
  CONSTRAINT fk_comp_receiving_center FOREIGN KEY (receivingcenter)
      REFERENCES egpgr_receiving_center (id),
  CONSTRAINT fk_complaint_ FOREIGN KEY (complainant)
      REFERENCES egpgr_complainant (id),
  CONSTRAINT fk_complaint_boundary FOREIGN KEY (location)
      REFERENCES eg_boundary (id) ,
  CONSTRAINT fk_complaint_childlocation FOREIGN KEY (childlocation)
      REFERENCES eg_boundary (id) ,
  CONSTRAINT fk_complaint_complainttype FOREIGN KEY (complainttype)
      REFERENCES  egpgr_complainttype (id) ,
  CONSTRAINT fk_complaint_position FOREIGN KEY (assignee)
      REFERENCES eg_position (id),
  CONSTRAINT fk_complaint_status FOREIGN KEY (status)
      REFERENCES egpgr_complaintstatus (id) ,
  CONSTRAINT fk_department FOREIGN KEY (department)
      REFERENCES eg_department (id) ,
  CONSTRAINT uk_complaint_crn UNIQUE (crn)

);

CREATE SEQUENCE SEQ_EGPGR_COMPLAINT 
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;





