alter table egcl_receiptheader drop column receiptdate;
alter table egcl_receiptheader drop column createddate;
alter table egcl_receiptheader drop column lastmodifieddate;

alter table egcl_receiptheader add column receiptdate BIGINT NOT NULL DEFAULT (100);
alter table egcl_receiptheader alter column receiptdate DROP DEFAULT;

alter table egcl_receiptheader add column createddate BIGINT NOT NULL DEFAULT (100);
alter table egcl_receiptheader alter column createddate DROP DEFAULT;

alter table egcl_receiptheader add column lastmodifieddate BIGINT NOT NULL DEFAULT (100);
alter table egcl_receiptheader alter column lastmodifieddate DROP DEFAULT;
