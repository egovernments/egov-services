DELETE from egasset_current_value;

ALTER TABLE egasset_current_value ADD COLUMN transactionDate bigint NOT NULL;
