ALTER TABLE egpa_kpi_value
DROP CONSTRAINT kpi_value_code_tenant_unq; 

UPDATE egpa_kpi_master_target SET finyear = '2017-18';