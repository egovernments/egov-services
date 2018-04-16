---------------------------- DROP FOREIGN KEYS ----------------------------

ALTER TABLE egeis_position DROP CONSTRAINT fk_egeis_position_deptdesigid;
ALTER TABLE egeis_positionhierarchy DROP CONSTRAINT fk_egeis_departmentdesignation_frompositionid;
ALTER TABLE egeis_positionhierarchy DROP CONSTRAINT fk_egeis_departmentdesignation_topositionid;
ALTER TABLE egeis_positionhierarchy DROP CONSTRAINT fk_egeis_departmentdesignation_objecttypeid;
ALTER TABLE egeis_departmentdesignation DROP CONSTRAINT fk_egeis_departmentdesignation_designationid;


--------------------------- UPDATE PRIMARY KEYS ---------------------------

-- EGEIS_DESIGNATION TABLE
ALTER TABLE egeis_designation DROP CONSTRAINT pk_egeis_designation;
ALTER TABLE egeis_designation ADD CONSTRAINT pk_egeis_designation PRIMARY KEY (id, tenantId);

-- EGEIS_DEPARTMENTDESIGNATION TABLE
ALTER TABLE egeis_departmentdesignation DROP CONSTRAINT pk_egeis_departmentdesignation;
ALTER TABLE egeis_departmentdesignation ADD CONSTRAINT pk_egeis_departmentdesignation PRIMARY KEY (id, tenantId);

-- EGEIS_EMPLOYEETYPE TABLE
ALTER TABLE egeis_employeetype DROP CONSTRAINT pk_egeis_employeetype;
ALTER TABLE egeis_employeetype ADD CONSTRAINT pk_egeis_employeetype PRIMARY KEY (id, tenantId);

-- EGEIS_GRADE TABLE
ALTER TABLE egeis_grade DROP CONSTRAINT pk_egeis_grade;
ALTER TABLE egeis_grade ADD CONSTRAINT pk_egeis_grade PRIMARY KEY (id, tenantId);

-- EGEIS_GROUP TABLE
ALTER TABLE egeis_group DROP CONSTRAINT pk_egeis_group;
ALTER TABLE egeis_group ADD CONSTRAINT pk_egeis_group PRIMARY KEY (id, tenantId);

-- EGEIS_RECRUITMENTMODE TABLE
ALTER TABLE egeis_recruitmentmode DROP CONSTRAINT pk_egeis_recruitmentmode;
ALTER TABLE egeis_recruitmentmode ADD CONSTRAINT pk_egeis_recruitmentmode PRIMARY KEY (id, tenantId);

-- EGEIS_RECRUITMENTQUOTA TABLE
ALTER TABLE egeis_recruitmentquota DROP CONSTRAINT pk_egeis_recruitmentquota;
ALTER TABLE egeis_recruitmentquota ADD CONSTRAINT pk_egeis_recruitmentquota PRIMARY KEY (id, tenantId);

-- EGEIS_RECRUITMENTTYPE TABLE
ALTER TABLE egeis_recruitmenttype DROP CONSTRAINT pk_egeis_recruitmenttype;
ALTER TABLE egeis_recruitmenttype ADD CONSTRAINT pk_egeis_recruitmenttype PRIMARY KEY (id, tenantId);

-- EGEIS_HRCONFIGURATION TABLE
ALTER TABLE egeis_hrconfiguration DROP CONSTRAINT pk_egeis_hrconfiguration;
ALTER TABLE egeis_hrconfiguration ADD CONSTRAINT pk_egeis_hrconfiguration PRIMARY KEY (id, tenantId);

-- EGEIS_HRCONFIGURATIONVALUES TABLE
ALTER TABLE egeis_hrconfigurationvalues DROP CONSTRAINT pk_egeis_hrconfigurationvalues;
ALTER TABLE egeis_hrconfigurationvalues ADD CONSTRAINT pk_egeis_hrconfigurationvalues PRIMARY KEY (id, tenantId);

-- EGEIS_HRSTATUS TABLE
ALTER TABLE egeis_hrstatus DROP CONSTRAINT pk_egeis_hrstatus;
ALTER TABLE egeis_hrstatus ADD CONSTRAINT pk_egeis_hrstatus PRIMARY KEY (id, tenantId);

-- EGEIS_OBJECTTYPE TABLE
ALTER TABLE egeis_objecttype DROP CONSTRAINT pk_egeis_objecttype;
ALTER TABLE egeis_objecttype ADD CONSTRAINT pk_egeis_objecttype PRIMARY KEY (id, tenantId);

-- EGEIS_POSITION TABLE
ALTER TABLE egeis_position DROP CONSTRAINT pk_egeis_position;
ALTER TABLE egeis_position ADD CONSTRAINT pk_egeis_position PRIMARY KEY (id, tenantId);

-- EGEIS_POSITIONHIERARCHY TABLE
ALTER TABLE egeis_positionhierarchy DROP CONSTRAINT pk_egeis_positionhierarchy;
ALTER TABLE egeis_positionhierarchy ADD CONSTRAINT pk_egeis_positionhierarchy PRIMARY KEY (id, tenantId);


-------------------------- RECREATE FOREIGN KEYS --------------------------

ALTER TABLE egeis_position ADD CONSTRAINT fk_egeis_position_deptdesigid FOREIGN KEY (deptdesigid, tenantId)
	REFERENCES egeis_departmentdesignation (id, tenantId);
ALTER TABLE egeis_positionhierarchy ADD CONSTRAINT fk_egeis_positionhierarchy_frompositionid FOREIGN KEY (frompositionid, tenantId)
	REFERENCES egeis_position (id, tenantId);
ALTER TABLE egeis_positionhierarchy ADD CONSTRAINT fk_egeis_positionhierarchy_topositionid FOREIGN KEY (topositionid, tenantId)
	REFERENCES egeis_position (id, tenantId);
ALTER TABLE egeis_positionhierarchy ADD CONSTRAINT fk_egeis_positionhierarchy_objecttypeid FOREIGN KEY (objecttypeid, tenantId)
	REFERENCES egeis_objecttype (id, tenantId);
ALTER TABLE egeis_departmentdesignation ADD CONSTRAINT fk_egeis_departmentdesignation_designationid FOREIGN KEY (designationid, tenantId)
	REFERENCES egeis_designation (id, tenantId);

