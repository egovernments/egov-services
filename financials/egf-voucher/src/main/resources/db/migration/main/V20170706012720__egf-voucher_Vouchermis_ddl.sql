
Create table egf_vouchermis( 
  id varchar(50),
  billNumber varchar(50),
  functionId varchar(50),
  fundsourceId varchar(50),
  schemeId varchar(50),
  subSchemeId varchar(50),
  functionaryId varchar(50),
  sourcePath varchar(50),
  budgetCheckRequired boolean,
  budgetAppropriationNo varchar(50),
    VoucherId varchar(50),
		createdby varchar(50),
		createddate timestamp without time zone,
		lastmodifiedby varchar(50),
		lastmodifieddate timestamp without time zone,
		tenantId varchar(250),
		version bigint
);
alter table egf_vouchermis add constraint pk_egf_vouchermis primary key (id);
create sequence seq_egf_vouchermis;
