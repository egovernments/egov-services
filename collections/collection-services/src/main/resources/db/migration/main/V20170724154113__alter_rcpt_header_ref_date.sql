alter table egcl_receiptheader drop column referencedate;
alter table egcl_receiptheader add column referencedate BIGINT NOT NULL DEFAULT(100);
alter table egcl_receiptheader alter column receiptdate DROP DEFAULT;

