Alter TABLE egov_citizen_service_req add column status character varying(20), drop CONSTRAINT egov_citizen_service_req_pkey, add CONSTRAINT egov_citizen_service_req_pkey PRIMARY KEY (id, tenantid);

