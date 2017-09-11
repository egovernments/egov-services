
Create table egf_billpayeedetail( 
  id varchar(50),
  accountDetailTypeId varchar(50),
  accountDetailKeyId varchar(50) NOT NULL,
  amount numeric (13,2) NOT NULL,
    BillDetailId varchar(50),
		createdby varchar(50),
		createddate timestamp without time zone,
		lastmodifiedby varchar(50),
		lastmodifieddate timestamp without time zone,
		tenantId varchar(250),
		version bigint
);
alter table egf_billpayeedetail add constraint pk_egf_billpayeedetail primary key (id);
create sequence seq_egf_billpayeedetail;
