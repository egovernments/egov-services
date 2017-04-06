CREATE TABLE egpgr_complaintstatus (
    id bigint NOT NULL,
    name character varying(25),
    version bigint
);

CREATE SEQUENCE seq_egpgr_complaintstatus
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE  egpgr_complaintstatus
    ADD CONSTRAINT pk_complaintstatus PRIMARY KEY (id);