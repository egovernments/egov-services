ALTER TABLE egtl_mstr_status
ALTER COLUMN moduleType SET DEFAULT 'LICENSE';

UPDATE egtl_mstr_status set moduleType='LICENSE';

ALTER TABLE egtl_mstr_status
ALTER COLUMN moduleType SET NOT NULL;