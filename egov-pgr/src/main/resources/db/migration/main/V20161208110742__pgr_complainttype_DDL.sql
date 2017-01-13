---------------------------------------- START -----------------------------------------------
ALTER TABLE egpgr_complainttype ADD COLUMN metadata BOOLEAN DEFAULT true;
ALTER TABLE egpgr_complainttype ADD COLUMN type character varying(50) DEFAULT 'realtime';
ALTER TABLE egpgr_complainttype ADD COLUMN keywords character varying(100) DEFAULT '';
---------------------------------------- END -------------------------------------------------

-->rollback script
--ALTER TABLE egpgr_complainttype DROP COLUMN metadata;
--ALTER TABLE egpgr_complainttype DROP COLUMN type;
--ALTER TABLE egpgr_complainttype DROP COLUMN keywords;
