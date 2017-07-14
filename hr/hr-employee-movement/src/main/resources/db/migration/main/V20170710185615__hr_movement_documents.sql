ALTER TABLE egeis_movement DROP COLUMN documents;
CREATE TABLE egeis_movementdocuments (
	id BIGINT NOT NULL,
	movementId BIGINT NOT NULL,
	document CHARACTER VARYING(1000) NOT NULL,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_movementdocuments PRIMARY KEY (id),
	CONSTRAINT uk_egeis_movementdocuments_document UNIQUE (document)
);

CREATE SEQUENCE seq_egeis_movementdocuments
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;