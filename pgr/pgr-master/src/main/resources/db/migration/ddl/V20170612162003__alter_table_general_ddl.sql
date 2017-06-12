ALTER TABLE egpgr_complainttype_category
ADD createdby bigint DEFAULT NULL,
ADD createddate timestamp DEFAULT NULL,
ADD lastmodifiedby bigint DEFAULT NULL,
ADD lastmodifieddate timestamp DEFAULT NULL;

ALTER TABLE egpgr_escalation DROP COLUMN id;
ALTER TABLE egpgr_escalation ADD id SERIAL NOT NULL;

ALTER TABLE egpgr_complainttype_category DROP COLUMN id;
ALTER TABLE egpgr_complainttype_category ADD id SERIAL NOT NULL;