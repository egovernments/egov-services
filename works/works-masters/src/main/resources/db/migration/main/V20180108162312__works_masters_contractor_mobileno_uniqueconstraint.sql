ALTER TABLE egw_contractor add constraint uk_egw_contractor_mobileno_tenantid UNIQUE (tenantid,mobilenumber);
