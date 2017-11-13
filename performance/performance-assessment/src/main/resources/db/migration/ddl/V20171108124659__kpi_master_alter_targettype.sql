ALTER TABLE egpa_kpi_master
  ADD COLUMN targettype character varying;
  
ALTER TABLE egpa_kpi_master ALTER COLUMN department type varchar using department::varchar;