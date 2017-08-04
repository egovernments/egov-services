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


