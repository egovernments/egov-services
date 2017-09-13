ALTER TABLE egwtr_document_type
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_documenttype_applicationtype
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_donation
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_gapcode
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_meter_water_rates
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_slab
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_metercost
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_meterstatus
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_non_meter_water_rates
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_pipesize
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_servicecharge
   ALTER COLUMN tenantid TYPE character varying(128); 


ALTER TABLE egwtr_servicecharge_details
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_storage_reservoir
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_supply_type
   ALTER COLUMN tenantid TYPE character varying(128); 


ALTER TABLE egwtr_treatment_plant
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_usage_type
   ALTER COLUMN tenantid TYPE character varying(128); 

ALTER TABLE egwtr_water_source_type
   ALTER COLUMN tenantid TYPE character varying(128); 