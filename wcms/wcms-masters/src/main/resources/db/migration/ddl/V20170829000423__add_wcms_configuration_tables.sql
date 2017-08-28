CREATE SEQUENCE seq_egwtr_configuration;
CREATE SEQUENCE seq_egwtr_configurationvalues;


CREATE TABLE egwtr_configuration (
id BIGINT NOT NULL,
keyName CHARACTER VARYING(50) NOT NULL,
description CHARACTER VARYING(250),
createdBy BIGINT NOT NULL,
createdDate bigint,
lastModifiedBy BIGINT NOT NULL,
lastModifiedDate bigint,
tenantId CHARACTER VARYING(250) NOT NULL,
CONSTRAINT pk_egwtr_configuration PRIMARY KEY (id)
);

CREATE TABLE egwtr_configurationvalues (
id BIGINT NOT NULL,
keyId BIGINT NOT NULL,
value CHARACTER VARYING(1000) NOT NULL,
effectiveFrom bigint NOT NULL,
createdBy BIGINT NOT NULL,
createdDate bigint,
lastModifiedBy BIGINT NOT NULL,
lastModifiedDate bigint,
tenantId CHARACTER VARYING(250) NOT NULL,
CONSTRAINT pk_egwtr_configurationvalues PRIMARY KEY (id)
);