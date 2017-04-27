-- EGEIS_HRCONFIGURATION TABLE
ALTER TABLE egeis_hrconfiguration ADD CONSTRAINT uk_egeis_hrconfiguration_keyName_tenantId UNIQUE (keyName, tenantId);

-- EGEIS_HRSTATUS TABLE
ALTER TABLE egeis_hrstatus ADD CONSTRAINT uk_egeis_hrstatus_code_objectName_tenantId UNIQUE (objectName, code, tenantId);
