CREATE TABLE cs_draft(
     id bigint NOT NULL,
     userid bigint not null,
     draft text not null,
     tenantid character varying(256) not null,
     servicecode character varying(20) not null,
     version bigint DEFAULT 0,
     createddate timestamp  ,
     lastmodifieddate timestamp ,
     createdby bigint ,
     lastmodifiedby bigint
 );

CREATE SEQUENCE seq_cs_draft
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cs_draft ADD CONSTRAINT pk_cs_draft PRIMARY KEY (id);