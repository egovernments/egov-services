create sequence seq_egwtr_connectiondocument;

create table egwtr_connectiondocument(
id bigint NOT NULL,
documenttype character varying(100) NOT NULL,
acknowledgementnumber character varying(100) NOT NULL,
filestoreid character varying(250) NOT NULL,
connectionid bigint NOT NULL,
tenantid varchar NOT NULL,
createdby bigint NOT NULL,
createddate bigint,
lastmodifiedby bigint NOT NULL,
lastmodifieddate bigint,
version numeric  DEFAULT 0,
CONSTRAINT egwtr_connectiondocument_pk PRIMARY KEY (id),
CONSTRAINT fk_connectiondocument FOREIGN KEY (connectionid,tenantid)
REFERENCES egwtr_waterconnection (id,tenantid)
);


