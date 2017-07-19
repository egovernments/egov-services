ALTER TABLE EG_WF_TYPES DROP COLUMN module;

--rollback alter table EG_WF_TYPES add module bigint;