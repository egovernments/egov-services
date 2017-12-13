ALTER TABLE indentdetail ALTER COLUMN indentquantity SET DEFAULT 0.00;
ALTER TABLE indentdetail ALTER COLUMN totalprocessedquantity SET DEFAULT 0.00;
ALTER TABLE indentdetail ALTER COLUMN indentissuedquantity SET DEFAULT 0.00;
ALTER TABLE indentdetail ALTER COLUMN poorderedquantity SET DEFAULT 0.00;
ALTER TABLE indentdetail ALTER COLUMN interstorerequestquantity SET DEFAULT 0.00;

ALTER TABLE indentdetail ADD CONSTRAINT indentquantitycheck CHECK (indentquantity >= 0.00);
ALTER TABLE indentdetail ADD CONSTRAINT totalprocessedquantitycheck CHECK (totalprocessedquantity >= 0.00);
ALTER TABLE indentdetail ADD CONSTRAINT indentissuedquantitycheck CHECK (indentissuedquantity >= 0.00);
ALTER TABLE indentdetail ADD CONSTRAINT poorderedquantitycheck CHECK (poorderedquantity >= 0.00);
ALTER TABLE indentdetail ADD CONSTRAINT interstorerequestquantitycheck CHECK (interstorerequestquantity >= 0.00);
