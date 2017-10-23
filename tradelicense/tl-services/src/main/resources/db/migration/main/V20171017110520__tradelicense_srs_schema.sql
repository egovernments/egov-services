---------------Renaming existing Trade License columns-----------------------------
ALTER TABLE egtl_license
RENAME adhaarnumber TO owneraadhaarnumber;

ALTER TABLE egtl_license 
ALTER COLUMN owneraadhaarnumber TYPE character varying(12);

ALTER TABLE egtl_license
RENAME emailid TO owneremailid;

ALTER TABLE egtl_license
RENAME mobilenumber TO ownermobilenumber;

---------------Adding new columns to Trade License-----------------------------
ALTER TABLE egtl_license
ADD COLUMN ownertype character varying DEFAULT 'INDIVIDUAL' NOT NULL,
ADD COLUMN ownerbirthyear character varying(4),
ADD COLUMN ownercorraddress character varying(250),
ADD COLUMN ownerCity character varying(50),
ADD COLUMN ownerpincode character varying(6),
ADD COLUMN ownerphonenumber character varying(10),
ADD COLUMN ownerphoto character varying,
ADD COLUMN establishmenttype character varying,
ADD COLUMN establishmentName character varying(100) DEFAULT '' NOT NULL,
ADD COLUMN establishmentRegNo character varying(30) DEFAULT '' NOT NULL,
ADD COLUMN establishmentcorraddress character varying(250) DEFAULT '' NOT NULL,
ADD COLUMN establishmentcity character varying(50),
ADD COLUMN establishmentpincode character varying(6) DEFAULT '' NOT NULL,
ADD COLUMN establishmentphoneno character varying(10) DEFAULT '' NOT NULL,
ADD COLUMN establishmentmobno character varying(10) DEFAULT '' NOT NULL,
ADD COLUMN establishmentemailid character varying(50) DEFAULT '' NOT NULL,
ADD COLUMN surveyorgatno character varying,
ADD COLUMN ctsOrFinalPlotNo character varying,
ADD COLUMN plotno character varying,
ADD COLUMN waterconnectionno character varying,
ADD COLUMN landownername character varying(100) DEFAULT '' NOT NULL,
ADD COLUMN isconsentlettertaken boolean,
ADD COLUMN businessdescription character varying DEFAULT '' NOT NULL,
ADD COLUMN prevlicenseno character varying,
ADD COLUMN prevlicenseDate bigint,
ADD COLUMN totalemployees integer DEFAULT 0 NOT NULL,
ADD COLUMN totalmachines integer,
ADD COLUMN licenseRejBefrForSamePremise boolean DEFAULT false NOT NULL,
ADD COLUMN expllicenseno character varying,
ADD COLUMN totalShifts integer;

---------------Drop default values on Trade License new columns-----------------------------
ALTER TABLE egtl_license
ALTER COLUMN ownertype DROP DEFAULT,
ALTER COLUMN establishmentName DROP DEFAULT,
ALTER COLUMN establishmentRegNo DROP DEFAULT,
ALTER COLUMN establishmentcorraddress DROP DEFAULT,
ALTER COLUMN establishmentpincode DROP DEFAULT,
ALTER COLUMN establishmentphoneno DROP DEFAULT,
ALTER COLUMN establishmentmobno DROP DEFAULT,
ALTER COLUMN establishmentemailid DROP DEFAULT,
ALTER COLUMN landownername DROP DEFAULT,
ALTER COLUMN businessdescription DROP DEFAULT,
ALTER COLUMN totalemployees DROP DEFAULT,
ALTER COLUMN licenseRejBefrForSamePremise DROP DEFAULT;

---------------Trade Partner-----------------------------
CREATE TABLE egtl_trade_partner(
    id bigint NOT NULL,
    tenantid character varying(128) NOT NULL,
    licenseId bigint NOT NULL,
    aadhaarNumber character varying(12),
    fullName character varying(100),
    gender character varying(30),
    birthYear character varying(4),
    emailId character varying(50) NOT NULL,
    designation character varying(100),
    residentialAddress character varying(250),
    correspondenceAddress character varying(250),
    phoneNumber character varying(10),
    mobileNumber character varying(10) NOT NULL,
    photo character varying,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egtl_trade_partner;

ALTER TABLE ONLY egtl_trade_partner
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_trade_partner');
ALTER TABLE ONLY egtl_trade_partner
    ADD CONSTRAINT pk_egtl_trade_partner PRIMARY KEY (id);
ALTER TABLE ONLY egtl_trade_partner
    ADD CONSTRAINT fk_egtl_trade_partner_licenseId FOREIGN KEY (licenseId) REFERENCES egtl_license(id);

---------------Trade Shift-----------------------------
CREATE TABLE egtl_trade_shift(
    id bigint NOT NULL,
    tenantid character varying(128) NOT NULL,
    licenseId bigint NOT NULL,
    shiftNo integer NOT NULL,
    fromTime bigint NOT NULL,
    toTime bigint NOT NULL,
    remarks character varying,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egtl_trade_shift;

ALTER TABLE ONLY egtl_trade_shift
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_trade_shift');
ALTER TABLE ONLY egtl_trade_shift
    ADD CONSTRAINT pk_egtl_trade_shift PRIMARY KEY (id);
ALTER TABLE ONLY egtl_trade_shift
    ADD CONSTRAINT fk_egtl_trade_shift_licenseId FOREIGN KEY (licenseId) REFERENCES egtl_license(id);