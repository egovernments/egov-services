ALTER TABLE egov_lcms_opinion ALTER COLUMN departmentname TYPE JSONB USING departmentname::jsonb;

ALTER TABLE egov_lcms_case RENAME stamp TO register;