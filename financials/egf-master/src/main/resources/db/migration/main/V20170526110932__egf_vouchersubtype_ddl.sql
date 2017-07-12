Create table egf_vouchersubtype( 
	id bigint,
	name varchar(256) NOT NULL,
	validUpTo date,
	exclude boolean NOT NULL,
	createdby bigint,
	createddate timestamp without time zone,
	lastmodifiedby bigint,
	lastmodifieddate timestamp without time zone,
	version bigint
);
alter table egf_vouchersubtype add constraint pk_egf_vouchersubtype primary key (id);
create sequence seq_egf_vouchersubtype;

