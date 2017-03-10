CREATE TABLE egeis_objectType (
	id BIGINT NOT NULL,
	type CHARACTER VARYING(50) NOT NULL,
	description CHARACTER VARYING(250),
	lastModifiedDate date,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_objectType PRIMARY KEY (id),
	CONSTRAINT uk_egeis_objectType_type UNIQUE (type)
);

CREATE SEQUENCE seq_egeis_objectType
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;