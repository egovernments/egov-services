-------------------category details tenantid-------------------
ALTER TABLE ONLY egtl_category_details ADD COLUMN tenantId character varying(128);

UPDATE egtl_category_details details SET tenantId = (select category.tenantid FROM egtl_mstr_category category where  category.id= details.categoryId);
   
ALTER TABLE egtl_category_details ALTER column tenantId SET NOT NULL;

-------------------feematrix details tenantid-------------------
ALTER TABLE ONLY egtl_fee_matrix_details ADD COLUMN tenantId character varying(128);


UPDATE egtl_fee_matrix_details details SET tenantId = (select matrix.tenantid FROM egtl_mstr_fee_matrix matrix where  matrix.id= details.feeMatrixId);

ALTER TABLE egtl_fee_matrix_details ALTER column tenantId SET NOT NULL ;