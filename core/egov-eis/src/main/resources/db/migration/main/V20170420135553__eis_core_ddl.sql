------------------START-------------
CREATE TABLE eg_department
(
  id numeric NOT NULL,
  name character varying(64) NOT NULL,
  createddate timestamp DEFAULT CURRENT_TIMESTAMP,
  code character varying(520),
  createdby bigint,
  lastmodifiedby bigint,
  lastmodifieddate timestamp ,
  tenantid character varying(256) not null,
  version bigint,
  CONSTRAINT eg_department_pkey PRIMARY KEY (id),
  CONSTRAINT eg_department_dept_code_key UNIQUE (code),
  CONSTRAINT eg_department_dept_name_key UNIQUE (name),
  CONSTRAINT eg_department_code_tenant_key unique (code,tenantid)
);

---------------END------------------

------------------START------------------
CREATE TABLE eg_designation (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    description character varying(1024),
    chartofaccounts bigint,
    code varchar(10),
    version bigint,
    tenantid character varying(256) not null,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);

CREATE SEQUENCE seq_eg_designation
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE eg_designation ADD CONSTRAINT eg_designation_designation_name_key UNIQUE (name);
ALTER TABLE eg_designation ADD CONSTRAINT eg_designation_pkey PRIMARY KEY (id);
alter table eg_designation add constraint eg_designation_name_tenant_key unique (name,tenantid);
-------------------END-------------------

----------------START--------------
CREATE TABLE egeis_deptdesig (
    id bigint NOT NULL,
    designation integer NOT NULL,
    department integer NOT NULL,
    outsourcedposts integer,
    sanctionedposts integer,
    version bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    tenantid character varying(256) not null
);
CREATE SEQUENCE seq_egeis_deptdesig
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE  egeis_deptdesig
    ADD CONSTRAINT egeis_deptdesig_desig_id_dept_id_tenant_key UNIQUE (designation, department,tenantid);
ALTER TABLE  egeis_deptdesig
    ADD CONSTRAINT egeis_deptdesig_pkey PRIMARY KEY (id);
ALTER TABLE  egeis_deptdesig
    ADD CONSTRAINT fk_egeis_deptdesig_departmnt FOREIGN KEY (department) REFERENCES eg_department(id);
ALTER TABLE  egeis_deptdesig
    ADD CONSTRAINT fk_egeis_deptdesig_design FOREIGN KEY (designation) REFERENCES eg_designation(id);
 
-----------------END--------------- 

    -----------------START--------------
CREATE TABLE egeis_employee (
    id numeric NOT NULL,
    code character varying(256),
    dateofappointment date,
    dateofretirement date,
    employeestatus character varying(16),
    employeetype bigint,
    version numeric DEFAULT 0,
    tenantid character varying(256) not null
);
ALTER TABLE egeis_employee
    ADD CONSTRAINT pk_egeis_employee_id PRIMARY KEY (id);
alter table egeis_employee add constraint egeis_employee_code_tenant_uk unique (code,tenantid);
--------------------------------_END--------------------

-------------START----------------
CREATE TABLE egeis_employeetype (
    id bigint NOT NULL,
    name character varying(256),
    version bigint,
    lastmodifieddate timestamp without time zone,
    createddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    chartofaccounts bigint,
    tenantid character varying(256) not null
);
CREATE SEQUENCE seq_egeis_employeetype
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE egeis_employeetype
    ADD CONSTRAINT pk_egeis_employeetype_id PRIMARY KEY (id);
alter table egeis_employeetype add constraint egeis_employeetype_name_tenant_uk unique (name,tenantid);
    
-----------------_END--------------

-----------------START--------------
CREATE TABLE egeis_grade_mstr (
    grade_id bigint NOT NULL,
    grade_value character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    age bigint,
    order_no integer,
    tenantid character varying(256) not null
);
CREATE SEQUENCE egpims_grade_mstr_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE egeis_grade_mstr
    ADD CONSTRAINT egeis_grade_mstr_grade_value_key UNIQUE (grade_value);
ALTER TABLE egeis_grade_mstr
    ADD CONSTRAINT egeis_grade_mstr_pkey PRIMARY KEY (grade_id);
alter table egeis_grade_mstr add constraint egeis_grademstr_gradeval_tenant_uk unique (grade_value,tenantid);
------------------END------------

------------------START------------------
CREATE TABLE eg_position (
    name character varying(256) NOT NULL,
    id bigint NOT NULL,
    deptdesig bigint NOT NULL,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    ispostoutsourced boolean,
    tenantid character varying(256) not null,
    version bigint
);
CREATE SEQUENCE seq_eg_position
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE eg_position ADD CONSTRAINT eg_position_pkey PRIMARY KEY (id);
alter table eg_position add constraint eg_position_id_tenant_key unique (id,tenantid);
-------------------END-------------------

-----------------__START-------------------
CREATE TABLE egeis_assignment (
    id bigint NOT NULL,
    fund bigint,
    function bigint,
    designation bigint,
    functionary bigint,
    department bigint,
    "position" bigint,
    grade bigint,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    createddate timestamp without time zone,
    createdby bigint,
    fromdate date,
    todate date,
    version bigint,
    employee bigint,
    isprimary boolean,
    tenantid character varying(256) not null
);

CREATE SEQUENCE seq_egeis_assignment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE egeis_assignment
    ADD CONSTRAINT eg_emp_assignment_pkey PRIMARY KEY (id);
ALTER TABLE egeis_assignment
    ADD CONSTRAINT des_fk FOREIGN KEY (designation) REFERENCES eg_designation(id);
CREATE INDEX index_emp_assgn_designationid ON egeis_assignment USING btree (designation);
CREATE INDEX index_emp_assgn_main_dept ON egeis_assignment USING btree (department);
CREATE INDEX index_emp_assgn_position_id ON egeis_assignment USING btree ("position");

ALTER TABLE egeis_assignment
    ADD CONSTRAINT main_de FOREIGN KEY (department) REFERENCES eg_department(id);
ALTER TABLE egeis_assignment
    ADD CONSTRAINT pos_id FOREIGN KEY ("position") REFERENCES eg_position(id);
alter table egeis_assignment add constraint egeis_assignment_id_tenant_uk unique (id,tenantid);    

--------------END-----------------

----------------_START-----------------
CREATE TABLE egeis_employee_hod (
    id bigint NOT NULL,
    assignment bigint,
    hod bigint,
    version bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    tenantid character varying(256) not null
);
CREATE SEQUENCE seq_egeis_employee_hod
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE egeis_employee_hod
    ADD CONSTRAINT eg_employee_dept_pkey PRIMARY KEY (id);  
CREATE INDEX index_emp_dept_assignment_id ON egeis_employee_hod USING btree (assignment);
CREATE INDEX index_emp_dept_hod ON egeis_employee_hod USING btree (hod);
ALTER TABLE egeis_employee_hod
    ADD CONSTRAINT ass_id FOREIGN KEY (assignment) REFERENCES egeis_assignment(id);
ALTER TABLE egeis_employee_hod
    ADD CONSTRAINT hod_id FOREIGN KEY (hod) REFERENCES eg_department(id);  
alter table egeis_employee_hod add constraint egeis_employeedhod_id_tenant_uk unique (id,tenantid);
--------------END-----------------------

------------------------START------------------
CREATE TABLE egeis_jurisdiction (
    id bigint NOT NULL,
    employee bigint NOT NULL,
    boundarytype bigint NOT NULL,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    version bigint,
    boundary bigint,
    tenantid character varying(256) not null
);
CREATE SEQUENCE seq_egeis_jurisdiction
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ALTER TABLE egeis_jurisdiction
    ADD CONSTRAINT pk_egeis_jurisdiction_id PRIMARY KEY (id);   
ALTER TABLE egeis_jurisdiction
    ADD CONSTRAINT fk_egeis_jurisdiction_employee FOREIGN KEY (employee) REFERENCES egeis_employee(id);
alter table egeis_jurisdiction add constraint egeis_jurisdiction_id_tenant_uk unique (id,tenantid);
------------_END------------------

-----------------__START-------------------
CREATE TABLE egeis_position_hierarchy (
    id bigint NOT NULL,
    position_from bigint,
    position_to bigint,
    object_type_id bigint,
    object_sub_type character varying(512),
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    createddate timestamp without time zone,
    createdby bigint,
    version bigint,
    tenantid character varying(256) not null
);
CREATE SEQUENCE seq_egeis_position_hierarchy
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE egeis_position_hierarchy
    ADD CONSTRAINT eg_position_hir_pkey PRIMARY KEY (id);
ALTER TABLE egeis_position_hierarchy
    ADD CONSTRAINT egeis_position_hir_position_frm_position_to_object_type_subtype UNIQUE (position_from, position_to, object_type_id, object_sub_type, tenantid);
------------_END------------------
