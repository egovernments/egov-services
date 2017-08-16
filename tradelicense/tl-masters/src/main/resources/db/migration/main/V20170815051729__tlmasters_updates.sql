ALTER TABLE egtl_mstr_category
ADD COLUMN validityYears  numeric;

ALTER TABLE egtl_mstr_category
ALTER COLUMN code TYPE character varying(20),
ALTER COLUMN name TYPE character varying(100);

ALTER TABLE egtl_mstr_uom
ALTER COLUMN code TYPE character varying(20),
ALTER COLUMN name TYPE character varying(100);


ALTER TABLE egtl_mstr_document_type
ALTER COLUMN name TYPE character varying(100);

ALTER TABLE egtl_mstr_status
ALTER COLUMN code TYPE character varying(20),
ALTER COLUMN name TYPE character varying(100);




