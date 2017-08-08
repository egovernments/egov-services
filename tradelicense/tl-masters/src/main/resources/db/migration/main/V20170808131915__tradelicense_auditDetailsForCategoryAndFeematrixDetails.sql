------------------CATEGORY DETAIL AUDIT DETAILS------------------
ALTER TABLE ONLY egtl_category_details ADD COLUMN createdBy character varying;
ALTER TABLE ONLY egtl_category_details ADD COLUMN lastModifiedBy character varying;
ALTER TABLE ONLY egtl_category_details ADD COLUMN createdTime bigint;
ALTER TABLE ONLY egtl_category_details ADD COLUMN lastModifiedTime bigint;

------------------FEEMATRIX DETAIL AUDIT DETAILS------------------
ALTER TABLE ONLY egtl_fee_matrix_details ADD COLUMN createdBy character varying;
ALTER TABLE ONLY egtl_fee_matrix_details ADD COLUMN lastModifiedBy character varying;
ALTER TABLE ONLY egtl_fee_matrix_details ADD COLUMN createdTime bigint;
ALTER TABLE ONLY egtl_fee_matrix_details ADD COLUMN lastModifiedTime bigint;