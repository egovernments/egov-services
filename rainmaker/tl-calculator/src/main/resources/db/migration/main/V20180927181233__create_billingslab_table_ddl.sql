CREATE TABLE eg_tl_billingslab (
tenantid VARCHAR,
id uuid,
licensetype VARCHAR,
structuretype  VARCHAR,
tradetype VARCHAR,
accessorycategory VARCHAR,
type VARCHAR,
uom VARCHAR,
"from" VARCHAR,
"to" VARCHAR,
rate bigint,
createdtime bigint,
createdby varchar,
lastmodifiedtime bigint,
lastmodifiedby varchar,

CONSTRAINT pk_tl_billingslab  PRIMARY KEY(id,tenantid) );