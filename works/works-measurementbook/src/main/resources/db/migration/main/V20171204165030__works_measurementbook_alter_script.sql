ALTER TABLE egw_measurementbook ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_measurementbook_details ADD COLUMN deleted boolean DEFAULT false;
ALTER TABLE egw_mb_measurementsheet ADD COLUMN deleted boolean DEFAULT false;

DROP TABLE egw_mb_contractor_bills;