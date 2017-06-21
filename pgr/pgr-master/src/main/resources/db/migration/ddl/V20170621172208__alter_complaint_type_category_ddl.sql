ALTER TABLE egpgr_complainttype_category DROP CONSTRAINT egpgr_complainttype_category_pkey;

ALTER TABLE egpgr_complainttype_category DROP COLUMN id;

ALTER TABLE egpgr_complainttype_category ADD COLUMN id SERIAL;

