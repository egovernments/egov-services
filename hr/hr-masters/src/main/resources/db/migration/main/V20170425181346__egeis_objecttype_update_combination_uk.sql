ALTER TABLE egeis_objecttype DROP CONSTRAINT if exists uk_egeis_objectType_type;
ALTER TABLE egeis_objecttype ADD CONSTRAINT uk_egeis_objectType_type UNIQUE (type,tenantid);