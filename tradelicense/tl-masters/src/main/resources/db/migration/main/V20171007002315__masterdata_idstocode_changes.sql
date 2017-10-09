-- CategoryId to category in category details
ALTER TABLE egtl_category_details
RENAME categoryid TO category;

ALTER TABLE egtl_category_details DROP constraint fk_egtl_category_details_categoryid;

ALTER TABLE egtl_category_details
ALTER COLUMN category TYPE character varying(100) USING category::character varying;

ALTER TABLE egtl_category_details
DROP CONSTRAINT unq_tlcategory_details;

ALTER TABLE egtl_category_details
ADD CONSTRAINT unq_tlcategory_details UNIQUE (tenantid,category, feetype, ratetype);


-- uomId to uom in category details
ALTER TABLE egtl_category_details
RENAME uomId TO uom;

ALTER TABLE egtl_category_details DROP constraint fk_egtl_category_details_uomid;

ALTER TABLE egtl_category_details
ALTER COLUMN uom TYPE character varying(100) USING uom::character varying;

-- parentId to parent in category master
ALTER TABLE egtl_mstr_category
RENAME parentId TO parent;

ALTER TABLE egtl_mstr_category DROP constraint fk_egtl_mstr_category_parentid;

ALTER TABLE egtl_mstr_category
ALTER COLUMN parent TYPE character varying(100) USING parent::character varying;

-- categoryId to category in FeeMatrix
ALTER TABLE egtl_mstr_fee_matrix
RENAME categoryid TO category;

ALTER TABLE egtl_mstr_fee_matrix DROP constraint fk_egtl_mstr_fee_matrix_categoryid;

ALTER TABLE egtl_mstr_fee_matrix
ALTER COLUMN category TYPE character varying(100) USING category::character varying;

-- subCategoryId to subCategory in FeeMatrix
ALTER TABLE egtl_mstr_fee_matrix
RENAME subcategoryid TO subcategory;

ALTER TABLE egtl_mstr_fee_matrix DROP constraint fk_egtl_mstr_fee_matrix_subcategoryid;

ALTER TABLE egtl_mstr_fee_matrix
ALTER COLUMN subcategory TYPE character varying(100) USING subcategory::character varying;

-- categoryId to category in DocumentType
ALTER TABLE egtl_mstr_document_type
RENAME categoryid TO category;

ALTER TABLE egtl_mstr_document_type
ALTER COLUMN category TYPE character varying(100) USING category::character varying;

-- subCategoryId to subCategory in DocumentType
ALTER TABLE egtl_mstr_document_type
RENAME subcategoryid TO subcategory;

ALTER TABLE egtl_mstr_document_type
ALTER COLUMN subcategory TYPE character varying(100) USING subcategory::character varying;


-- updating the category parent with corresponding category code
UPDATE egtl_mstr_category category1 SET parent = (select category2.code FROM egtl_mstr_category category2 where category2.id::varchar  = category1.parent);

-- updating the categoryDetails category with corresponding subcategory code
UPDATE egtl_category_details details SET category = (select category.code FROM egtl_mstr_category category where category.id::varchar  = details.category);

-- updating the categoryDetails uom with corresponding uom code
UPDATE egtl_category_details details SET uom = (select uom.code FROM egtl_mstr_uom uom where uom.id::varchar  = details.uom);

-- updating the feeMatrix category with corresponding category code
UPDATE egtl_mstr_fee_matrix feematrix SET category = (select category.code FROM egtl_mstr_category category where category.id::varchar  = feematrix.category);

-- updating the feeMatrix subCategory with corresponding subCategory code
UPDATE egtl_mstr_fee_matrix feematrix SET subcategory = (select category.code FROM egtl_mstr_category category where category.id::varchar  = feematrix.subcategory);

-- updating the documentType category with corresponding category code
UPDATE egtl_mstr_document_type documenttype SET category = (select category.code FROM egtl_mstr_category category where category.id::varchar  = documenttype.category);

-- updating the documentType subCategory with corresponding subCategory code
UPDATE egtl_mstr_document_type documenttype SET subcategory = (select category.code FROM egtl_mstr_category category where category.id::varchar  = documenttype.subcategory);

