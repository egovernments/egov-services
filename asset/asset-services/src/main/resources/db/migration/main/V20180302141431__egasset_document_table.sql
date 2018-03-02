CREATE SEQUENCE seq_egasset_document;

CREATE TABLE egasset_document 
(
id bigint,
asset bigint NOT NULL,
filestore character varying NOT NULL, 
tenantId character varying NOT NULL,
CONSTRAINT pk_egasset_document PRIMARY KEY(id,tenantid)
);
