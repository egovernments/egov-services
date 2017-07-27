CREATE TABLE eg_sevaconfig (
id BIGINT NOT NULL,
keyName CHARACTER VARYING(50) NOT NULL,
description CHARACTER VARYING(250),
createdBy BIGINT NOT NULL,
createdDate DATE NOT NULL,
lastModifiedBy BIGINT,
lastModifiedDate DATE,
tenantId CHARACTER VARYING(250) NOT NULL,

CONSTRAINT pk_egeis_hrConfiguration PRIMARY KEY (Id)
);

CREATE SEQUENCE seq_eg_sevaconfig
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE TABLE eg_sevaconfigvalues (
id BIGINT NOT NULL,
keyId BIGINT NOT NULL,
value CHARACTER VARYING(1000) NOT NULL,
effectiveFrom DATE NOT NULL,
createdBy BIGINT NOT NULL,
createdDate DATE NOT NULL,
lastModifiedBy BIGINT,
lastModifiedDate DATE,
tenantId CHARACTER VARYING(250) NOT NULL,

CONSTRAINT pk_egeis_hrConfigurationValues PRIMARY KEY (Id)
--CONSTRAINT uk_egeis_hrConfigurationValues_keyId UNIQUE (keyId)
);

CREATE SEQUENCE seq_eg_sevaconfigvalues
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;
