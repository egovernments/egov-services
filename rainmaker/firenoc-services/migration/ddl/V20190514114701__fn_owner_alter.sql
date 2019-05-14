CREATE TABLE public.eg_fn_owner
(
    uuid character varying(64),
    tenantid character varying(256),
    firenocdetailsuuid character varying(64) NOT NULL,
    isactive boolean,
    isprimaryowner boolean,
    ownertype character varying(64),
    ownershippercentage character varying(64),
    relationship character varying(64),
    active boolean,
    institutionid character varying(64),
    createdby character varying(64),
    createdtime bigint,
    lastmodifiedby character varying(64),
    lastmodifiedtime bigint,
    useruuid character varying(64),
    CONSTRAINT pk_eg_fn_owner PRIMARY KEY (uuid, firenocdetailsuuid),
    CONSTRAINT uk_eg_fn_owner UNIQUE (uuid)
);