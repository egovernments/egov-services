ALTER TABLE materialissuedetail ALTER COLUMN quantityissued SET DEFAULT 0.00;
ALTER TABLE materialissuedfromreceipt ALTER COLUMN quantity SET DEFAULT 0.00;


ALTER TABLE materialissuedetail ADD CONSTRAINT quantityissued CHECK (quantityissued >= 0.00);
ALTER TABLE materialissuedfromreceipt ADD CONSTRAINT quantity CHECK (quantity >= 0.00);

