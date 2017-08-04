ALTER TABLE ONLY egtl_mstr_category
    DROP CONSTRAINT unq_tlcategory;
    
ALTER TABLE ONLY egtl_mstr_category
	ADD CONSTRAINT unq_tlcategory_code UNIQUE (tenantId, code);
	
ALTER TABLE ONLY egtl_mstr_category
	ADD CONSTRAINT unq_tlcategory_name UNIQUE (tenantId, name);
	
	
ALTER TABLE ONLY egtl_mstr_uom
    DROP CONSTRAINT unq_tluom ;
    
ALTER TABLE ONLY egtl_mstr_uom
    ADD CONSTRAINT unq_tluom_code UNIQUE (tenantId, code);
    
ALTER TABLE ONLY egtl_mstr_uom
    ADD CONSTRAINT unq_tluom_name UNIQUE (tenantId, name);