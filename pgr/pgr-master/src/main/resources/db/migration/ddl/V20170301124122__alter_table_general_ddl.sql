ALTER TABLE egpgr_complainttype_category
ADD createdby bigint DEFAULT NULL,
ADD createddate timestamp DEFAULT NULL,
ADD lastmodifiedby bigint DEFAULT NULL,
ADD lastmodifieddate timestamp DEFAULT NULL;

ALTER TABLE egpgr_complainttype_category DROP COLUMN id;
ALTER TABLE egpgr_complainttype_category ADD id SERIAL NOT NULL;

ALTER TABLE egpgr_complainttype_category
ADD code varchar(255) NOT NULL;	

alter table egpgr_complaint add column lastaccessedtime timestamp;

ALTER TABLE egpgr_complainttype DROP COLUMN keywords;

DROP SEQUENCE SEQ_EGPGR_COMPLAINT;
DROP SEQUENCE seq_egpgr_complainant;

ALTER TABLE egpgr_complainttype_category ADD CONSTRAINT egpgr_complainttype_category_code_key UNIQUE (code);
