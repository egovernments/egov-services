alter table egeis_recruitmentQuota add CONSTRAINT uk_egeis_recruitmentQuota_name_tenantid UNIQUE (name,tenantid);
