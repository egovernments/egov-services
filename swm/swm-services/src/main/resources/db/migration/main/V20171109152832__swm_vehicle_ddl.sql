
Create table egswm_vehicle( 
  tenantId varchar(128) NOT NULL,
  vehicleType varchar(256),
  fuelType varchar(256),
  regNumber varchar(22) NOT NULL,
  engineSrNumber varchar(256),
  chassisSrNumber varchar(256) NOT NULL,
  vehicleCapacity numeric (13,2) NOT NULL,
  operatorsReq bigint NOT NULL,
  model varchar(256) NOT NULL,
  vendor varchar(256),
  driver varchar(256) NOT NULL,
  purchaseDate bigint,
  price numeric (13,2),
  sourceOfPurchase varchar(256),
  remarks varchar(300),
  insuranceNumber varchar(256) NOT NULL,
  insuranceValidityDate bigint NOT NULL,
  kilometers bigint,
  endOfWarranty bigint,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);
alter table egswm_vehicle add constraint pk_egswm_vehicle primary key (regNumber,tenantId);