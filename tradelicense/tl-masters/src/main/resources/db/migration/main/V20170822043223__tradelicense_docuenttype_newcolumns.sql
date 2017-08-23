ALTER TABLE egtl_mstr_document_type
ADD COlUMN categoryId bigint,
ADD COLUMN subCategoryId bigint,
DROP CONSTRAINT unq_tldocument_type,
ADD CONSTRAINT unq_tldocument_type_tenat_name UNIQUE ( name,tenantId);