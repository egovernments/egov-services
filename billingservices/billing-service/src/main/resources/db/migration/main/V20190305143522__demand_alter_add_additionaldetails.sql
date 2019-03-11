--> demand

ALTER TABLE egbs_demand RENAME column owner TO payer;

ALTER TABLE egbs_demand ADD COLUMN additionaldetails json;

ALTER TABLE egbs_demand ALTER COLUMN createdby type character varying(256),
						ALTER COLUMN lastmodifiedby type character varying(256);
						
--> demand detail

ALTER TABLE egbs_demanddetail ADD COLUMN additionaldetails json;

ALTER TABLE egbs_demanddetail ALTER COLUMN createdby type character varying(256),
						ALTER COLUMN lastmodifiedby type character varying(256);
						
--> BILL 

ALTER TABLE egbs_bill RENAME COLUMN payeename TO payername;

ALTER TABLE egbs_bill RENAME COLUMN payeeaddress TO payeraddress;

ALTER TABLE egbs_bill RENAME COLUMN payeeemail TO payeremail;

ALTER TABLE egbs_bill ALTER COLUMN createdby type character varying(256), 
					  ALTER COLUMN lastmodifiedby type character varying(256);


--> bill detail

ALTER TABLE egbs_billdetail ALTER COLUMN billno drop NOT NULL;
							
ALTER TABLE egbs_billdetail ADD COLUMN fromperiod bigint,
							ADD COLUMN toperiod bigint,
							ADD COLUMN demandid character varying(64);


ALTER TABLE egbs_billdetail ALTER COLUMN createdby type character varying(256), 
					  ALTER COLUMN lastmodifiedby type character varying(256);

--> billACCountDetail

ALTER TABLE egbs_billaccountdetail ALTER COLUMN accountdescription DROP NOT NULL,
								   ALTER COLUMN glcode DROP NOT NULL;

ALTER TABLE egbs_billaccountdetail ADD COLUMN taxheadcode character varying(256),
								   ADD COLUMN amount numeric(10,2),
								   ADD COLUMN adjustedamount numeric(10,2),
								   ADD COLUMN demanddetailid character varying(64);
								   
ALTER TABLE egbs_billaccountdetail ALTER COLUMN createdby type character varying(256), 
					  ALTER COLUMN lastmodifiedby type character varying(256);
								   