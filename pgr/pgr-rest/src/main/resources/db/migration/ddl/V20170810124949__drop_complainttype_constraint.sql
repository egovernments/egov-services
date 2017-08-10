
ALTER TABLE egpgr_complainttype DROP constraint uk_complainttype_code ;

ALTER TABLE egpgr_complainttype DROP constraint uk_pgr_complainttype_name ;

ALTER TABLE egpgr_complainttype DROP constraint unique_complainttype_code_tenant ;

ALTER TABLE egpgr_complainttype ADD CONSTRAINT unique_complainttype_code_tenant_name UNIQUE (name, code, tenantid);

