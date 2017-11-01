ALTER TABLE egasset_asset ADD COLUMN IF NOT EXISTS openingdate bigint,ADD COLUMN IF NOT EXISTS fundsource character varying(250),ADD COLUMN IF NOT EXISTS location character varying(4000); 
