--------- Trade License  ----------


-------------------START-------------------
CREATE TABLE egtl_license (
    id bigint NOT NULL,
    tenantId character varying(128) NOT NULL,
    applicationType character varying(50) NOT NULL,
    applicationNumber character varying(128) NOT NULL,
    licenseNumber character varying(128) NOT NULL,
    applicationDate timestamp without time zone NOT NULL,
    adhaarNumber character(12),
    mobileNumber character varying(10) NOT NULL,
    ownerName character varying(32) NOT NULL,
    fatherSpouseName character varying(32) NOT NULL,
    emailId character varying(128) NOT NULL,
    ownerAddress character varying(256) NOT NULL,
    propertyAssesmentNo character(15),
    localityId bigint NOT NULL,
    revenueWardId bigint NOT NULL,
    tradeAddress character varying(256) NOT NULL,
    ownerShipType character varying(50) NOT NULL,
    tradeTitle character varying(33) NOT NULL,
    tradeType character varying(50) NOT NULL,
    categoryId bigint NOT NULL,
    subCategoryId bigint NOT NULL,
    uomId bigint NOT NULL,
    quantity numeric NOT NULL,
    remarks character varying(256),
    tradeCommencementDate timestamp without time zone NOT NULL,
    agreementDate timestamp without time zone ,
    agreementNo character varying(128),
    isLegacy boolean,
    active boolean,
    expiryDate timestamp without time zone NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);



CREATE SEQUENCE seq_egtl_license;

ALTER TABLE ONLY egtl_license 
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_license');
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT pk_egtl_license PRIMARY KEY (id);
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT unq_tl_licenseno UNIQUE (licenseNumber,tenantId);
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT unq_tl_agrmtno UNIQUE (agreementNo);         

    
-------------------END-------------------


------------------START------------------
CREATE TABLE egtl_fee_details(
    id bigint NOT NULL,
    licenseId bigint NOT NULL,
    financialYear character varying(50) NOT NULL,
    amount double precision,
    paid boolean,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egtl_fee_details;

ALTER TABLE ONLY egtl_fee_details
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_fee_details');
ALTER TABLE ONLY egtl_fee_details
    ADD CONSTRAINT pk_egtl_fee_details PRIMARY KEY (id);
ALTER TABLE ONLY egtl_fee_details
    ADD CONSTRAINT fk_egtl_fee_details_licenseId FOREIGN KEY (licenseId) REFERENCES egtl_license(id);

-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_support_document(
    id bigint NOT NULL,
    licenseId bigint NOT NULL,
    documentTypeId bigint NOT NULL,
    fileStoreId character varying(128) NOT NULL,
    comments character varying(1024) NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egtl_support_document;

ALTER TABLE ONLY egtl_support_document
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_support_document');
ALTER TABLE ONLY egtl_support_document
    ADD CONSTRAINT pk_egtl_support_document PRIMARY KEY (id);
ALTER TABLE ONLY egtl_support_document
    ADD CONSTRAINT fk_egtl_support_document_licenseId FOREIGN KEY (licenseId) REFERENCES egtl_license(id);

-------------------END-------------------