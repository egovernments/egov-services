ALTER TABLE egwtr_waterconnection
	  DROP COLUMN categorytype;
	  
ALTER TABLE egwtr_usage_type
   ALTER COLUMN code TYPE character varying(50);