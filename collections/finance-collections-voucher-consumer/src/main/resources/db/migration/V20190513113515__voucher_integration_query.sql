CREATE TABLE egf_voucher_integration_log(
id int PRIMARY KEY,
referenceNumber varchar(50),
status varchar(20),
voucherNumber varchar(25),
type varchar(20),
description text,
requestJson TEXT,
tenantId varchar(50)
);

CREATE SEQUENCE seq_egf_voucher_integration_log START 1;
