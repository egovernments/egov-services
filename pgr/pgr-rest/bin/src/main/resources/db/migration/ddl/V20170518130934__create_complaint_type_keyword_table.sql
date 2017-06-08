ALTER TABLE egpgr_complainttype DROP COLUMN keywords;

CREATE TABLE servicetype_keyword (
    id bigint NOT NULL,
    servicecode VARCHAR(20) NOT NULL,
    keyword VARCHAR(100) NOT NULL,
    tenantid VARCHAR(256) NOT NULL,
    version bigint DEFAULT 0,
    createddate timestamp NOT NULL,
    lastmodifieddate timestamp ,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);

CREATE SEQUENCE seq_servicetype_keyword
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE servicetype_keyword ADD CONSTRAINT servicetype_keyword_uk UNIQUE (servicecode, tenantid, keyword);
ALTER TABLE servicetype_keyword ADD CONSTRAINT servicetype_keyword_pk PRIMARY KEY (id);
