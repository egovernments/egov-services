CREATE TABLE eg_token (
    id character(36) PRIMARY KEY,
    tokennumber character varying(128) NOT NULL,
    tokenidentity character varying(100) NOT NULL,
    ttlsecs bigint NOT NULL,
    createddate timestamp NOT NULL,
    lastmodifieddate timestamp,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    version bigint
);

CREATE INDEX idx_token_number_identity ON eg_token (tokennumber, tokenidentity);