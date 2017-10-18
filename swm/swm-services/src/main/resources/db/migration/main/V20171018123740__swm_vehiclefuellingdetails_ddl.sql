
Create table egswm_vehiclefuellingdetails( 
  id varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  transactionId varchar(256) NOT NULL,
  transactionDate bigint NOT NULL,
  vehicleType varchar(50) NOT NULL,
  vehicleRegNo varchar(50) NOT NULL,
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
alter table egswm_vehiclefuellingdetails add constraint pk_egswm_vehiclefuellingdetails primary key (id);
create sequence egseq_swm_vehiclefuellingdetails;
