
Create table egswm_vendor( 
  vendorNo varchar(256) NOT NULL,
  tenantId varchar(256) NOT NULL,
  name varchar(256) NOT NULL,
  registrationNo varchar(256) NOT NULL,
  supplier varchar(256) NOT NULL,
  details varchar(500) ,
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  version bigint
);

alter table egswm_vendor add constraint pk_egswm_vendor primary key (vendorNo,tenantId);

Create table egswm_supplier( 
  supplierNo varchar(128) NOT NULL,
  tenantId varchar(256) NOT NULL,
  name varchar(100),
  agencyName varchar(100),
  email varchar(100) NOT NULL,
  tinNumber varchar(256) NOT NULL,
  gst varchar(256) NOT NULL,
  mobileNo varchar(10) ,
  contactNo varchar(10) NOT NULL,
  faxNumber varchar(10),
  address varchar(500) NOT NULL,
  registrationNo varchar(256),
  aadharNo varchar(12),
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  version bigint
);

alter table egswm_supplier add constraint pk_egswm_supplier primary key (supplierNo,tenantId);

Create table egswm_vendorservicedlocations( 
  tenantId varchar(256) NOT NULL,
  vendor varchar(256) NOT NULL,
  location varchar(256) NOT NULL,
  version bigint
);

Create table egswm_vendorservicesOffered( 
  tenantId varchar(256) NOT NULL,
  vendor varchar(256) NOT NULL,
  service varchar(256) NOT NULL,
  version bigint
);