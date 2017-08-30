-------------------category details tenantid-------------------
ALTER TABLE ONLY egtl_category_details ADD COLUMN tenantId character varying(128);

UPDATE egtl_category_details
SET tenantId = (select egtl_mstr_category.tenantid FROM egtl_mstr_category JOIN egtl_category_details 
   ON egtl_mstr_category.id=egtl_category_details.categoryId);
   
ALTER TABLE egtl_category_details
ALTER column tenantId SET NOT NULL;

-------------------feematrix details tenantid-------------------
ALTER TABLE ONLY egtl_fee_matrix_details ADD COLUMN tenantId character varying(128);

UPDATE egtl_fee_matrix_details
SET tenantId = (select egtl_mstr_fee_matrix.tenantid FROM egtl_mstr_fee_matrix JOIN egtl_fee_matrix_details 
   ON egtl_mstr_fee_matrix.id=egtl_fee_matrix_details.feeMatrixId);
   
ALTER TABLE egtl_fee_matrix_details
ALTER column tenantId SET NOT NULL ;