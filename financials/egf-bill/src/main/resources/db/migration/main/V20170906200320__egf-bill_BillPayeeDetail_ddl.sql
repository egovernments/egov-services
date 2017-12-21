
Create table egf_billpayeedetail( 
  id varchar(50),
  accountDetailType varchar(50),
  accountDetailKey varchar(50) NOT NULL,
  amount numeric (13,2) NOT NULL,
  billDetail varchar(50),
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  tenantId varchar(250),
  version bigint
);
alter table egf_billpayeedetail add constraint pk_egf_billpayeedetail primary key (id,tenantId);
create sequence seq_egf_billpayeedetail;
