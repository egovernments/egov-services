CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE egcl_receiptheader_history AS SELECT * FROM egcl_receiptheader WHERE tenantId = 'somerandomstring';
ALTER TABLE egcl_receiptheader_history ADD COLUMN uuid varchar(256) NOT NULL;

CREATE TABLE egcl_receiptdetails_history AS SELECT * FROM egcl_receiptdetails WHERE tenantId = 'somerandomstring';
ALTER TABLE egcl_receiptdetails_history ADD COLUMN uuid varchar(256) NOT NULL;

CREATE TABLE egcl_instrumentheader_history AS SELECT * FROM egcl_instrumentheader WHERE tenantId = 'somerandomstring';
ALTER TABLE egcl_instrumentheader_history ADD COLUMN uuid varchar(256) NOT NULL;
