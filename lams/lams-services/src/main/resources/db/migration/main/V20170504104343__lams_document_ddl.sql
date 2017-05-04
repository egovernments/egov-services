CREATE SEQUENCE seq_eglams_documenttype;

CREATE SEQUENCE seq_eglams_document;


CREATE TABLE eglams_documenttype 
(
id bigint,
name character varying,
application character varying(16),
tenantId character varying NOT NULL,
CONSTRAINT pk_eglams_documenttype PRIMARY KEY(id,tenantid)
);


CREATE TABLE eglams_document 
(
id bigint,
documenttype bigint NOT NULL,
agreement bigint NOT NULL,
filestore character varying NOT NULL, 
tenantId character varying NOT NULL,
CONSTRAINT pk_eglams_document PRIMARY KEY(id,tenantid),
CONSTRAINT fk_eglams_document FOREIGN KEY(id,tenantid) REFERENCES eglams_documenttype(id,tenantid)
);

