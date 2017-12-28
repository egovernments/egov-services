ALTER TABLE materialreceipt ADD COLUMN supplierbillpaid boolean default false;

ALTER TABLE materialreceipt ALTER COLUMN deleted set default false;
ALTER TABLE materialreceiptdetail ALTER COLUMN deleted set default false;