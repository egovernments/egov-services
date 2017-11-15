ALTER TABLE material RENAME staockingUom TO stockingUom;
ALTER TABLE material drop column materialcontroltype;
ALTER TABLE material drop column overridematerialcontroltype;


ALTER TABLE material ADD COLUMN inactivedate bigint;
ALTER TABLE material ADD COLUMN assetcategory character varying(50);

ALTER TABLE material ADD COLUMN lotcontrol boolean;
ALTER TABLE material ADD COLUMN shelfLifeControl boolean;
ALTER TABLE material ADD COLUMN serialNumber boolean;
ALTER TABLE material ADD COLUMN scrapable boolean;