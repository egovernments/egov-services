ALTER TABLE egpgr_receiving_center ALTER COLUMN createddate DROP DEFAULT;

ALTER TABLE egpgr_receiving_center ALTER COLUMN lastmodifieddate DROP DEFAULT;


ALTER TABLE egpgr_receivingmode ALTER COLUMN createddate DROP DEFAULT;

ALTER TABLE egpgr_receivingmode ALTER COLUMN lastmodifieddate DROP DEFAULT;


ALTER TABLE egpgr_complainttype_category ALTER COLUMN createddate DROP DEFAULT;

ALTER TABLE egpgr_complainttype_category ALTER COLUMN lastmodifieddate DROP DEFAULT;

ALTER TABLE egpgr_complainttype_category DROP CONSTRAINT egpgr_complainttype_category_name_key;
