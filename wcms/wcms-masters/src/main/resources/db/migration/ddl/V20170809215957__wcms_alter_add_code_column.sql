--Document Application
ALTER TABLE egwtr_documenttype_applicationtype ADD column code character varying(20) NOT NULL;

ALTER TABLE egwtr_documenttype_applicationtype
ADD CONSTRAINT unique_docapp_code UNIQUE (code, tenantid);

---Donation
ALTER TABLE egwtr_donation ADD column code character varying(20) NOT NULL;

ALTER TABLE egwtr_donation
ADD CONSTRAINT unq_donation_code UNIQUE (code, tenantid);

----property pipesize
ALTER TABLE egwtr_property_pipe_size ADD column code character varying(20) NOT NULL;

ALTER TABLE egwtr_property_pipe_size
ADD CONSTRAINT unq_proppipesize_code UNIQUE (code, tenantid);


---property usagetype
ALTER TABLE egwtr_property_usage_type ADD column code character varying(20) NOT NULL;

ALTER TABLE egwtr_property_usage_type
ADD CONSTRAINT unq_propusage_code UNIQUE (code, tenantid);

---property category

ALTER TABLE egwtr_property_category_type ADD column code character varying(20) NOT NULL;

ALTER TABLE egwtr_property_category_type
ADD CONSTRAINT unq_propcategory_code UNIQUE (code, tenantid);

--document type-application type

ALTER TABLE egwtr_documenttype_applicationtype
    ALTER COLUMN id TYPE bigint ;
    
ALTER TABLE egwtr_documenttype_applicationtype ALTER COLUMN id SET NOT NULL;

ALTER TABLE egwtr_documenttype_applicationtype
ALTER id DROP DEFAULT;

----Document type
ALTER TABLE egwtr_document_type
    ALTER COLUMN id TYPE bigint ;
    
ALTER TABLE egwtr_document_type ALTER COLUMN id SET NOT NULL;

ALTER TABLE egwtr_document_type
ALTER id DROP DEFAULT;