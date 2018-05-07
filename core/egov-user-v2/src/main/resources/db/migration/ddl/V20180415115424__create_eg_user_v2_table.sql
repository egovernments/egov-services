CREATE TABLE eg_user_v2 (
    uuid character varying(64) NOT NULL, -- uuid
    tenantid character varying(256) NOT NULL,
    username character varying(64) NOT NULL,
    type character varying(50),
    password character varying(64),
    locale character varying(16),
    mobile character varying(32),
    email character varying(128),
    active boolean,
    accountLocked boolean,
    createdby character varying(64),
    lastmodifiedby character varying(64),
    createddate bigint,
    lastmodifieddate bigint,
    CONSTRAINT pk_eg_user_v2 PRIMARY KEY (uuid),
    CONSTRAINT unq_eg_user_v2_username UNIQUE(tenantid,username)
 );

 CREATE TABLE eg_user_detail_v2 (
 	uuid character varying(64) NOT NULL,
 	userId character varying(64) NOT NULL REFERENCES eg_user_v2 (uuid),-- id of eg_user_v2 table
    salutation character varying(5),
    firstname character varying(32),
    middlename character varying(32),
    lastname character varying(32),
    aadhaarnumber character varying(20),
    pwdexpirydate timestamp,
    gender character varying(16),
    dob timestamp,
    altcontactnumber character varying(50),
    fathername character varying(32),
    husbandname character varying(32),
    pan character varying(16),
    signature character varying(36),
    identificationmark character varying(300),
    photo character varying(64),
    createdby character varying(64),
    lastmodifiedby character varying(64),
    createddate bigint,
    lastmodifieddate bigint,
    CONSTRAINT pk_eg_user_detail_v2 PRIMARY KEY (uuid)
    );
