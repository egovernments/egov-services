ALTER TABLE egtl_mstr_document_type
DROP CONSTRAINT unq_tldocument_type_tenat_name,
ALTER COLUMN applicationType DROP NOT NULL;