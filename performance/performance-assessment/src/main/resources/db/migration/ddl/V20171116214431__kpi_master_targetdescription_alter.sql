ALTER TABLE egpa_kpi_master
  ADD COLUMN targetdescription character varying;
  
ALTER TABLE egpa_kpi_value
  ADD COLUMN actualdescription character varying;
