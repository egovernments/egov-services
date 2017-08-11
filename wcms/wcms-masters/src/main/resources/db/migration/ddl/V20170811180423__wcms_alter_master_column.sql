--Document Application
ALTER TABLE egwtr_documenttype_applicationtype ADD column code character varying(20) ;

UPDATE egwtr_documenttype_applicationtype SET code='1';
ALTER TABLE egwtr_documenttype_applicationtype ALTER COLUMN code SET NOT NULL;


---Donation
ALTER TABLE egwtr_donation ADD column code character varying(20) ;

UPDATE egwtr_donation SET code='1';
ALTER TABLE egwtr_donation ALTER COLUMN code SET NOT NULL;

----property pipesize
ALTER TABLE egwtr_property_pipe_size ADD column code character varying(20);

UPDATE egwtr_property_pipe_size SET code='1';
ALTER TABLE egwtr_property_pipe_size ALTER COLUMN code SET NOT NULL;

---property usagetype
ALTER TABLE egwtr_property_usage_type ADD column code character varying(20) ;

UPDATE egwtr_property_usage_type SET code='1';
ALTER TABLE egwtr_property_usage_type ALTER COLUMN code SET NOT NULL;

---property category

ALTER TABLE egwtr_property_category_type ADD column code character varying(20);

UPDATE egwtr_property_category_type SET code='1';
ALTER TABLE egwtr_property_category_type ALTER COLUMN code SET NOT NULL;


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