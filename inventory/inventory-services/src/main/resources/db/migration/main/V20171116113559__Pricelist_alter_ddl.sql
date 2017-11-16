--Removing old primary key which is combination of id and tenantId
ALTER TABLE pricelist DROP CONSTRAINT pk_pricelist;

--Adding rateContractNumber as new primary key
ALTER TABLE pricelist add constraint pk_pricelist primary key (rateContractNumber);


ALTER TABLE pricelistdetails alter fromDate drop not null, alter toDate drop not null;