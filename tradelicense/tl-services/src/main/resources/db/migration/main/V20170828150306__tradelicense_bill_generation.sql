CREATE TABLE egtl_tradelicense_bill
(
  id bigint NOT NULL,
  billid character varying(64) NOT NULL,
  licenseid bigint NOT NULL,
  createdBy character varying,
  lastModifiedBy character varying,
  createdTime bigint,
  lastModifiedTime bigint,
  tenantid character varying NOT NULL,
  	CONSTRAINT pk_tradelicense_bill_pkey PRIMARY KEY (id),
	CONSTRAINT fk_tradelicense_bill FOREIGN KEY (licenseid)
      REFERENCES egtl_license (id));


create sequence seq_egtl_tradelicense_bill;