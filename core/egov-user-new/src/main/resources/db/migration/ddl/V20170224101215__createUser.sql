CREATE TABLE eg_user (
    id bigint NOT NULL,
    salutation character varying(5),
    locale character varying(16),
    username character varying(64) NOT NULL,
    password character varying(64),
    pwdexpirytime bigint NOT NULL,
    mobilenumber character varying(50),
    emailid character varying(128),
    name character varying(100),
    gender character varying(10),
    aadhaarnumber character varying(20),
    type character varying(50),
    active boolean,
    accountlocked boolean DEFAULT false,
    tenantid character varying(256) not null,
    createdtime bigint NOT NULL,
    lastmodifiedtime bigint NOT NULL,
    createdby character varying(250),
    lastmodifiedby character varying(250),
    CONSTRAINT eg_user_pkey PRIMARY KEY (id, tenantid),
    CONSTRAINT eg_user_user_name_tenant UNIQUE (username, tenantid)
);

CREATE SEQUENCE seq_eg_user
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;