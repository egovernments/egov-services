
ALTER TABLE materialreceipt DROP COLUMN lastmodifiedtime;
ALTER TABLE materialreceipt DROP COLUMN createdtime;

ALTER TABLE materialreceipt ADD lastmodifiedtime bigint;
ALTER TABLE materialreceipt ADD createdtime bigint;


ALTER TABLE materialreceipt 
ALTER COLUMN createdBy type character varying(128);

ALTER TABLE materialreceipt 
ALTER COLUMN lastmodifiedBy type character varying(128);
