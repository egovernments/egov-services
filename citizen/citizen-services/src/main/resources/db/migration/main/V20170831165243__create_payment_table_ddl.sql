CREATE TABLE egov_citizen_service_payments(

 id BIGINT NOT NULL,
 srn character varying(50) NOT NULL,
 userid BIGINT NOT NULL,
 pgrequest character varying(50000) NOT NULL,
 pgresponse character varying(50000),
 transactionid character varying(50),
 tenantid character varying(50) NOT NULL,
 amount BIGINT,
 createddate BIGINT,
 lastmodifiedddate BIGINT,
 createdby BIGINT,
 lastmodifiedby BIGINT,

 CONSTRAINT egcs_payment_pkey PRIMARY KEY (id),
 CONSTRAINT fk_tenant_srn FOREIGN KEY (srn, tenantid) REFERENCES egov_citizen_service_req (id, tenantid)

);

CREATE SEQUENCE seq_citizen_payment;
