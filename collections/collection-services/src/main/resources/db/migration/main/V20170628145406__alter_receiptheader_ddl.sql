ALTER TABLE egcl_receiptheader DROP COLUMN businessdetails;

ALTER TABLE egcl_receiptheader
ADD COLUMN businessdetails character varying(256) NOT NULL;