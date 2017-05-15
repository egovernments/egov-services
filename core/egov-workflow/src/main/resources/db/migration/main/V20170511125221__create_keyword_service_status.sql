CREATE TABLE keyword_service_status (
    keyword character varying(50) not null,
    tenantid character varying(256) not null,
    servicestatuscode character varying(20) not null,
    version bigint DEFAULT 0,
    createdby bigint NOT NULL,
    createddate timestamp NOT NULL,
    lastmodifiedby bigint NOT NULL,
    lastmodifieddate timestamp NOT NULL,
    constraint uk_keyword_tenantid_servicestatuscode unique (keyword,tenantid,servicestatuscode)
);
