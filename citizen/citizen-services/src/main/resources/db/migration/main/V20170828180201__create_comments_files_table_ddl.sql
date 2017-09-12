CREATE TABLE egov_citizen_service_req_comments(

 id BIGINT NOT NULL,
 srn character varying(50) NOT NULL,
 tenantid character varying(50) NOT NULL,
 commentfrom character varying(50) NOT NULL,
 comment character varying(500) NOT NULL,
 commentdate BIGINT NOT NULL,
 createddate BIGINT,
 lastmodifiedddate BIGINT,
 createdby BIGINT,
 lastmodifiedby BIGINT,

 CONSTRAINT egov_citizen_service_req_comments_pkey PRIMARY KEY (id),
 CONSTRAINT fk_comments_srn FOREIGN KEY (srn, tenantid) REFERENCES egov_citizen_service_req (id, tenantid)

);

CREATE TABLE egov_citizen_service_req_documents(

 id BIGINT NOT NULL,
 srn character varying(50) NOT NULL,
 tenantid character varying(50) NOT NULL,
 uploadedby character varying(50) NOT NULL,
 filestoreid character varying(500) NOT NULL,
 uploaddate BIGINT NOT NULL,
 isactive boolean NOT NULL,
 createddate BIGINT,
 lastmodifiedddate BIGINT,
 createdby BIGINT,
 lastmodifiedby BIGINT,

 CONSTRAINT egov_citizen_service_req_docs_pkey PRIMARY KEY (id),
 CONSTRAINT fk_tenant_srn FOREIGN KEY (srn, tenantid) REFERENCES egov_citizen_service_req (id, tenantid),
 CONSTRAINT unique_docs_srn UNIQUE (srn, filestoreid)


);

CREATE SEQUENCE seq_citizen_service_comments;
CREATE SEQUENCE seq_citizen_service_documents;


ALTER TABLE egov_citizen_service_req ADD COLUMN modulestatus character varying(50);
ALTER TABLE egov_citizen_service_req ADD COLUMN additionalfee BIGINT;