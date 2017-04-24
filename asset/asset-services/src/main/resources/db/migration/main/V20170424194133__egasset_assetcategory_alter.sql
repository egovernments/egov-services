DROP TABLE egasset_assetcategory;


CREATE TABLE egasset_assetcategory (
    id bigint NOT NULL,
    name character varying(250) NOT NULL,
    code character varying(250) NOT NULL,
    parentid bigint,
    assetcategorytype character varying(250) NOT NULL,
    depreciationmethod character varying(250),
    depreciationrate bigint,
    assetaccount bigint,
    accumulateddepreciationaccount bigint,
    revaluationreserveaccount bigint,
    depreciationexpenseaccount bigint,
    unitofmeasurement bigint,
    customfields character varying(10000),
    tenantid character varying(250) NOT NULL,
    createdby character varying(64) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby character varying(64),
    lastmodifieddate timestamp without time zone,
    CONSTRAINT pk_egasset_assetcategory PRIMARY KEY (id,tenantid),
    CONSTRAINT uk_egasset_assetcategory_code UNIQUE (code)
);


