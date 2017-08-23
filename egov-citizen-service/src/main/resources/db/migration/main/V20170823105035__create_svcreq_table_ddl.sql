CREATE TABLE egov_citizen_service_req(
 id character varying(50) NOT NULL,
 tenantid character varying(40),
 userid bigint,
 servicecode character varying(40),
 consumercode character varying(50),
 email character varying(100),
 mobilenumber character varying(50),
 assignedto character varying(100),
 createddate bigint,
 lastmodifiedddate bigint,
 createdby bigint,
 lastmodifiedby bigint,
 jsonvalue json,
 
 CONSTRAINT egov_citizen_service_req_pkey PRIMARY KEY (id)
 );

CREATE SEQUENCE seq_citizen_service;