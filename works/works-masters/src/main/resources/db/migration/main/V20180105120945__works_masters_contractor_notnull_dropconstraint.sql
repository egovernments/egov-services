ALTER TABLE egw_contractor ALTER COLUMN paymentaddress DROP NOT NULL;
ALTER TABLE egw_contractor ALTER COLUMN contactperson DROP NOT NULL;
ALTER TABLE egw_contractor ALTER COLUMN email DROP NOT NULL;
ALTER TABLE egw_contractor ALTER COLUMN pannumber DROP NOT NULL;
ALTER TABLE egw_contractor ALTER COLUMN tinnumber DROP NOT NULL;
ALTER TABLE egw_contractor ALTER COLUMN pwdapprovalcode DROP NOT NULL;
ALTER TABLE egw_contractor ALTER COLUMN pwdapprovalvalidtill DROP NOT NULL;
ALTER TABLE egw_contractor ALTER COLUMN epfRegistrationNumber DROP NOT NULL;
ALTER TABLE egw_contractor ALTER COLUMN tinnumber DROP NOT NULL;

Alter table egw_contractor alter column exemptedfrom TYPE character varying(50);

