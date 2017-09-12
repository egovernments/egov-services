DROP TABLE IF EXISTS egov_citizen_service_req;

CREATE TABLE egov_citizen_service_req
(
 id character varying(50) NOT NULL,
 tenantid character varying(40) NOT NULL,
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
 jsonvalue character varying(50000),
 status character varying(20),
 CONSTRAINT egov_citizen_service_req_pkey PRIMARY KEY (id, tenantid)
);