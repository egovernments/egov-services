ALTER TABLE egtl_mstr_document_type
ADD COlUMN categoryId bigint,
ADD COLUMN subCategoryId bigint,
ALTER COLUMN applicationType DROP NOT NULL;