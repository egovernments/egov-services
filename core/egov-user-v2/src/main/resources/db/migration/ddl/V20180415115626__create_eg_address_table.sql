CREATE TABLE eg_user_address_v2 (
    tenantid character varying(256) NOT NULL,
    uuid character varying(64) NOT NULL,
 	userid character varying(64) NOT NULL REFERENCES eg_user_v2 (uuid),-- id of eg_user_v2 table
    latitude numeric(9,6),
    longitude numeric(9,6),
    addressid character varying(64),
 	addressnumber character varying(256),
    type character varying(64),
    addressline1 character varying(256),
    addressline2 character varying(256),
    landmark character varying(256),
    city character varying(256),    
    pincode character varying(10),
    detail character varying(256),
    createdby character varying(64),
    lastmodifiedby character varying(64),
    createddate bigint,
    lastmodifieddate bigint,
    CONSTRAINT pk_eg_user_address_v2 PRIMARY KEY (uuid,tenantid)
    );