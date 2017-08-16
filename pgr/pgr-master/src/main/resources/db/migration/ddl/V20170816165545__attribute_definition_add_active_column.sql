ALTER TABLE attribute_definition ADD COLUMN active character(1) NOT NULL DEFAULT 'Y'::bpchar;
