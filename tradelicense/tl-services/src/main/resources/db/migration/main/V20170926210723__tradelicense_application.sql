--- License Application ---
CREATE TABLE egtl_license_application (
    id bigint NOT NULL,
    applicationNumber character varying(128),
    tenantId character varying(128) ,
    applicationType character varying(20) ,
    status character varying(128),
    state_id character varying(128),
    applicationDate timestamp without time zone ,
    licenseId bigint NOT NULL,
    createdTime bigint NOT NULL,
    createdBy character varying,
    lastModifiedTime bigint ,
    lastModifiedBy character varying
);

CREATE SEQUENCE seq_egtl_license_application;

ALTER TABLE egtl_license_application
    ALTER COLUMN id SET DEFAULT nextval('seq_egtl_license_application'),
    ADD CONSTRAINT pk_egtl_license_application PRIMARY KEY (id),
    ADD CONSTRAINT fk_egtl_license_application_licenseId FOREIGN KEY (licenseId) REFERENCES egtl_license(id);

INSERT INTO egtl_license_application (applicationNumber, tenantId,applicationType,status,applicationDate,licenseId,createdTime,createdBy)  SELECT applicationNumber,tenantId,applicationType,status,applicationDate,id,createdTime,createdBy FROM egtl_license;


ALTER TABLE egtl_license
DROP COLUMN applicationType,
DROP COLUMN applicationNumber,
DROP COLUMN applicationDate;


 ALTER TABLE egtl_support_document
 ADD COLUMN applicationId bigint NOT NULL DEFAULT 0,
ADD COLUMN tenantId character varying(128);


--- Support Document ---

UPDATE  egtl_support_document set applicationId = app.id FROM ( SELECT id,licenseId FROM egtl_license_application ) as app WHERE egtl_support_document.licenseId = app.licenseId;

UPDATE  egtl_support_document set tenantId = app.tenantId FROM ( SELECT tenantId, licenseId FROM egtl_license_application ) as app WHERE egtl_support_document.licenseId = app.licenseId;

 ALTER TABLE egtl_support_document 
DROP COLUMN licenseid,
ALTER COLUMN applicationId DROP DEFAULT,
ADD CONSTRAINT fk_egtl_support_document_application FOREIGN KEY (applicationId) REFERENCES egtl_license_application(id);

--- FEE DETAILS ---
 ALTER TABLE egtl_fee_details
 ADD COLUMN applicationId bigint NOT NULL DEFAULT 0,
ADD COLUMN tenantId character varying(128);

UPDATE  egtl_fee_details set applicationId = app.id FROM ( SELECT id,licenseId FROM egtl_license_application ) as app WHERE egtl_fee_details.licenseId = app.licenseId;

UPDATE  egtl_fee_details set tenantId = app.tenantId FROM ( SELECT tenantId,licenseId FROM egtl_license_application ) as app WHERE egtl_fee_details.licenseId = app.licenseId;

 ALTER TABLE egtl_fee_details 
DROP COLUMN licenseId,
ALTER COLUMN applicationId DROP DEFAULT,
ADD CONSTRAINT fk_egtl_fee_details_application FOREIGN KEY (applicationId) REFERENCES egtl_license_application(id);