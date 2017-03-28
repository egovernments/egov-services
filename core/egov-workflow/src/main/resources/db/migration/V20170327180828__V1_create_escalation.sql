CREATE TABLE egpgr_escalation (
    id bigint NOT NULL,
    complaint_type_id bigint,
    designation_id bigint,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    createddate timestamp without time zone,
    no_of_hrs integer,
    version bigint
);


CREATE SEQUENCE seq_egpgr_escalation
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ONLY egpgr_escalation
    ADD CONSTRAINT pk_escalation_id PRIMARY KEY (id);

ALTER TABLE ONLY egpgr_escalation
    ADD CONSTRAINT fk_pgr_escalation_com_type_id FOREIGN KEY (complaint_type_id) REFERENCES egpgr_complainttype(id);

ALTER TABLE ONLY egpgr_escalation
    ADD CONSTRAINT fk_pgr_escalation_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id);
ALTER TABLE ONLY egpgr_escalation
    ADD CONSTRAINT fk_pgr_escalation_desig_id FOREIGN KEY (designation_id) REFERENCES eg_designation(id);
ALTER TABLE ONLY egpgr_escalation
    ADD CONSTRAINT fk_pgr_escalation_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id);