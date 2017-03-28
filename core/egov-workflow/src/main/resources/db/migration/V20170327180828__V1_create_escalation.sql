CREATE TABLE egpgr_escalation (
    id bigint NOT NULL,
    complaint_type_id bigint,
    designation_id bigint,
    lastmodifiedby bigint,
    tenantid character varying(256) NOT NULL,
    lastmodifieddate timestamp,
    createdby bigint,
    createddate timestamp,
    no_of_hrs integer,
    version bigint
);


CREATE SEQUENCE seq_egpgr_escalation
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE egpgr_escalation ADD CONSTRAINT pk_escalation_id PRIMARY KEY (id);
