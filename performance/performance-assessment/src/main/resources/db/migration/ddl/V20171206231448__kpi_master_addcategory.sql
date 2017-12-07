ALTER TABLE public.egpa_kpi_master
  ADD COLUMN category bigint;
  
  
CREATE TABLE egpa_kpi_category
(
  id bigint NOT NULL,
  code character varying NOT NULL,
  name character varying NOT NULL,
  CONSTRAINT egpa_kpi_category_pk PRIMARY KEY (id))
WITH (
  OIDS=FALSE
);