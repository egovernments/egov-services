DELETE from egasset_current_value where transactiondate IS NULL;

ALTER TABLE egasset_current_value ADD COLUMN transactionDate bigint NOT NULL;
