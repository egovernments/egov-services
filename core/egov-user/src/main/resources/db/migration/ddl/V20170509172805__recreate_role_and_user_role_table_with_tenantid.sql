DROP TABLE eg_userrole;
DROP TABLE eg_role;
DROP SEQUENCE SEQ_EG_ROLE;

CREATE TABLE eg_role (
    id bigint NOT NULL,
    name character varying(32) NOT NULL,
    code character varying(50) NOT NULL,
    description character varying(128),
    createddate timestamp DEFAULT CURRENT_TIMESTAMP,
    createdby bigint,
    lastmodifiedby bigint,
    lastmodifieddate timestamp,
    version bigint,
    tenantid character varying(256) not null,
    CONSTRAINT eg_roles_role_name_key UNIQUE (name)
);

CREATE SEQUENCE SEQ_EG_ROLE
 START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE eg_role ADD CONSTRAINT eg_role_pk PRIMARY KEY (id, tenantid);

CREATE TABLE eg_userrole (
    roleid bigint NOT NULL,
    roleidtenantid character varying(256) NOT NULL,
    userid bigint NOT NULL,
    useridtenantid character varying(256) NOT NULL,
    FOREIGN KEY (roleid, roleidtenantid) REFERENCES eg_role (id, tenantid),
    FOREIGN KEY (userid, useridtenantid) REFERENCES eg_user (id, tenantid)
);