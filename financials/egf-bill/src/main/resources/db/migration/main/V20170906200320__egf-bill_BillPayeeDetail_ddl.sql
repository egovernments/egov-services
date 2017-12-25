
Create table egf_billpayeedetail( 
  id varchar(256),
  accountDetailType varchar(256) NOT NULL,
  accountDetailKey varchar(256) NOT NULL,
  amount numeric (13,2) NOT NULL,
  billDetail varchar(256),
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  tenantId varchar(250),
  version bigint
);
alter table egf_billpayeedetail add constraint pk_egf_billpayeedetail primary key (id,tenantId);
create sequence seq_egf_billpayeedetail;
