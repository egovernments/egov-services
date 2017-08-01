------------------START------------------
CREATE TABLE egtl_mstr_category(
    id bigint NOT NULL,
    tenantId character varying(128) NOT NULL,
    name character varying(256) NOT NULL,
    code character varying(50) NOT NULL,
    parentId bigint,
    businessNature character varying(50),
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egtl_mstr_category;

ALTER TABLE ONLY egtl_mstr_category 
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_mstr_category');
ALTER TABLE ONLY egtl_mstr_category
    ADD CONSTRAINT pk_egtl_mstr_category PRIMARY KEY (id);
ALTER TABLE ONLY egtl_mstr_category
    ADD CONSTRAINT unq_tlcategory UNIQUE (tenantId, code, name);
ALTER TABLE ONLY egtl_mstr_category
    ADD CONSTRAINT fk_egtl_mstr_category_parentId FOREIGN KEY (parentId) REFERENCES egtl_mstr_category(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_uom(
    id bigint NOT NULL,
    tenantId character varying(128) NOT NULL,
    name character varying(256) NOT NULL,
    code character varying(50) NOT NULL,
    active boolean,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egtl_mstr_uom;

ALTER TABLE ONLY egtl_mstr_uom 
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_mstr_uom');
ALTER TABLE ONLY egtl_mstr_uom
    ADD CONSTRAINT pk_egtl_mstr_uom PRIMARY KEY (id);
ALTER TABLE ONLY egtl_mstr_uom
    ADD CONSTRAINT unq_tluom UNIQUE (tenantId, code, name);
-------------------END-------------------

-------------------START-------------------
CREATE TABLE egtl_category_details (
    id bigint NOT NULL,
    categoryId bigint NOT NULL,
    feeType character varying(50) NOT NULL,
    rateType character varying(50) NOT NULL,
    uomId bigint NOT NULL
);

CREATE SEQUENCE seq_egtl_category_details;

ALTER TABLE ONLY egtl_category_details 
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_category_details');
ALTER TABLE ONLY egtl_category_details
    ADD CONSTRAINT pk_egtl_category_details PRIMARY KEY (id);
ALTER TABLE ONLY egtl_category_details
    ADD CONSTRAINT unq_tlcategory_details UNIQUE (categoryId, feeType, rateType);
ALTER TABLE ONLY egtl_category_details
    ADD CONSTRAINT fk_egtl_category_details_categoryId FOREIGN KEY (categoryId) REFERENCES egtl_mstr_category(id);
ALTER TABLE ONLY egtl_category_details
    ADD CONSTRAINT fk_egtl_category_details_uomId FOREIGN KEY (uomId) REFERENCES egtl_mstr_uom(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_document_type(
    id bigint NOT NULL,
    tenantId character varying(128) NOT NULL,
    name character varying(256) NOT NULL,
    mandatory boolean DEFAULT true,
    enabled boolean DEFAULT true,
    applicationType character varying(50) NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egtl_mstr_document_type;

ALTER TABLE ONLY egtl_mstr_document_type 
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_mstr_document_type');
ALTER TABLE ONLY egtl_mstr_document_type
    ADD CONSTRAINT pk_egtl_mstr_document_type PRIMARY KEY (id);
ALTER TABLE ONLY egtl_mstr_document_type
    ADD CONSTRAINT unq_tldocument_type UNIQUE (tenantId, applicationType, name);
-------------------END-------------------
    
------------------START------------------
CREATE TABLE egtl_mstr_penalty_rate(
    id bigint NOT NULL,
    tenantId character varying(128) NOT NULL,
    applicationType character varying(50) NOT NULL,
    fromRange bigint NOT NULL,
    toRange bigint NOT NULL,
    rate double precision,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egtl_mstr_penalty_rate;

ALTER TABLE ONLY egtl_mstr_penalty_rate 
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_mstr_penalty_rate');
ALTER TABLE ONLY egtl_mstr_penalty_rate
    ADD CONSTRAINT pk_egtl_mstr_penalty_rate PRIMARY KEY (id);
-------------------END-------------------
    
------------------START------------------
CREATE TABLE egtl_mstr_fee_matrix(
    id bigint NOT NULL,
    tenantId character varying(128) NOT NULL,
    applicationType character varying(50) NOT NULL,
    categoryId bigint NOT NULL,
    businessNature  character varying(50) NOT NULL,
    subCategoryId bigint NOT NULL,
    financialYear character varying(50) NOT NULL,
    effectivefrom timestamp without time zone,
    effectiveto timestamp without time zone,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egtl_mstr_fee_matrix;

ALTER TABLE ONLY egtl_mstr_fee_matrix 
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_mstr_fee_matrix');
ALTER TABLE ONLY egtl_mstr_fee_matrix
    ADD CONSTRAINT pk_egtl_mstr_fee_matrix PRIMARY KEY (id);
ALTER TABLE ONLY egtl_mstr_fee_matrix
    ADD CONSTRAINT unq_tlfee_matrix UNIQUE (tenantId, applicationType, categoryId, subCategoryId, financialYear);
ALTER TABLE ONLY egtl_mstr_fee_matrix
    ADD CONSTRAINT fk_egtl_mstr_fee_matrix_categoryId FOREIGN KEY (categoryId) REFERENCES egtl_mstr_category(id);
ALTER TABLE ONLY egtl_mstr_fee_matrix
    ADD CONSTRAINT fk_egtl_mstr_fee_matrix_subCategoryId FOREIGN KEY (subCategoryId) REFERENCES egtl_mstr_category(id);
-------------------END-------------------
    
-------------------START-------------------
CREATE TABLE egtl_fee_matrix_details (
    id bigint NOT NULL,
    feeMatrixId bigint NOT NULL,
    uomFrom bigint NOT NULL,
    uomto bigint NOT NULL,
    amount double precision
);

CREATE SEQUENCE seq_egtl_fee_matrix_details;

ALTER TABLE ONLY egtl_fee_matrix_details 
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_fee_matrix_details');
ALTER TABLE ONLY egtl_fee_matrix_details
    ADD CONSTRAINT pk_egtl_fee_matrix_details PRIMARY KEY (id);
ALTER TABLE ONLY egtl_fee_matrix_details
    ADD CONSTRAINT fk_egtl_fee_matrix_details_feeMatrixId FOREIGN KEY (feeMatrixId) REFERENCES egtl_mstr_fee_matrix(id);
-------------------END-------------------


-------------------START-------------------
CREATE TABLE egtl_mstr_status (
    id bigint NOT NULL,
    tenantId character varying(128) NOT NULL,
    name character varying(256) NOT NULL,
    code character varying(50) NOT NULL,
    active boolean,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egtl_mstr_status;


ALTER TABLE ONLY egtl_mstr_status 
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_mstr_status');
ALTER TABLE ONLY egtl_mstr_status
    ADD CONSTRAINT pk_egtl_mstr_status PRIMARY KEY (id);
ALTER TABLE ONLY egtl_mstr_status
    ADD CONSTRAINT unq_tlstatus UNIQUE (tenantId, code, name);
-------------------END-------------------

--------- Trade License  ----------


-------------------START-------------------
CREATE TABLE egtl_license (
    id bigint NOT NULL,
    tenantId character varying(128) NOT NULL,
    applicationType character varying(50) NOT NULL,
    applicationNumber character varying(128) NOT NULL,
    licenseNumber character varying(128) NOT NULL,
    oldLicenseNumber character varying(128),
    applicationDate timestamp without time zone NOT NULL,
    adhaarNumber character(12),
    mobileNumber character varying(10) NOT NULL,
    ownerName character varying(32) NOT NULL,
    fatherSpouseName character varying(32) NOT NULL,
    emailId character varying(128) NOT NULL,
    ownerAddress character varying(256) NOT NULL,
    propertyAssesmentNo character(15),
    localityId bigint NOT NULL,
    wardId bigint NOT NULL,
    tradeAddress character varying(256) NOT NULL,
    ownerShipType character varying(50) NOT NULL,
    tradeTitle character varying(33) NOT NULL,
    tradeType character varying(50) NOT NULL,
    categoryId bigint NOT NULL,
    subCategoryId bigint NOT NULL,
    uomId bigint NOT NULL,
    uomValue numeric NOT NULL,
    remarks character varying(256) NOT NULL,
    tradeCommencementDate timestamp without time zone NOT NULL,
    agrementDate timestamp without time zone ,
    agrementNo character varying(128),
    isLegacy boolean,
    active boolean,
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
    ADD CONSTRAINT unq_tl_licenseno UNIQUE (licenseNumber);
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT unq_tl_ollicenseno UNIQUE (oldLicenseNumber);
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT unq_tl_agrmtno UNIQUE (agrementNo);    
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT fk_egtl_license_categoryId FOREIGN KEY (categoryId) REFERENCES egtl_mstr_category(id);
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT fk_egtl_license_subCategoryId FOREIGN KEY (subCategoryId) REFERENCES egtl_mstr_category(id);    
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT fk_egtl_license_uomId FOREIGN KEY (uomId) REFERENCES egtl_mstr_uom(id);        

    
-------------------END-------------------


------------------START------------------
CREATE TABLE egtl_fee_details(
    id bigint NOT NULL,
    tenantId character varying(128) NOT NULL,
    licenseId bigint NOT NULL,
    financialYear character varying(50) NOT NULL,
    rate double precision,
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
    tenantId character varying(128) NOT NULL,
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
ALTER TABLE ONLY egtl_support_document
    ADD CONSTRAINT fk_egtl_support_document_docTypeId FOREIGN KEY (documentTypeId) REFERENCES egtl_mstr_document_type(id);

-------------------END-------------------
