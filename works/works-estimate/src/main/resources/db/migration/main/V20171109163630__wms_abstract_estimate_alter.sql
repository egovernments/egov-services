ALTER TABLE egw_abstractestimate_details DROP COLUMN estimatenumber;
ALTER TABLE egw_abstractestimate RENAME technicalsanctionnumber TO financialsanctionnumber;
ALTER TABLE egw_abstractestimate RENAME technicalsanctiondate TO financialsanctiondate;
ALTER TABLE egw_abstractestimate RENAME technicalsanctionby TO financialsanctionby;

ALTER TABLE egw_abstractestimate ADD COLUMN landassetrequired boolean;
ALTER TABLE egw_abstractestimate ADD COLUMN nooflands bigint;
ALTER TABLE egw_abstractestimate ADD COLUMN otherassetspecificationremarks varchar(1024);
ALTER TABLE egw_abstractestimate ADD COLUMN dpremarks varchar(512);
ALTER TABLE egw_abstractestimate ADD COLUMN workproposedasperdp varchar(100);