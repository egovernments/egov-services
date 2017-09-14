ALTER TABLE egtl_mstr_fee_matrix ALTER COLUMN applicationType DROP NOT NULL;
ALTER TABLE egtl_mstr_fee_matrix ALTER COLUMN businessNature DROP NOT NULL;
ALTER TABLE egtl_mstr_fee_matrix  ADD COLUMN feeType character varying(50);