Create table egf_vouchersubtype(
id varchar(256),
vouchertype varchar(256) not null,
vouchername varchar(50) not null UNIQUE,
cutoffdate timestamp without time zone,
exclude boolean,	
createdby varchar(256),
createddate timestamp without time zone,
lastmodifiedby varchar(256),
lastmodifieddate timestamp without time zone,
tenantId varchar(256),
version bigint
);
alter table egf_vouchersubtype add constraint pk_egf_vouchersubtype primary key (id);
create sequence seq_egf_vouchersubtype;