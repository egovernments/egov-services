CREATE TABLE eg_user_enc
(
  title character varying(8),
  salutation character varying(5),
  dob timestamp without time zone,
  locale character varying(16),
  username varchar (180) NOT NULL,
  password character varying(64) NOT NULL,
  pwdexpirydate timestamp without time zone DEFAULT now(),
  mobilenumber varchar (150),
  altcontactnumber varchar (150),
  emailid varchar (300),
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  active boolean,
  name varchar (250),
  gender smallint,
  pan varchar (65),
  aadhaarnumber varchar (85),
  type character varying(50),
  version numeric DEFAULT 0,
  guardian varchar (250),
  guardianrelation character varying(32),
  signature character varying(36),
  accountlocked boolean DEFAULT false,
  bloodgroup character varying(32),
  photo character varying(36),
  identificationmark character varying(300),
  tenantid character varying(256) NOT NULL,
  id bigint NOT NULL,
  uuid character(36),
  CONSTRAINT eg_user_enc_pkey PRIMARY KEY (id, tenantid),
  CONSTRAINT eg_user_enc_user_name_tenant UNIQUE (username, type, tenantid)
);


CREATE INDEX IDX_EG_USER_ENC_USERNAME ON EG_USER_ENC (username);
CREATE INDEX IDX_EG_USER_ENC_NAME ON EG_USER_ENC (name);
CREATE INDEX IDX_EG_USER_ENC_ACTIVE ON EG_USER_ENC (active);
CREATE INDEX IDX_EG_USER_ENC_MOBILE ON EG_USER_ENC (mobilenumber);
CREATE INDEX IDX_EG_USER_ENC_TYPE ON EG_USER_ENC (type);
CREATE INDEX IDX_EG_USER_ENC_UUID ON EG_USER_ENC (uuid);



CREATE TABLE eg_user_address_enc
(
  id bigint NOT NULL,
  version numeric DEFAULT 0,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  type character varying(50) NOT NULL,
  address varchar (440),
  city character varying(300),
  pincode character varying(10),
  userid bigint NOT NULL,
  tenantid character varying(256) NOT NULL,
  CONSTRAINT eg_user_address_enc_pkey PRIMARY KEY (id),
  CONSTRAINT eg_user_address_enc_user_fkey FOREIGN KEY (userid, tenantid)
      REFERENCES eg_user_enc (id, tenantid) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT eg_user_address_enc_type_unique UNIQUE (userid, tenantid, type)
);
