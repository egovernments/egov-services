--Dropping constraint
ALTER TABLE egpgr_complainttype DROP CONSTRAINT unique_complainttype_code_tenant_name ;


-- Adding constraint
ALTER TABLE egpgr_complainttype ADD CONSTRAINT unique_complainttype_code_tenant UNIQUE (code, tenantid);

ALTER TABLE egpgr_complainttype ADD CONSTRAINT unique_complainttype_code_tenant_name_category UNIQUE (name, code, tenantid, category);


