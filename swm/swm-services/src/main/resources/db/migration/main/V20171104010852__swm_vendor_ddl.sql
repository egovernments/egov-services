
Create table egswm_vendor( 
  vendorNo varchar(256) NOT NULL,
  tenantId varchar(256) NOT NULL,
  name varchar(256) NOT NULL,
  registrationNo varchar(256) NOT NULL,
  contractor varchar(256) NOT NULL,
  details varchar(500) ,
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  version bigint
);

Create table egswm_contractor( 
  contractorNo varchar(128) NOT NULL,
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

Create table egswm_vendorservicedlocations( 
  tenantId varchar(256) NOT NULL,
  vendor varchar(256) NOT NULL,
  location varchar(256) NOT NULL,
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  version bigint
);

Create table egswm_vendorservicesOffered( 
  tenantId varchar(256) NOT NULL,
  vendor varchar(256) NOT NULL,
  service varchar(256) NOT NULL,
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  version bigint
);


alter table egswm_vendor add constraint pk_egswm_vendor primary key (vendorNo);
create sequence seq_egswm_vendor;

alter table egswm_contractor add constraint pk_egswm_contractor primary key (contractorNo);
create sequence seq_egswm_contractor;