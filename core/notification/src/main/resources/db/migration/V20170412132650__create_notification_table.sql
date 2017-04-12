CREATE TABLE notification (
    id bigint PRIMARY KEY,
    messagecode VARCHAR(128) NOT NULL,
    messagevalues VARCHAR(250) NOT NULL,
    reference VARCHAR(128) NOT NULL,
    userid bigint NOT NULL,
    read CHARACTER(1) NOT NULL DEFAULT 'N',
    createddate timestamp NOT NULL,
    lastmodifieddate timestamp,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    version bigint
);

CREATE SEQUENCE seq_notification
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;