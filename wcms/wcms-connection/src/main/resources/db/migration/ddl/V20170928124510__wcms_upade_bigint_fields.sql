update  egwtr_waterconnection set manualReceiptDate=0 where manualReceiptDate is null;
 ALTER TABLE egwtr_waterconnection  ALTER COLUMN manualReceiptDate SET DEFAULT 0;


update  egwtr_waterconnection set noOfFlats=0 where noOfFlats is null;
 ALTER TABLE egwtr_waterconnection  ALTER COLUMN noOfFlats SET DEFAULT 0;