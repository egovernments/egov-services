create table pricelist (
      id character varying(50) NOT NULL,
      supplier varchar(50) NOt NULL,
      rateType varchar(256) NOT NULL,
      rateContractNumber varchar(1024) NOT NULL,
      rateContractDate varchar(50) ,
      agreementNumber varchar(50) NOT NULL,
      agreementDate varchar(50) NOT NULL,
      agreementStartDate varchar(50) NOT NULL,
      agreementEndDate varchar(50),
      active boolean,
      fileStoreId varchar(50) NOT NULL,
      priceListDetails varchar(50) NOT NULL,
      createdby varchar(50),
      createdtime bigint,
      lastmodifiedby varchar(50),
      lastmodifiedtime bigint,
      tenantid varchar(256) NOT NULL
      );

alter table pricelist add constraint pk_pricelist primary key (id, tenantid);
create sequence seq_pricelist;


create table pricelistdetails (
      id character varying(50) NOT NULL,
      pricelist character varying(50) NOT NULL, 
      material varchar(50),
      fromDate varchar(256) NOT NULL,
      toDate varchar(1024) NOT NULL,
      ratePerUnit varchar(50) ,
      quantity varchar(50) NOT NULL,
      uom varchar(50) NOT NULL,
      active boolean,
      createdby varchar(50),
      createdtime bigint,
      lastmodifiedby varchar(50),
      lastmodifiedtime bigint,
      tenantid varchar(256) NOT NULL
      );
      
alter table pricelistdetails add constraint pk_pricelistdetails primary key (id, tenantid);
create sequence seq_pricelistdetails;


