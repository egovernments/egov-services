Drop Sequence SEQ_egw_contractor;
ALTER TABLE egw_contractor ALTER COLUMN bank DROP NOT NULL;
ALTER TABLE egw_contractor ALTER COLUMN bankaccountnumber DROP NOT NULL;
ALTER TABLE egw_contractor ALTER COLUMN accountcode DROP NOT NULL;
ALTER TABLE egw_contractor ALTER COLUMN ifsccode DROP NOT NULL;
