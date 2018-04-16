-------------------------UPDATE UNIQUE KEY CONSTRAINT---------------------------------

--EGEIS_DESIGNATION TABLE

ALTER TABLE egeis_designation DROP CONSTRAINT if exists uk_egeis_designation_code;
ALTER TABLE egeis_designation ADD CONSTRAINT uk_egeis_designation_code UNIQUE (code,tenantid);

--EGEIS_DEPARTMENTDESIGNATION

ALTER TABLE egeis_departmentDesignation DROP CONSTRAINT if exists uk_egeis_departmentdesignation_departmentID_designationId;
ALTER TABLE egeis_departmentDesignation ADD CONSTRAINT uk_egeis_departmentdesignation_departmentID_designationId UNIQUE (departmentId, designationId,tenantid);

--EGEIS_POSITION

ALTER TABLE egeis_position DROP CONSTRAINT if exists uk_egeis_position_name;
ALTER TABLE egeis_position ADD CONSTRAINT uk_egeis_position_name UNIQUE (name,tenantid);

