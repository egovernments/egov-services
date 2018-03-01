CREATE TABLE eg_user_details (
    userid bigint NOT NULL,
    tenantid character varying(256) not null,
    firstname character varying(150),
    middlename character varying(150),
    lastname character varying(150), 
    dob bigint,
    altcontactnumber character varying(50),
    fathername character varying(150),
    husbandName character varying(150),
    bloodgroup character varying(32),
    pan character varying(10),
    photo character varying(36),
    identificationmark character varying(300),
    signature character varying(36),
    CONSTRAINT eg_user_details_fkey FOREIGN KEY (userid, tenantid) REFERENCES eg_user (id, tenantid),
    CONSTRAINT eg_user_details_type_unique UNIQUE (userid, tenantid)
    );