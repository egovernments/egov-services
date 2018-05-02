CREATE TABLE egeis_leaveapplication_documents (
	id BIGINT NOT NULL,
	leaveapplicationId BIGINT NOT NULL,
	document CHARACTER VARYING(1000) NOT NULL,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_leaveapplication_documents PRIMARY KEY (id,tenantId)
);

CREATE SEQUENCE seq_egeis_leaveapplication_documents
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;