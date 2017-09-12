------------------START------------------
CREATE TABLE egtl_notice_document(
    id bigint NOT NULL,
    licenseId bigint NOT NULL,
    documentName character varying(128),
    fileStoreId character varying(128) NOT NULL,
    tenantId character varying(128) NOT NULL,
    createdBy character varying,
    lastModifiedBy character varying,
    createdTime bigint,
    lastModifiedTime bigint
);

CREATE SEQUENCE seq_egtl_notice_document;

ALTER TABLE ONLY egtl_notice_document
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_notice_document');
ALTER TABLE ONLY egtl_notice_document
    ADD CONSTRAINT pk_egtl_notice_document PRIMARY KEY (id);
ALTER TABLE ONLY egtl_notice_document
    ADD CONSTRAINT fk_egtl_notice_document_licenseId FOREIGN KEY (licenseId) REFERENCES egtl_license(id);

-------------------END-------------------