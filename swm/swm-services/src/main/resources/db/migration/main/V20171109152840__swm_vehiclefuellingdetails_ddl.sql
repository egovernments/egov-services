
Create table egswm_vehiclefuellingdetails( 
  tenantId varchar(128) NOT NULL,
  transactionNo varchar(256) NOT NULL,
  transactionDate bigint NOT NULL,
  vehicle varchar(50) NOT NULL,
  vehicleReadingDuringFuelling bigint NOT NULL,
  refuellingStation varchar(50) NOT NULL,
  fuelFilled varchar(256) NOT NULL,
  typeOfFuel varchar(256) NOT NULL,
  totalCostIncurred numeric (13,2) NOT NULL,
  receiptNo varchar(256) NOT NULL,
  receiptDate bigint NOT NULL,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);
alter table egswm_vehiclefuellingdetails add constraint pk_egswm_vehiclefuellingdetails primary key (transactionNo,tenantId);