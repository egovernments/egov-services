
Create table egswm_vehicle( 
  id varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  vehicleType varchar(50),
  regNumber varchar(256) NOT NULL,
  engineSrNumber varchar(50),
  chassisSrNumber varchar(256) NOT NULL,
  vehicleCapacity bigint NOT NULL,
  numberOfPersonsReq bigint NOT NULL,
  model varchar(256) NOT NULL,
  ulbOwnedVehicle boolean,
  vendorName varchar(256),
  vehicleDriverName varchar(256),
  purchaseDate bigint,
  yearOfPurchase varchar(256),
  price numeric (13,2),
  sourceOfPurchase varchar(256),
  remarks varchar(300),
  insuranceNumber varchar(256) NOT NULL,
  insuranceValidityDate bigint NOT NULL,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);
alter table egswm_vehicle add constraint pk_egswm_vehicle primary key (id);
create sequence seq_egswm_vehicle;
