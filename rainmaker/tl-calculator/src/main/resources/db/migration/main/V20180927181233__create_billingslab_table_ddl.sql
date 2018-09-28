CREATE TABLE eg_tl_billingslab (
tenantid varchar,
id varchar,
licensetype varchar,
structuretype  varchar,
tradetype varchar,
accessorycategory varchar,
type varchar,
uom varchar,
"from" numeric(12,2),
"to" numeric(12,2),
rate numeric(12,2),
createdtime bigint,
createdby varchar,
lastmodifiedtime bigint,
lastmodifiedby varchar,

CONSTRAINT pk_tl_billingslab  PRIMARY KEY(id,tenantid) );