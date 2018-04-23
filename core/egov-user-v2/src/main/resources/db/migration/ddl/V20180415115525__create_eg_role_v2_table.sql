CREATE TABLE eg_user_role_v2 (
    uuid character varying(64) NOT NULL,
 	userid character varying(64) NOT NULL REFERENCES eg_user_v2 (id),-- id of eg_user_v2 table
    rolecode character varying(50) NOT NULL,
	createdby character varying(64),
    lastmodifiedby character varying(64),
    createddate timestamp,
    lastmodifieddate timestamp,
    CONSTRAINT eg_user_role_code_key UNIQUE (rolecode, userid)
);