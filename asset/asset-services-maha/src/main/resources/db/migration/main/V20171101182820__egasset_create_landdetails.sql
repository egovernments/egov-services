CREATE TABLE egasset_asset_landdetails (

    code character varying(250),
    surveynumber character varying(250),
    area numeric,
    tenantid character varying(250),
    assetid bigint,
    CONSTRAINT pk_egasset_asset_landdetails PRIMARY KEY (code,assetid,tenantid)
    );
