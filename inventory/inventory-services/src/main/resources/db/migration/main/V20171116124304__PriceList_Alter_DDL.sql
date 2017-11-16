--Removing old primary key which is just rateContractNumber
ALTER TABLE pricelist DROP CONSTRAINT pk_pricelist;

--Adding rateContractNumber and tenantid combination as new primary key
ALTER TABLE pricelist add constraint pk_pricelist primary key (rateContractNumber, tenantid);

