--Material Receipt Detail
ALTER TABLE materialreceiptdetail ALTER COLUMN receivedqty SET DEFAULT 0.00;
ALTER TABLE materialreceiptdetail ALTER COLUMN acceptedqty SET DEFAULT 0.00;

ALTER TABLE materialreceiptdetail ADD CONSTRAINT mrdacceptedqtychk CHECK (acceptedqty >= 0.00);
ALTER TABLE materialreceiptdetail ADD CONSTRAINT mrdreceivedqtychk CHECK (receivedqty >= 0.00);

--Material Receipt Additional info
ALTER TABLE materialreceiptdetailaddnlinfo ALTER COLUMN quantity SET DEFAULT 0.00;
ALTER TABLE materialreceiptdetailaddnlinfo ADD CONSTRAINT mrdinfoqtychk CHECK (quantity >= 0.00);


