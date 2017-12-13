ALTER TABLE public.egpa_kpi_value_documents
   ALTER COLUMN valueid TYPE character varying;
   
ALTER TABLE public.egpa_kpi_value_documents
   ALTER COLUMN documentcode DROP NOT NULL;
   
ALTER TABLE public.egpa_kpi_value_documents
   ALTER COLUMN kpicode DROP NOT NULL;
   
ALTER TABLE public.egpa_kpi_value_detail
   DROP CONSTRAINT kpi_value_attrib_unq;
